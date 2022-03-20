package com.youngdatafan.di.run.management.datasource;

import com.youngdatafan.dataintegration.core.exception.DpException;
import com.youngdatafan.dataintegration.core.exception.ValidationException;
import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.dataintegration.core.util.JsonUtils;
import com.youngdatafan.dataintegration.core.util.StatusCode;
import com.youngdatafan.dataintegration.core.util.sql.DataSourceWrap;
import com.youngdatafan.dataintegration.core.util.sql.DatabaseType;
import com.youngdatafan.di.run.management.datasource.feign.DatasourceServiceApiClient;
import com.youngdatafan.portal.model.management.datasource.dto.DatasourceDTO;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariPoolMXBean;
import java.lang.management.ManagementFactory;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PreDestroy;
import javax.management.JMX;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.pentaho.di.core.encryption.KettleTwoWayPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;



/**
 * 插件运行数据源仓库.
 *
 * @author gavin
 * @since 2020/2/21 2:56 下午
 */
@Slf4j
@Component
public class PluginRunDataSourceRepository {

    private static final Pattern URL_PATTERN = Pattern.compile("([\\w.]+):(\\d+)[:/]?([\\w.]+)?");

    private static final Pattern URL_TD_PATTERN = Pattern.compile("([\\w.]+)/DATABASE=(\\w+).*DBS_PORT=(\\d+)");

    /**
     * 清理失败的dataSource对象
     */
    private final Map<String, DataSourceWrap> clearFailDataSources = new ConcurrentHashMap<>();
    private final DatasourceServiceApiClient datasourceServiceApiClient;
    private final KettleTwoWayPasswordEncoder PASSWORD_ENCODER = new KettleTwoWayPasswordEncoder();
    /**
     * 数据源监控对象
     */
    private final Map<String, HikariPoolMXBean> datasourceMxBeans = new ConcurrentHashMap<>();
    /**
     * 数据源map对象
     */
    private volatile Map<String, DataSourceWrap> dataSources = new ConcurrentHashMap<>();
    /**
     * 等待获取连接池的最大超时时间
     */
    @Value("${dp.project.datasource.maxWaitTimeout:10000}")
    private int maxWaitTimeout;
    @Value("${dp.project.datasource.maxFreeTimeMinute:180}")
    private int maxFreeTimeMinute;

    @Value("${dp.project.datasource.minPoolSize:1}")
    private int minPoolSize;

    @Value("${dp.project.datasource.maxPoolSize:20}")
    private int maxPoolSize;

    @Autowired
    public PluginRunDataSourceRepository(DatasourceServiceApiClient datasourceServiceApiClient) {
        this.datasourceServiceApiClient = datasourceServiceApiClient;
    }

    /**
     * 清理失败的数据源
     */
    @Scheduled(initialDelay = 4000, fixedDelayString = "${dp.project.datasource.clearFailDsFixedDelay:60000}")
    public void clearFailDataSource() {
        log.info("开始处理上一次清理失败的连接池");

        for (String datasourceId : clearFailDataSources.keySet()) {
            close(datasourceId, clearFailDataSources.remove(datasourceId));
        }
    }

    /**
     * 清理修改的数据源
     */
    @Scheduled(initialDelay = 6000, fixedDelayString = "${dp.project.datasource.clearModifyDsFixedDelay:300000}")
    public void clearModifyDataSource() {
        log.info("开始清理修改的连接池");

        for (String datasourceId : dataSources.keySet()) {
            // 打印数据源状态
            printDatasourceStatus(datasourceId);

            // 获取内存中的数据源
            final DataSourceWrap dataSourceWrap = dataSources.get(datasourceId);

            try {
                DataSourceWrap dbDataSourceWrap = new DataSourceWrap();
                // 查询并且设置数据源信息
                settingDataSourceWrap("", datasourceId, dbDataSourceWrap, false);

                final boolean matched = matchDataSourceWrap(dataSourceWrap, dbDataSourceWrap);
                if (!matched) {
                    // 数据库不存在数据源则加入到清理map中
                    log.warn("数据库中数据源有更改，加入到清理map中 ,datasourceId: {}", datasourceId);
                    final DataSourceWrap remove = dataSources.remove(datasourceId);
                    clearFailDataSources.put(datasourceId + "_" + RandomStringUtils.randomAlphabetic(6), remove);
                }

            } catch (ValidationException e) {
                log.error("datasourceId: {}", datasourceId, e);

                log.warn("数据库不存在数据源则，加入到清理map中 ,datasourceId: {}", datasourceId);
                final DataSourceWrap remove = dataSources.remove(datasourceId);
                clearFailDataSources.put(datasourceId + "_" + RandomStringUtils.randomAlphabetic(6), remove);

            } catch (Exception e) {
                log.error("datasourceId: {}", datasourceId, e);
            }
        }
    }

    /**
     * 清理空闲的数据源
     */
    @Scheduled(initialDelay = 2000, fixedDelayString = "${dp.project.datasource.clearFreeDsFixedDelay:300000}")
    public void clearFreeDataSource() {
        log.info("开始清理空闲连接池");
        // 记录清理时间
        final long minTime = System.currentTimeMillis() - (maxFreeTimeMinute * 1000 * 60);

        for (String datasourceId : dataSources.keySet()) {
            // 打印数据源状态
            printDatasourceStatus(datasourceId);

            // 获取内存中的数据源
            final DataSourceWrap dataSourceWrap = dataSources.get(datasourceId);

            final long lastUsedTime = dataSourceWrap.getLastUsedTime();
            if (lastUsedTime < minTime) {
                log.info("删除空闲线程池: datasourceId:{} ，lastUsedTime:{}", datasourceId, lastUsedTime);
                final DataSourceWrap remove = dataSources.remove(datasourceId);

                if (remove.getLastUsedTime() == lastUsedTime) {
                    close(datasourceId, remove);
                } else {
                    // 说明并发在使用，尝试重新加入到map中
                    final DataSourceWrap dataSourceWrap1 = dataSources.putIfAbsent(datasourceId, remove);
                    if (dataSourceWrap1 != null) {
                        // 清理失败的
                        close(datasourceId, clearFailDataSources.remove(datasourceId));

                        log.info("连接池{}，正在并发使用放入失败，丢入清理失败的map中，等待后续清理。", datasourceId);
                        clearFailDataSources.put(datasourceId + "_" + RandomStringUtils.randomAlphabetic(6), dataSourceWrap1);
                    }
                }
            }

            // 验证连接池是否可用，不可用则丢入清理map中，等待后续定期清理
            try (Connection conn = dataSourceWrap.getDataSource().getConnection()) {
                log.debug("测试连接池成功 {}", datasourceId);
            } catch (Exception e) {
                final DataSourceWrap remove = dataSources.remove(datasourceId);
                if (remove != null) {
                    log.info("连接池{}，获取连接失败放入失败，丢入清理失败的map中，等待后续清理。", datasourceId);
                    clearFailDataSources.put(datasourceId + "_" + RandomStringUtils.randomAlphabetic(6), remove);
                }
            }

        }
    }

    /**
     * 比较两个数据源对象是否相等
     */
    private boolean matchDataSourceWrap(DataSourceWrap dataSourceWrap, DataSourceWrap dbDataSourceWrap) {
        if (customEquals(dataSourceWrap.getHost(), dbDataSourceWrap.getHost())
                && customEquals(dataSourceWrap.getDb(), dbDataSourceWrap.getDb())
                && customEquals(dataSourceWrap.getPort(), dbDataSourceWrap.getPort())
                && customEquals(dataSourceWrap.getDatabaseType(), dbDataSourceWrap.getDatabaseType())
                && customEquals(dataSourceWrap.getUser(), dbDataSourceWrap.getUser())
                && customEquals(dataSourceWrap.getPass(), dbDataSourceWrap.getPass())) {

            final Map<String, String> params = dataSourceWrap.getParams() == null ? new HashMap<>(1) : dataSourceWrap.getParams();
            final Map<String, String> dbParams = dbDataSourceWrap.getParams() == null ? new HashMap<>(1) : dbDataSourceWrap.getParams();

            boolean mapEquals = true;
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (!entry.getValue().equals(dbParams.get(entry.getKey()))) {
                    mapEquals = false;
                    break;
                }
            }

            return mapEquals;
        }

        return false;
    }

    public boolean customEquals(Object str1, Object str2) {
        if (str1 == null && str2 == null) {
            return true;
        }

        if (str1 != null) {
            return str1.equals(str2);
        }

        return false;
    }

    @PreDestroy
    public void shutdown() {
        log.info("程序关闭，销毁数据库连接池");
        synchronized (clearFailDataSources) {
            for (Map.Entry<String, DataSourceWrap> stringDataSourceWrapEntry : clearFailDataSources.entrySet()) {
                close(stringDataSourceWrapEntry.getKey(), stringDataSourceWrapEntry.getValue());
            }

            clearFailDataSources.clear();
        }

        synchronized (dataSources) {
            for (Map.Entry<String, DataSourceWrap> stringDataSourceWrapEntry : dataSources.entrySet()) {
                close(stringDataSourceWrapEntry.getKey(), stringDataSourceWrapEntry.getValue());
            }

            dataSources.clear();
        }
    }

    /**
     * 获取数据源
     *
     * @param userId       用户id
     * @param datasourceId 数据源id
     * @return DataSourceWrap
     */
    public DataSourceWrap getDataSource(String userId, String datasourceId) {
        boolean putNull = false;
        DataSourceWrap dataSourceWrap;

        // 加锁
        synchronized (dataSources) {
            dataSourceWrap = dataSources.get(datasourceId);

            if (dataSourceWrap == null) {
                dataSourceWrap = new DataSourceWrap();
                // 先put空对象
                putNull = true;
                dataSources.put(datasourceId, dataSourceWrap);
            }

            // 标记最后使用时间
            dataSourceWrap.setLastUsedTime(System.currentTimeMillis());
        }

        // 释放锁
        if (putNull) {
            try {
                // 查询并且设置数据源信息
                settingDataSourceWrap(userId, datasourceId, dataSourceWrap, true);

            } catch (Exception e) {
                // 删除空对象，由于安全考虑进行close
                close(datasourceId, dataSources.remove(datasourceId));

                // 异常抛出去
                throw e;
            }

        } else if (dataSourceWrap.getDataSource() == null) {

            final long currentTimeMillis = System.currentTimeMillis();
            // 等待创建数据源
            while (dataSources.containsKey(datasourceId)
                    && dataSourceWrap.getDataSource() == null) {

                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // 超时抛出异常
                if ((System.currentTimeMillis() - currentTimeMillis) > maxWaitTimeout) {
                    throw new DpException(StatusCode.CODE_10010.getCode(), "获取数据源超时，数据源Id:{}" + datasourceId);
                }
            }
        }

        return dataSourceWrap;
    }

    /**
     * 查询并且设置数据源信息
     */
    private void settingDataSourceWrap(String userId, String datasourceId, DataSourceWrap dataSourceWrap, boolean createDataSource) {
        // 查询数据源配置
        DatasourceDTO datasourceDTO = getDatasourceById(userId, datasourceId);

        // 密码解密
        final String dsPassword = datasourceDTO.getDsPassword();
        if (dsPassword != null && dsPassword.startsWith(KettleTwoWayPasswordEncoder.PASSWORD_ENCRYPTED_PREFIX)) {
            datasourceDTO.setDsPassword(PASSWORD_ENCODER.decode(dsPassword));
        }

        if (createDataSource) {
            // 创建数据源
            final HikariDataSource hikariDataSource = createHikariDataSource(datasourceDTO);
            // 赋值
            dataSourceWrap.setDataSource(hikariDataSource);
        }

        dataSourceWrap.setDatabaseType(DatabaseType.valueOf(datasourceDTO.getDsType().toUpperCase()));
        dataSourceWrap.setDataSourceId(datasourceId);

        // 保存数据库信息
        final Matcher matcher = URL_PATTERN.matcher(datasourceDTO.getDsUrl());

        if (matcher.find()) {
            dataSourceWrap.setHost(matcher.group(1));
            dataSourceWrap.setPort(matcher.group(2));
            dataSourceWrap.setDb(matcher.group(3));
            dataSourceWrap.setUser(datasourceDTO.getDsUsername());
            dataSourceWrap.setPass(datasourceDTO.getDsPassword());
        } else {

            final Matcher tdMatcher = URL_TD_PATTERN.matcher(datasourceDTO.getDsUrl());
            if (tdMatcher.find()) {
                dataSourceWrap.setHost(tdMatcher.group(1));
                dataSourceWrap.setDb(tdMatcher.group(2));
                dataSourceWrap.setPort(tdMatcher.group(3));
                dataSourceWrap.setUser(datasourceDTO.getDsUsername());
                dataSourceWrap.setPass(datasourceDTO.getDsPassword());
            } else {
                throw new RuntimeException("获取数据库连接信息失败,url： " + datasourceDTO.getDsUrl());
            }
        }
    }

    /**
     * 查询数据源
     */
    public String getDatasourceIdByName(String datasourceName) {
        Result<DatasourceDTO, Object> result = datasourceServiceApiClient.getDatasourceByName(datasourceName);
        if (result == null || result.getContent() == null) {
            throw new ValidationException(StatusCode.CODE_10010.getCode(), "找不到数据源：" + datasourceName);
        }

        return result.getContent().getDatasourceId();
    }

    /**
     * 查询数据源
     */
    private DatasourceDTO getDatasourceById(String userId, String datasourceId) {
        Result<DatasourceDTO, Object> result = datasourceServiceApiClient.getDatasourceById(userId, datasourceId);
        if (result == null || result.getContent() == null) {
            throw new ValidationException(StatusCode.CODE_10010.getCode(), "找不到数据源: " + datasourceId);
        }

        return result.getContent();
    }

    /**
     * 创建Hikari连接池
     */
    private HikariDataSource createHikariDataSource(DatasourceDTO datasourceDTO) {
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setDriverClassName(datasourceDTO.getDriverClassName());
        hikariDataSource.setJdbcUrl(datasourceDTO.getDsUrl());
        hikariDataSource.setUsername(datasourceDTO.getDsUsername());
        hikariDataSource.setPassword(datasourceDTO.getDsPassword());
        hikariDataSource.setMaximumPoolSize(maxPoolSize);
        hikariDataSource.setMinimumIdle(minPoolSize);

        // 赋值数据连接池高级属性
        final String dsConectorSetting = datasourceDTO.getDsConectorSetting();
        if (StringUtils.isNotBlank(dsConectorSetting)) {
            try {
                final HashMap<String, Object> config = JsonUtils.parseObject(dsConectorSetting, HashMap.class);
                final Properties properties = new Properties();
                for (Map.Entry<String, Object> entity : config.entrySet()) {
                    if (entity.getValue() != null) {
                        properties.setProperty(entity.getKey(), String.valueOf(entity.getValue()));
                    }
                }

                final HikariConfig hikariConfig = new HikariConfig(properties);
                hikariConfig.setDriverClassName(datasourceDTO.getDriverClassName());
                hikariConfig.setJdbcUrl(datasourceDTO.getDsUrl());
                hikariConfig.setUsername(datasourceDTO.getDsUsername());
                hikariConfig.setPassword(datasourceDTO.getDsPassword());
                hikariConfig.setMinimumIdle(minPoolSize);

                if (hikariConfig.getMaximumPoolSize() <= maxPoolSize) {
                    // 控制连接池最小大小
                    hikariConfig.setMaximumPoolSize(maxPoolSize);
                }

                hikariConfig.validate();
                hikariConfig.copyStateTo(hikariDataSource);

                // 添加性能监控
                hikariDataSource.setRegisterMbeans(true);
                hikariDataSource.setPoolName(datasourceDTO.getDatasourceId());
                MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
                ObjectName poolName = new ObjectName("com.zaxxer.hikari:type=Pool (" + datasourceDTO.getDatasourceId() + ")");
                final HikariPoolMXBean hikariPoolMXBean = JMX.newMXBeanProxy(mBeanServer, poolName, HikariPoolMXBean.class);
                // 添加到map中
                datasourceMxBeans.put(datasourceDTO.getDatasourceId(), hikariPoolMXBean);

            } catch (Exception e) {
                log.error("数据源配置格式有误：{}", dsConectorSetting, e);
            }
        }

        return hikariDataSource;
    }

    /**
     * 打印数据源状态
     *
     * @param datasourceId 数据源id
     */
    private void printDatasourceStatus(String datasourceId) {
        final HikariPoolMXBean poolProxy = datasourceMxBeans.get(datasourceId);
        try {
            if (poolProxy != null) {
                log.info("HikariPoolState id=[{}] Active=[{}] Idle=[{}] Wait=[{}] Total=[{}]", datasourceId,
                        poolProxy.getActiveConnections(),
                        poolProxy.getIdleConnections(),
                        poolProxy.getThreadsAwaitingConnection(),
                        poolProxy.getTotalConnections());
            }
        } catch (Exception e) {
            log.error("数据源状态监控执行失败", e);
        }
    }

    /**
     * 关闭连接池
     *
     * @param datasourceId   数据源id
     * @param dataSourceWrap 数据源
     */
    private void close(String datasourceId, DataSourceWrap dataSourceWrap) {

        if (dataSourceWrap != null) {
            log.info("关闭连接池{}", datasourceId);
            final DataSource dataSource = dataSourceWrap.getDataSource();
            if (dataSource instanceof HikariDataSource) {
                try {
                    ((HikariDataSource) dataSource).close();
                } catch (Exception e) {
                    log.info("连接池关闭失效 {}", datasourceId, e);
                }

                try {
                    // 清理连接池监控对象
                    datasourceMxBeans.remove(datasourceId);
                } catch (Exception e) {
                    log.info("清理连接池监控对象失败 {}", datasourceId, e);
                }
            }
        }
    }

}

package com.youngdatafan.gateway.ratelimit;

import com.youngdatafan.dataintegration.core.lock.CacheLock;
import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.dataintegration.core.util.StatusCode;
import com.youngdatafan.gateway.core.util.Constans;
import com.youngdatafan.gateway.ratelimit.bean.EnvironmentEnum;
import com.youngdatafan.gateway.ratelimit.bean.GatewayRateLimitStatus;
import com.youngdatafan.gateway.ratelimit.bean.RateLimitConfig;
import com.youngdatafan.gateway.ratelimit.feign.GatewayRateLimitServiceApiClient;
import com.youngdatafan.gateway.ratelimit.util.TokenConstants;
import com.youngdatafan.portal.system.management.gateway.dto.DpGatewayRateLimitDTO;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 网关限流规则同步作业
 *
 * @author gavin
 * @since 2020/6/13 3:12 下午
 */
@Component
public class GatewayRateLimitSyncTask {
    private static final Logger log = LoggerFactory.getLogger(GatewayRateLimitSyncTask.class);

    private final DbRateLimitRepository rateLimitRepository;
    private final GatewayRateLimitServiceApiClient gatewayRouteServiceApiClient;
    private final RedisLockRegistry redisLockRegistry;

    /**
     * 保存网关规则状态
     */
    private final Map<String, GatewayRateLimitStatus> gatewayRouteStatusMap = new ConcurrentHashMap<>();

    @Autowired
    public GatewayRateLimitSyncTask(DbRateLimitRepository rateLimitRepository, GatewayRateLimitServiceApiClient gatewayRouteServiceApiClient, RedisLockRegistry redisLockRegistry) {
        this.rateLimitRepository = rateLimitRepository;
        this.gatewayRouteServiceApiClient = gatewayRouteServiceApiClient;
        this.redisLockRegistry = redisLockRegistry;
    }

    public static RateLimitConfig toRateLimitConfig(DpGatewayRateLimitDTO dpGatewayRateLimit) {
        RateLimitConfig rateLimitConfig = new RateLimitConfig();
        rateLimitConfig.setKey(dpGatewayRateLimit.getResource());

        // 超时设置
        rateLimitConfig.setTimeLimiterConfig(TimeLimiterConfig.custom()
                .timeoutDuration(Duration.ofMillis(dpGatewayRateLimit.getTimeOut() == null ? -1 : dpGatewayRateLimit.getTimeOut())).build());

        Map<String, DpRedisRateLimiter.Config> tokenConfig = new HashMap<>(1);

        DpRedisRateLimiter.Config config = new DpRedisRateLimiter.Config();
        int thresholdValue = Integer.parseInt(dpGatewayRateLimit.getThresholdValue());
        config.setReplenishRate(thresholdValue);
        config.setBurstCapacity(thresholdValue);
        config.setTimeType(1);
        config.setEnvironmentEnum(EnvironmentEnum.PROD);

        tokenConfig.put(TokenConstants.API_REQUEST_URI, config);
        rateLimitConfig.setTokenConfig(tokenConfig);
        return rateLimitConfig;
    }

    @PostConstruct
    public void init() {
        try {
            final Result<List<DpGatewayRateLimitDTO>, Object> listObjectResult = gatewayRouteServiceApiClient.selectAll();
            if (listObjectResult == null || !(listObjectResult.getCode().equals(StatusCode.CODE_10000.getCode())) ||
                    listObjectResult.getContent() == null) {
                return;
            }

            for (DpGatewayRateLimitDTO dpGatewayRateLimit : listObjectResult.getContent()) {
                try {
                    // 转换DTO并且保存规则
                    convertAndSave(dpGatewayRateLimit);
                } catch (Exception e) {
                    log.warn("init error ratelimit dto: {}", dpGatewayRateLimit, e);
                }
            }
        } catch (Exception e) {
            log.warn("初始化数据库规则失败，等待后续同步任务处理规则状态。", e);
        }
    }

    @Scheduled(initialDelay = 5000, fixedDelayString = "${dp.gateway.ratelimit.sync.fixedDelay:300000}")
    @CacheLock(key = Constans.LOCK_GATEWAY_RATELIMIT, expire = 60)
    public void syncRoute() {
        final long timeMillis = System.currentTimeMillis();

        try {
            sync();
            log.info("start syncRateLimit ,规则记录数：{} ，耗时：{}", rateLimitRepository.size()
                    , (System.currentTimeMillis() - timeMillis));
        } catch (Exception e) {
            log.error("规则同步异常", e);
        }
    }

    private void sync() {
        final Result<List<DpGatewayRateLimitDTO>, Object> listObjectResult = gatewayRouteServiceApiClient.selectAll();
        if (listObjectResult == null || !(listObjectResult.getCode().equals(StatusCode.CODE_10000.getCode())) ||
                listObjectResult.getContent() == null) {
            return;
        }

        for (DpGatewayRateLimitDTO dpGatewayRateLimitDTO : listObjectResult.getContent()) {
            try {
                syncDbRateLimit(dpGatewayRateLimitDTO);
            } catch (Exception e) {
                log.warn("save error ratelimit dto: {}", dpGatewayRateLimitDTO, e);
            }
        }

        // list to map
        final Map<String, DpGatewayRateLimitDTO> routeMap = listObjectResult.getContent().stream().collect(Collectors.toMap(DpGatewayRateLimitDTO::getResource
                , DpGatewayRateLimitDTO -> DpGatewayRateLimitDTO));

        for (RateLimitConfig config : rateLimitRepository.getRoutes()) {
            try {
                syncMemoryRateLimit(routeMap, config);
            } catch (Exception e) {
                log.warn("save error ratelimit config: {}", config, e);
            }
        }

        // 清理过期的状态
        for (Map.Entry<String, GatewayRateLimitStatus> stringGatewayRouteStatusEntry : gatewayRouteStatusMap.entrySet()) {
            if ((System.currentTimeMillis() - stringGatewayRouteStatusEntry.getValue().getLastAccessTime()) / 1000 > 300) {
                gatewayRouteStatusMap.remove(stringGatewayRouteStatusEntry.getKey());
            }
        }
    }

    private void syncMemoryRateLimit(Map<String, DpGatewayRateLimitDTO> routeMap, RateLimitConfig config) {
        final String key = config.getKey();
        if (!routeMap.containsKey(key)) {
            //数据库没有，内存规则里有，则保存状态

            final GatewayRateLimitStatus gatewayRouteStatus = saveStatus(key);
            // 上次都不一致，或者上次访问时间超过5分组，则删除规则
            if (gatewayRouteStatus.getNonExistentCount() > 3
                    || (System.currentTimeMillis() - gatewayRouteStatus.getLastAccessTime()) / 1000 > 300) {

                // 删除内存中规则
                rateLimitRepository.delete(key);
                gatewayRouteStatusMap.remove(key);
            }
        }
    }

    private void syncDbRateLimit(DpGatewayRateLimitDTO dpGatewayRateLimitDTO) {
        final String resource = dpGatewayRateLimitDTO.getResource();
        if (rateLimitRepository.containsKey(resource)) {
            //数据库规则更新，则更新内存中规则

            final RateLimitConfig rateLimitConfig = rateLimitRepository.get(resource);
            // dto转换
            final RateLimitConfig routeDefinition1 = toRateLimitConfig(dpGatewayRateLimitDTO);
            if (!configEquals(rateLimitConfig, routeDefinition1)) {
                log.info("捕获到限流规则更新：{}", routeDefinition1);
                save(routeDefinition1);
            }

        } else {
            // 保存规则
            convertAndSave(dpGatewayRateLimitDTO);
        }
    }

    /**
     * 保存状态
     */
    private GatewayRateLimitStatus saveStatus(String routeId) {
        GatewayRateLimitStatus status;
        if (gatewayRouteStatusMap.containsKey(routeId)) {
            status = gatewayRouteStatusMap.get(routeId);
            status.addNonExistentCount();
        } else {
            status = new GatewayRateLimitStatus(1);
            gatewayRouteStatusMap.put(routeId, status);
        }
        status.setLastAccessTime(System.currentTimeMillis());

        return status;
    }

    /**
     * 转换DTO并且保存规则
     *
     * @param dpGatewayRateLimit DpGatewayRateLimitDTO
     */
    private void convertAndSave(DpGatewayRateLimitDTO dpGatewayRateLimit) {
        RateLimitConfig rateLimitConfig = toRateLimitConfig(dpGatewayRateLimit);

        // 保存规则
        rateLimitRepository.save(rateLimitConfig);
    }

    /**
     * 保存规则
     */
    private void save(RateLimitConfig rateLimitConfig) {
        try {
            // 保存规则
            rateLimitRepository.save(rateLimitConfig);

        } catch (Exception e) {
            log.warn("save erro ratelimit: {}", rateLimitConfig, e);
        }
    }

    /**
     * config对象比较
     */
    private boolean configEquals(RateLimitConfig config1, RateLimitConfig config2) {
        if (config1 == null || config1.getKey() == null || config2 == null) {
            return false;
        }
        final Map<String, DpRedisRateLimiter.Config> tokenConfig2 = config2.getTokenConfig();
        if (config1.getTokenConfig() == null || tokenConfig2 == null) {
            return false;
        }

        final TimeLimiterConfig timeLimiterConfig1 = config1.getTimeLimiterConfig();
        final TimeLimiterConfig timeLimiterConfig2 = config2.getTimeLimiterConfig();
        if (!(timeLimiterConfig1.getTimeoutDuration().equals(timeLimiterConfig2.getTimeoutDuration()))
                || timeLimiterConfig1.shouldCancelRunningFuture() != timeLimiterConfig2.shouldCancelRunningFuture()) {
            return false;
        }

        // 循环比较 tokenConfig
        for (Map.Entry<String, DpRedisRateLimiter.Config> stringConfigEntry : config1.getTokenConfig().entrySet()) {
            if (!tokenConfig2.containsKey(stringConfigEntry.getKey())) {
                return false;
            }

            // config对象比较
            final DpRedisRateLimiter.Config configTmp2 = tokenConfig2.get(stringConfigEntry.getKey());
            if (!configTmp2.equals(stringConfigEntry.getValue())) {
                return false;
            }
        }

        return true;
    }

}

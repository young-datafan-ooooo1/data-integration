package com.youngdatafan.di.run.management.server.service;


import com.youngdatafan.dataintegration.core.exception.DpException;
import com.youngdatafan.dataintegration.core.exception.ValidationException;
import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.dataintegration.core.util.StatusCode;
import com.youngdatafan.dataintegration.core.util.UUIDUtils;
import com.youngdatafan.dataintegration.core.util.json.JSONLinkedObject;
import com.youngdatafan.dataintegration.core.util.json.XML;
import com.youngdatafan.di.run.management.server.bean.ProjectExecutor;
import com.youngdatafan.di.run.management.server.dto.ProjectExecutorDTO;
import com.youngdatafan.di.run.management.server.dto.ProjectExecutorStepDTO;
import com.youngdatafan.di.run.management.server.entity.DpDeProjectExecHistory;
import com.youngdatafan.di.run.management.server.mapper.DpDeProjectExecHistoryMapper;
import com.youngdatafan.di.run.management.server.trans.LogBrowser;
import com.youngdatafan.di.run.management.server.trans.TransExecutor;
import com.youngdatafan.di.run.management.server.util.ProjectExecuteEnv;
import com.youngdatafan.di.run.management.server.util.ProjectExecuteStatus;
import com.youngdatafan.di.run.management.server.vo.ProjectExecutorParam;
import com.youngdatafan.di.run.management.server.websocket.ProjectExecuteCallback;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.pentaho.di.core.exception.KettleMissingPluginsException;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.core.logging.LogLevel;
import org.pentaho.di.core.variables.Variables;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.trans.TransExecutionConfiguration;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMetaDataCombi;
import org.pentaho.di.trans.step.StepStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

/**
 * 项目同步执行实现(调度)
 *
 * @author gavin
 * @since 2020/2/13 10:53 上午
 */
@Service
public class ProjectScheduleExecutorService {

    /**
     * 实例id缓存key 前缀
     */
    public static final String INSTANCE_ID_CACHE_KEY_PREFIX = "instanceId_";

    public static final String CACHE_PREFIX = "di_re_";

    /**
     * 集成调度执行心跳map缓存key
     */
    public static final String DI_SCHEDULE_EXEC_HEARTBEAT_CACHE_KEY = "di_schedule_exec_heartbeat";

    private static final Logger logger = LoggerFactory.getLogger(ProjectScheduleExecutorService.class);

    /**
     * 存放执行器对象的map
     */
    private final ConcurrentHashMap<String, ProjectExecutor> executorMap = new ConcurrentHashMap<>();

    private final RedisTemplate<String, String> redisTemplate;

    private final DpDeProjectExecHistoryMapper projectExecHistoryMapper;

    @Value("${spring.cloud.consul.discovery.instance-id}")
    private String instanceId;

    /**
     * 心跳保持间隔时间，单位：秒
     */
    @Value("${dp.project.restExecute.heartbeatInterval:30}")
    private int heartbeatInterval;

    /**
     * 心跳保持超时时间，单位：秒
     */
    @Value("${dp.project.restExecute.heartbeatTimeout:180}")
    private int heartbeatTimeout;

    /**
     * 刷新频率，单位：毫秒
     */
    @Value("${dp.project.restExecute.refreshRateMs:800}")
    private int refreshRateMs;

    /**
     * 临时文件夹
     */
    @Value("${dp.project.restExecute.tmpFolder:./tmp_rest/}")
    private String tmpFolder;

    /**
     * 临时文件后缀
     */
    @Value("${dp.project.restExecute.tmpFileSuffix:.xml}")
    private String tmpFileSuffix;

    @Value("${dp.project.restExecute.deleteTmpFile:true}")
    private boolean deleteTmpFile;

    @Value("${kettle.engine.name:Pentaho local}")
    private String engineName;

    @Autowired
    public ProjectScheduleExecutorService(RedisTemplate<String, String> redisTemplate, DpDeProjectExecHistoryMapper projectExecHistoryMapper) {
        this.redisTemplate = redisTemplate;
        this.projectExecHistoryMapper = projectExecHistoryMapper;
    }

    /**
     * 停止执行器
     *
     * @param projectId 项目id
     */
    public boolean stop(String projectId) {
        final String cacheKey = CACHE_PREFIX + projectId;
        String executorId = redisTemplate.opsForValue().get(cacheKey);
        if (executorId == null) {
            logger.info("项目不存在，执行器ID:{}", executorId);
            return false;
        }

        // 删除缓存
        redisTemplate.delete(Arrays.asList(cacheKey, INSTANCE_ID_CACHE_KEY_PREFIX + projectId));
        // 删除redis心跳记录
        redisTemplate.boundHashOps(DI_SCHEDULE_EXEC_HEARTBEAT_CACHE_KEY).delete(projectId);

        ProjectExecutor projectExecutor = executorMap.get(executorId);
        if (projectExecutor == null) {
            logger.info("执行器不存在，执行器ID:{}", executorId);
            return false;

        } else {
            logger.info("手动停止执行器，执行器ID:{}", executorId);
            projectExecutor.getTransExecutor().stop();
            return true;
        }
    }

    /**
     * 检查项目是否在运行
     *
     * @param projectId 项目id
     */
    public void checkExists(String projectId) {
        final String cacheKey = CACHE_PREFIX + projectId;
        if (redisTemplate.opsForValue().get(cacheKey) != null) {
            // 获取项目的最后心跳时间
            final String lastHeartbeatTime = (String) redisTemplate.boundHashOps(DI_SCHEDULE_EXEC_HEARTBEAT_CACHE_KEY).get(projectId);

            if (lastHeartbeatTime != null &&
                (System.currentTimeMillis() - Long.parseLong(lastHeartbeatTime)) / 1000 >= heartbeatTimeout) {
                throw new ValidationException(StatusCode.CODE_10010, "项目正在运行中");
            }
        }
    }

    /**
     * 根据项目文件执行
     *
     * @param executorParamVO 执行参数
     * @param executeCallback 项目执行回调
     */
    public void executeByFile(StringBuilder log, String projectFile, ProjectExecutorParam executorParamVO
        , ProjectExecuteCallback executeCallback) throws Exception {
        executorParamVO.setStartTime(new Date());
        final String executorId = executorParamVO.getExecutorId();

        final String projectId = executorParamVO.getProjectId();
        final String cacheKey = CACHE_PREFIX + projectId;

        // 保存用户运行的项目，保存一天
        redisTemplate.boundValueOps(cacheKey).set(executorId, 1, TimeUnit.DAYS);
        // 记录项目的运行实例
        redisTemplate.boundValueOps(INSTANCE_ID_CACHE_KEY_PREFIX + projectId).set(instanceId, 1, TimeUnit.DAYS);
        // 保存项目启动时间到心跳map中
        redisTemplate.boundHashOps(DI_SCHEDULE_EXEC_HEARTBEAT_CACHE_KEY).put(projectId, String.valueOf(System.currentTimeMillis()));

        final long currentTimeMillis = System.currentTimeMillis();
        // 保存执行流水
        final DpDeProjectExecHistory dpDeProjectExecHistory = saveExecuteHistory(executorParamVO);
        TransExecutor executor = null;

        try {
            // 执行
            executor = execute(log, executorId, projectFile, executorParamVO, executeCallback);

        } catch (Exception e) {
            logger.error("执行发生错误", e);

            // 构建消息对象
            Result<ProjectExecutorDTO, Object> webSocketResponse = Result.success(ProjectExecutorDTO.builder()
                .executorId(executorId)
                // 获取实时日志
                .log("项目执行失败：\n" + e.getMessage()).build());

            // 回调消息
            executeCallback.onMessage("/runningState", webSocketResponse);

            // 删除map缓存，并停止执行器
            ProjectExecutor projectExecutor = executorMap.remove(executorId);
            if (projectExecutor != null && projectExecutor.getTransExecutor() != null) {
                projectExecutor.getTransExecutor().stop();
            }

            throw e;

        } finally {
            // 删除map缓存，并停止执行器
            executorMap.remove(executorId);
            logger.info("删除执行器缓存： executorId: {}", executorId);

            if (deleteTmpFile) {
                File tmpFile = new File(tmpFolder, executorId + tmpFileSuffix);
                if (tmpFile.exists()) {
                    logger.info("删除临时文件： filePath: {}, 删除状态:{}", tmpFile.getPath(), tmpFile.delete());
                }
            }

            // 删除redis缓存
            redisTemplate.delete(cacheKey);
            // 删除redis心跳记录
            redisTemplate.boundHashOps(DI_SCHEDULE_EXEC_HEARTBEAT_CACHE_KEY).delete(projectId);

            // 更新执行历史状态
            updateHistoryStatus(currentTimeMillis, dpDeProjectExecHistory, executor);
        }
    }

    private void updateHistoryStatus(long currentTimeMillis, DpDeProjectExecHistory dpDeProjectExecHistory, TransExecutor execute) {
        DpDeProjectExecHistory updateHistory = new DpDeProjectExecHistory();
        updateHistory.setId(dpDeProjectExecHistory.getId());

        if (execute != null && execute.getTrans().isStopped()) {
            updateHistory.setStatus(ProjectExecuteStatus.TERMINATIN.name());
        } else {
            updateHistory.setStatus(ProjectExecuteStatus.END.name());
        }
        updateHistory.setEndTime(new Date());
        updateHistory.setExecSecond((int) ((System.currentTimeMillis() - currentTimeMillis) / 1000));
        projectExecHistoryMapper.updateByPrimaryKeySelective(updateHistory);
    }

    private DpDeProjectExecHistory saveExecuteHistory(ProjectExecutorParam executorParamVO) {
        final DpDeProjectExecHistory dpDeProjectExecHistory = new DpDeProjectExecHistory();
        dpDeProjectExecHistory.setId(UUIDUtils.nextId());
        dpDeProjectExecHistory.setProjectId(executorParamVO.getProjectId());
        dpDeProjectExecHistory.setUserId(executorParamVO.getUserId());
        dpDeProjectExecHistory.setUserName(executorParamVO.getUserName());
        dpDeProjectExecHistory.setExecEnv(ProjectExecuteEnv.JC_YXPT.name());
        dpDeProjectExecHistory.setStatus(ProjectExecuteStatus.RUNNING.name());
        dpDeProjectExecHistory.setStartTime(new Date());
        projectExecHistoryMapper.insert(dpDeProjectExecHistory);

        return dpDeProjectExecHistory;
    }

    /**
     * 执行
     *
     * @param executorId      执行器编号
     * @param executorParamVO 执行参数
     * @param executeCallback 项目执行回调
     * @return 执行器
     */
    private TransExecutor execute(StringBuilder log, String executorId, String projectFile, ProjectExecutorParam executorParamVO
        , ProjectExecuteCallback executeCallback) throws Exception {
        // 启动转换
        TransExecutor transExecutor = start(executorId, projectFile, executorParamVO);
        // 项目id
        final String projectId = executorParamVO.getProjectId();
        // 上次心跳时间
        long lastHeartbeatTime = System.currentTimeMillis();

        ProjectExecutor projectExecutor = new ProjectExecutor(transExecutor, null);
        // 将TransExecutor添加到map中保存
        executorMap.put(executorId, projectExecutor);

        // 日志浏览器
        LogBrowser logBrowser = new LogBrowser(transExecutor);

        // 循环发送运行状态
        while (!transExecutor.isFinishedOrStopped()) {

            // 根据步骤名称，构建步骤状态
            List<ProjectExecutorStepDTO> executorSteps = buildStepStatus(projectExecutor);
            // 构建消息对象
            Result<ProjectExecutorDTO, Object> webSocketResponse = Result.success(ProjectExecutorDTO.builder()
                .executorId(executorId)
                .executorSteps(executorSteps)
                // 获取实时日志
                .log(logBrowser.getRealTimeLog()).build());

            // 回调消息
            executeCallback.onMessage("/runningState", webSocketResponse);

            try {
                TimeUnit.MILLISECONDS.sleep(refreshRateMs);
            } catch (InterruptedException e) {
                logger.error("execute InterruptedException.");
                break;
            }

            final long timeMillis = System.currentTimeMillis();
            if ((timeMillis - lastHeartbeatTime) / 1000 >= heartbeatInterval) {
                // 更新项目执行心跳时间map中
                redisTemplate.boundHashOps(DI_SCHEDULE_EXEC_HEARTBEAT_CACHE_KEY).put(projectId, String.valueOf(timeMillis));

                // 时间状态更新
                lastHeartbeatTime = timeMillis;
            }
        }

        //构建所有步骤状态
        final List<ProjectExecutorStepDTO> executorSteps = buildStepStatus(projectExecutor);
        // 构建消息对象
        Result<ProjectExecutorDTO, Object> webSocketResponse = Result.success(
            ProjectExecutorDTO.builder()
                .executorId(executorId)
                .executorSteps(executorSteps)
                .transFinished(true)
                // 获取实时日志
                .log(logBrowser.getRealTimeLog()).build());
        // 回调消息
        executeCallback.onMessage("/runningState", webSocketResponse);

        logger.info("项目执行完成，执行器id:{}", executorId);

        // 执行结果判断
        if (transExecutor.getTrans() != null && transExecutor.getTrans().getErrors() > 0) {
            logger.error("作业运行失败");
            // 作业运行失败
            throw new DpException(StatusCode.CODE_10010.getCode(), log.toString());
        }
        return transExecutor;
    }

    /**
     * 构建所有步骤状态
     *
     * @param projectExecutor ProjectExecutor
     * @return ArrayList
     */
    private List<ProjectExecutorStepDTO> buildStepStatus(ProjectExecutor projectExecutor) {
        List<StepMetaDataCombi> steps = projectExecutor.getTransExecutor().getTrans().getSteps();
        List<ProjectExecutorStepDTO> executorSteps = new ArrayList<>(steps.size());

        // 循环步骤
        for (StepMetaDataCombi stepMetaDataCombi : steps) {
            final StepInterface step = stepMetaDataCombi.step;

            // 创建步骤状态对象
            StepStatus stepStatus = new StepStatus(step);
            // 构建步骤信息
            final ProjectExecutorStepDTO executorStep = ProjectExecutorStepDTO.builder()
                .stepName(step.getStepname())
                .linesInput(stepStatus.getLinesInput())
                .linesOutput(stepStatus.getLinesOutput())
                .linesRead(stepStatus.getLinesRead())
                .linesOutput(stepStatus.getLinesOutput())
                .linesUpdated(stepStatus.getLinesUpdated())
                .linesRejected(stepStatus.getLinesRejected())
                .stepExecutionStatus(step.getStatus().name())
                .statusDescription(stepStatus.getStatusDescription())
                .seconds(stepStatus.getSeconds())
                .speed(stepStatus.getSpeed())
                .errors(stepStatus.getErrors()).build();
            executorSteps.add(executorStep);
        }

        return executorSteps;
    }

    /**
     * 生成唯一的执行id
     *
     * @return 唯一的执行id
     */
    public String generateExecutorId() {
        // uuid + 5位随机字母
        return UUIDUtils.generateUUID32() + RandomStringUtils.randomAlphabetic(5);
    }

    /**
     * 启动转换
     *
     * @param executorId      执行器id
     * @param projectFile     项目文件
     * @param executorParamVO 执行参数
     * @return TransExecutor
     */
    private TransExecutor start(String executorId, String projectFile, ProjectExecutorParam executorParamVO) throws Exception {
        // 构建TransMeta 对象
        TransMeta transMeta = buildTransMeta(executorId, projectFile);

        TransExecutionConfiguration executionConfiguration = new TransExecutionConfiguration();
        // 设置默认值以便运行配置可以正确设置
        executionConfiguration.setExecutingLocally(true);
        executionConfiguration.setExecutingRemotely(false);
        executionConfiguration.setExecutingClustered(false);
        // 日志级别
        LogLevel logLevel = LogLevel.getLogLevelForCode(executorParamVO.getLogLevel());

        // 不启用安全模式
        executionConfiguration.setSafeModeEnabled(executorParamVO.isSafeModeEnabled());
        executionConfiguration.getUsedVariables(transMeta);
        executionConfiguration.setLogLevel(logLevel);

        // 默认设置本地引擎执行
        executionConfiguration.setRunConfiguration(engineName);


        // 创建执行器
        TransExecutor transExecutor = new TransExecutor(transMeta);
        //设置命令参数
        executionConfiguration.setVariables(executorParamVO.getVariables());

        try {
            //启动转换
            transExecutor.start(executionConfiguration, executorParamVO);

        } catch (Exception e) {
            logger.error("启动作业失败", e);
            // 获取步骤日志
            throw new DpException(StatusCode.CODE_10010.getCode()
                , new LogBrowser(transExecutor).getRealTimeLog());
        }

        return transExecutor;
    }

    /**
     * 构建TransMeta 对象
     *
     * @param projectFile 项目文件
     * @return TransMeta
     */
    private TransMeta buildTransMeta(String executorId, String projectFile) throws IOException, KettleXMLException, KettleMissingPluginsException {
        Document document;
        //json转xml
        if (!projectFile.startsWith("<?xml")) {
            // json转xml
            projectFile = StringEscapeUtils.unescapeXml(projectFile);
            projectFile = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + XML.toString(new JSONLinkedObject(projectFile));

            logger.debug("json转换成xml，转换后的xml:{}", projectFile);
        }

        // 写到临时目录
        File outFile = new File(tmpFolder, executorId + tmpFileSuffix);
        FileUtils.writeStringToFile(outFile, projectFile);

        // 加载xml
        document = XMLHandler.loadXMLString(projectFile);

        TransMeta transMeta = new TransMeta();
        transMeta.loadXML(
            document.getDocumentElement(), outFile.getPath(), null, null, true, new Variables(),
            (message, rememberText, rememberPropertyName) -> {
                // Yes means: overwrite
                return true;
            });

        if (transMeta.hasMissingPlugins()) {
            logger.info("【{}】缺少执行插件。", projectFile);
        }

        return transMeta;
    }

}

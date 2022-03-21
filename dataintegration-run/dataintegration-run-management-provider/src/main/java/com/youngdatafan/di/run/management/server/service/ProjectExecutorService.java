package com.youngdatafan.di.run.management.server.service;

import com.youngdatafan.dataintegration.core.exception.ValidationException;
import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.dataintegration.core.util.JsonUtils;
import com.youngdatafan.dataintegration.core.util.StatusCode;
import com.youngdatafan.dataintegration.core.util.UUIDUtils;
import com.youngdatafan.dataintegration.core.util.json.JSONLinkedObject;
import com.youngdatafan.dataintegration.core.util.json.XML;
import com.youngdatafan.di.run.management.server.bean.ProjectExecutor;
import com.youngdatafan.di.run.management.server.dto.ProjectExecutorDTO;
import com.youngdatafan.di.run.management.server.dto.ProjectExecutorStepDTO;
import com.youngdatafan.di.run.management.server.dto.ProjectHistoryExecuteDTO;
import com.youngdatafan.di.run.management.server.entity.DpDeProjectExecHistory;
import com.youngdatafan.di.run.management.server.mapper.DpDeProjectExecHistoryMapper;
import com.youngdatafan.di.run.management.server.trans.LogBrowser;
import com.youngdatafan.di.run.management.server.trans.TransExecutor;
import com.youngdatafan.di.run.management.server.trans.TransPreview;
import com.youngdatafan.di.run.management.server.util.ProjectExecuteEnv;
import com.youngdatafan.di.run.management.server.util.ProjectExecuteStatus;
import com.youngdatafan.di.run.management.server.vo.ProjectExecutorParam;
import com.youngdatafan.di.run.management.server.vo.ProjectHistoryExecuteVO;
import com.youngdatafan.di.run.management.server.vo.ProjectStopVO;
import com.youngdatafan.di.run.management.server.websocket.ProjectExecuteCallback;
import com.youngdatafan.di.run.management.util.ProjectExecuteException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.pentaho.di.trans.step.BaseStepData;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMetaDataCombi;
import org.pentaho.di.trans.step.StepStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

/**
 * 项目异步执行实现
 *
 * @author gavin
 * @since 2020/2/13 10:53 上午
 */
@Service
public class ProjectExecutorService {

    public static final String CACHE_PREFIX = "di_executor_";
    public static final String INSTANCEID = "instanceId";
    private static final Logger logger = LoggerFactory.getLogger(ProjectExecutorService.class);
    /**
     * 存放执行器对象的map
     */
    private final ConcurrentHashMap<String, ProjectExecutor> executorMap = new ConcurrentHashMap<>();
    private final RedisTemplate<String, String> redisTemplate;
    private final DpDeProjectExecHistoryMapper projectExecHistoryMapper;


    @Value("${spring.cloud.consul.discovery.instance-id}")
    private String instanceId;
    /**
     * 刷新频率，单位：毫秒
     */
    @Value("${dp.project.execute.refreshRateMs:800}")
    private int refreshRateMs;
    /**
     * 临时文件夹
     */
    @Value("${dp.project.execute.tmpFolder:./tmp/}")
    private String tmpFolder;
    /**
     * 临时文件后缀
     */
    @Value("${dp.project.execute.tmpFileSuffix:.xml}")
    private String tmpFileSuffix;
    @Value("${dp.project.execute.deleteTmpFile:true}")
    private boolean deleteTmpFile;
    @Value("${kettle.engine.name:Pentaho local}")
    private String engineName;

    @Autowired
    public ProjectExecutorService(RedisTemplate<String, String> redisTemplate, DpDeProjectExecHistoryMapper projectExecHistoryMapper) {
        this.redisTemplate = redisTemplate;
        this.projectExecHistoryMapper = projectExecHistoryMapper;
    }

    /**
     * 暂停或者回复执行器
     *
     * @param executorId 执行器id
     * @return 返回是否处于暂停中状态
     */
    public boolean pauseResume(String executorId) {
        //TODO 要处理暂停的最大超时时间
        ProjectExecutor projectExecutor = executorMap.get(executorId);
        if (projectExecutor != null) {
            TransExecutor transExecutor = projectExecutor.getTransExecutor();
            transExecutor.pauseResume();

            boolean pausing = transExecutor.isPausing();
            logger.info("暂停或者恢复执行器，执行器ID:{},isPausing:{}", executorId, pausing);

            return pausing;
        }

        return false;
    }

    /**
     * 停止执行器
     *
     * @param executorId 执行器id
     */
    public boolean stop(String executorId) {
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
     * @param userId    用户id
     * @param projectId 项目id
     */
    public void checkExists(String userId, String projectId) {
        final String cacheKey = CACHE_PREFIX + userId;
        if (redisTemplate.opsForHash().hasKey(cacheKey, projectId)) {
            throw new ValidationException(StatusCode.CODE_10010, "项目正在运行中");
        }
    }

    /**
     * 停止并且删除缓存
     *
     * @param userId        用户id
     * @param projectStopVO ProjectStopVO
     */
    public boolean stop(String userId, ProjectStopVO projectStopVO) {
        final String cacheKey = CACHE_PREFIX + userId;
        redisTemplate.opsForHash().delete(cacheKey, projectStopVO.getProjectId());

        // 停止执行
        return stop(projectStopVO.getExecutorId());
    }

    /**
     * 异步执行
     *
     * @param executorParamVO 执行参数
     * @param executeCallback 项目执行回调
     */
    @Async("asyncTaskExecutor")
    public void asyncExecuteByFile(String projectFile, ProjectExecutorParam executorParamVO
            , ProjectExecuteCallback executeCallback) {
        // 执行
        executeByFile(projectFile, executorParamVO, executeCallback);
    }

    /**
     * 根据项目文件执行
     *
     * @param executorParamVO 执行参数
     * @param executeCallback 项目执行回调
     */
    public void executeByFile(String projectFile, ProjectExecutorParam executorParamVO
            , ProjectExecuteCallback executeCallback) {
        executorParamVO.setStartTime(new Date());
        final String executorId = executorParamVO.getExecutorId();

        // 保存用户运行的项目状态
        final String cacheKey = saveUserExecuteProject(executorParamVO);

        final long currentTimeMillis = System.currentTimeMillis();
        // 保存执行流水
        final DpDeProjectExecHistory dpDeProjectExecHistory = saveExecuteHistory(executorParamVO);
        TransExecutor executor = null;

        try {
            // 执行
            executor = execute(executorId, projectFile, executorParamVO, executeCallback);

        } catch (ProjectExecuteException e) {
            // 响应错误信息
            final TransExecutor transExecutor = e.getTransExecutor();
            final List<ProjectExecutorStepDTO> stepDTOS = buildStepStatus(transExecutor);
            responseError(executorParamVO, executeCallback, executorId
                    , new LogBrowser(transExecutor).getRealTimeLog()
                    , stepDTOS);

        } catch (Exception e) {
            logger.error("执行发生错误", e);
            responseError(executorParamVO, executeCallback, executorId, "项目执行失败：\n" + e.getMessage(), null);

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
            redisTemplate.opsForHash().delete(cacheKey, executorParamVO.getProjectId());

            // 更新执行历史状态
            updateHistoryStatus(currentTimeMillis, dpDeProjectExecHistory, executor);
        }
    }

    private void responseError(ProjectExecutorParam executorParamVO, ProjectExecuteCallback executeCallback, String executorId
            , String log, List<ProjectExecutorStepDTO> stepDTOS) {
        // 构建消息对象
        Result webSocketResponse = Result.fail(StatusCode.CODE_10010.getCode()
                , ProjectExecutorDTO.builder()
                        .requestId(executorParamVO.getRequestId())
                        .executorId(executorId)
                        .transFinished(true)
                        .executorSteps(stepDTOS)
                        // 获取实时日志
                        .log(log).build(), "");

        // 回调消息
        executeCallback.onMessage("/runningState", webSocketResponse);

        // 删除map缓存，并停止执行器
        ProjectExecutor projectExecutor = executorMap.remove(executorId);
        if (projectExecutor != null && projectExecutor.getTransExecutor() != null) {
            projectExecutor.getTransExecutor().stop();
        }
    }

    private String saveUserExecuteProject(ProjectExecutorParam executorParamVO) {
        final String userId = executorParamVO.getUserId();
        final String cacheKey = CACHE_PREFIX + userId;

        // 保存用户运行的项目
        final HashMap<Object, Object> executeProjectCacheMap = new HashMap<>(2);
        executeProjectCacheMap.put(executorParamVO.getProjectId(), JsonUtils.toString(executorParamVO));
        executeProjectCacheMap.put(INSTANCEID, instanceId);
        redisTemplate.boundHashOps(cacheKey).putAll(executeProjectCacheMap);

        // 保存一天
        redisTemplate.expire(cacheKey, 1, TimeUnit.DAYS);

        return cacheKey;
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
        dpDeProjectExecHistory.setExecEnv(ProjectExecuteEnv.JCPT.name());
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
    private TransExecutor execute(String executorId, String projectFile, ProjectExecutorParam executorParamVO
            , ProjectExecuteCallback executeCallback) throws Exception {
        // 启动转换
        TransExecutor transExecutor = start(executorId, projectFile, executorParamVO);

        ProjectExecutor projectExecutor = new ProjectExecutor(transExecutor, null);
        // 将TransExecutor添加到map中保存
        executorMap.put(executorId, projectExecutor);

        // 日志浏览器
        LogBrowser logBrowser = new LogBrowser(transExecutor);

        // 循环发送运行状态
        while (!transExecutor.isFinishedOrStopped()) {

            // 根据步骤名称，构建步骤状态
            List<ProjectExecutorStepDTO> executorSteps = buildStepStatus(transExecutor);
            // 构建消息对象
            Result<ProjectExecutorDTO, Object> webSocketResponse = Result.success(ProjectExecutorDTO.builder()
                    .executorId(executorId)
                    .requestId(executorParamVO.getRequestId())
                    .executorSteps(executorSteps)
                    .errors(transExecutor.getTrans().getErrors())
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
        }

        //构建所有步骤状态
        final List<ProjectExecutorStepDTO> executorSteps = buildStepStatus(transExecutor);
        // 构建消息对象
        Result<ProjectExecutorDTO, Object> webSocketResponse = Result.success(
                ProjectExecutorDTO.builder()
                        .executorId(executorId)
                        .requestId(executorParamVO.getRequestId())
                        .executorSteps(executorSteps)
                        .errors(transExecutor.getTrans().getErrors())
                        .transFinished(true)
                        // 获取实时日志
                        .log(logBrowser.getRealTimeLog()).build());
        // 回调消息
        executeCallback.onMessage("/runningState", webSocketResponse);

        logger.info("项目执行完成，执行器id:{}", executorId);

        return transExecutor;
    }

    /**
     * 构建所有步骤状态
     *
     * @param transExecutor TransExecutor
     * @return ArrayList
     */
    private List<ProjectExecutorStepDTO> buildStepStatus(TransExecutor transExecutor) {
        if (transExecutor == null || transExecutor.getTrans() == null) {
            return null;
        }

        final List<StepMetaDataCombi> steps = transExecutor.getTrans().getSteps();
        final TransPreview transPreview = transExecutor.getTransPreview();

        List<ProjectExecutorStepDTO> executorSteps = new ArrayList<>(steps.size());

        // 循环步骤
        for (StepMetaDataCombi stepMetaDataCombi : steps) {
            final StepInterface step = stepMetaDataCombi.step;

            // 创建步骤状态对象
            StepStatus stepStatus = new StepStatus(step);
            // 构建步骤信息
            final String stepname = step.getStepname();

            // 处理预览数据
            List<String[]> preViewData = null;
            List<String> previewFieldNames = null;
            if (step.getStatus() == BaseStepData.StepExecutionStatus.STATUS_FINISHED) {
                preViewData = transPreview.getData(stepname);
                previewFieldNames = transPreview.getFieldNames(stepname);
                // 清理掉预览数据
                transPreview.remove(stepname);
            }

            final ProjectExecutorStepDTO executorStep = ProjectExecutorStepDTO.builder()
                    .stepName(stepname)
                    .copy(stepStatus.getCopy())
                    .priority(stepStatus.getPriority())
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
                    .previewRows(preViewData)
                    .previewFieldNames(previewFieldNames)
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
    public String generateExecutorId(String projectId) {
        // projectId + 12位随机字母
        return projectId + "_" + RandomStringUtils.randomAlphabetic(12);
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

        //设置命令参数
        executionConfiguration.setVariables(executorParamVO.getVariables());

        // 创建执行器
        TransExecutor transExecutor = new TransExecutor(transMeta);

        try {
            //启动转换
            transExecutor.start(executionConfiguration,executorParamVO);

        } catch (Exception e) {
            logger.error("启动作业失败", e);
            // 获取步骤日志
            throw new ProjectExecuteException(transExecutor);
        }

        return transExecutor;
    }

    /**
     * 构建TransMeta 对象
     *
     * @param projectFile 项目文件
     * @return TransMeta
     */
    public TransMeta buildTransMeta(String executorId, String projectFile) throws IOException, KettleXMLException, KettleMissingPluginsException {
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

    public boolean executorIsExists(String executorId) {
        return executorMap.containsKey(executorId);
    }

    /**
     * 查询正在运行的项目
     *
     * @param userId 用户id
     * @return list
     */
    public List<ProjectHistoryExecuteDTO> selectRunningProject(String userId) {
        final String cacheKey = CACHE_PREFIX + userId;

        final Map<Object, Object> entries = redisTemplate.opsForHash().entries(cacheKey);
        entries.remove(INSTANCEID);

        List<ProjectHistoryExecuteDTO> result = new ArrayList<>();
        for (Object value : entries.values()) {
            try {
                final ProjectExecutorParam projectExecutorParam = JsonUtils.parseObject((String) value, ProjectExecutorParam.class);
                final ProjectHistoryExecuteDTO projectHistoryExecuteDTO = new ProjectHistoryExecuteDTO();
                BeanUtils.copyProperties(projectExecutorParam, projectHistoryExecuteDTO);
                result.add(projectHistoryExecuteDTO);
            } catch (IOException e) {
                logger.error("解析错误，json: {}", value);
            }
        }

        // 时间降序
        result.sort((o1, o2) -> o2.getStartTime().compareTo(o1.getStartTime()));

        return result;
    }

    /**
     * 查询历史执行
     *
     * @param projectHistoryExecuteVO ProjectHistoryExecuteVO
     * @return list
     */
    public List<ProjectHistoryExecuteDTO> selectUserHistoryExecute(String userId, ProjectHistoryExecuteVO projectHistoryExecuteVO) {
        return projectExecHistoryMapper.selectUserHistoryExecute(userId, projectHistoryExecuteVO.getProjectName()
                , projectHistoryExecuteVO.getStartTime(), projectHistoryExecuteVO.getEndTime());
    }

}

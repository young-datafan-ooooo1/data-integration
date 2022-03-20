package com.youngdatafan.di.run.management.server.controller;

import com.youngdatafan.dataintegration.core.exception.ValidationException;
import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.dataintegration.core.util.JsonUtils;
import com.youngdatafan.dataintegration.core.util.StatusCode;
import com.youngdatafan.di.run.management.server.dto.ProjectExecutorDTO;
import com.youngdatafan.di.run.management.server.feign.ProjectServiceApiClient;
import com.youngdatafan.di.run.management.server.service.ProjectScheduleExecutorService;
import com.youngdatafan.di.run.management.server.vo.ProjectExecutorParam;
import com.youngdatafan.di.run.management.server.vo.ProjectFileExecutorVO;
import com.youngdatafan.di.run.management.server.vo.ProjectIdExecutorVO;
import com.youngdatafan.portal.common.project.dto.ProjectFileDTO;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * reset接口执行
 *
 * @author gavin
 * @since 2020/2/13 10:27 上午
 */
@RestController
@RequestMapping("/scheduleExecute")
public class ProjectScheduleExecutorController {

    private static final Logger logger = LoggerFactory.getLogger(ProjectScheduleExecutorController.class);

    private final ProjectScheduleExecutorService projectScheduleExecutorService;
    private final ProjectServiceApiClient projectServiceApiClient;

    @Value("${dp.exec.maxLogLength:65536}")
    private int maxLogLength;

    @Autowired
    public ProjectScheduleExecutorController(ProjectScheduleExecutorService projectScheduleExecutorService, ProjectServiceApiClient projectServiceApiClient) {
        this.projectScheduleExecutorService = projectScheduleExecutorService;
        this.projectServiceApiClient = projectServiceApiClient;
    }

    @PostMapping("/executeById")
    public Result<String, Object> executeById(@Validated ProjectIdExecutorVO projectIdExecutorVO) throws Exception {
        String userId = "schedule";
        String userName = "schedule";

        // 生成唯一的执行器id
        String executorId = projectScheduleExecutorService.generateExecutorId();

        Result<ProjectFileDTO, Object> projectResult = projectServiceApiClient.getProjectFile(projectIdExecutorVO.getProjectId());
        if (!StatusCode.CODE_10000.equals(projectResult.getCode())) {
            // 打印日志
            logger.warn(JsonUtils.toString(projectResult));
            // 抛出异常
            throw new ValidationException(StatusCode.CODE_10010.getCode(), "获取项目文件失败.");
        }

        // 判断项目文件是否存在
        final ProjectFileDTO projectFile = projectResult.getContent();
        if (projectFile == null || StringUtils.isBlank(projectFile.getProjectFile())) {
            throw new ValidationException(StatusCode.CODE_10010.getCode(), "项目文件不存在或者没有执行权限。");
        }

        // 创建执行参数对象
        ProjectExecutorParam projectExecutorParam = new ProjectExecutorParam();
        BeanUtils.copyProperties(projectIdExecutorVO, projectExecutorParam);
        projectExecutorParam.setExecutorId(executorId);
        projectExecutorParam.setUserId(userId);
        projectExecutorParam.setUserName(userName);

        // 检查项目是否在运行
        projectScheduleExecutorService.checkExists(projectExecutorParam.getProjectId());

        final StringBuilder log = new StringBuilder();
        // 异步执行
        projectScheduleExecutorService.executeByFile(log, projectFile.getProjectFile(), projectExecutorParam
                , (destination, webSocketMessage) -> {
                    // 打印日志
                    logger.info("executorId: {} ,message: {}", executorId, JsonUtils.toString(webSocketMessage));

                    final ProjectExecutorDTO content = (ProjectExecutorDTO) webSocketMessage.getContent();
                    // 保存日志
                    if (log.length() < maxLogLength && content != null) {
                        log.append(content.getLog());
                    }
                });

        // 将执行器id返回
        return Result.success(executorId, log.toString());
    }

    @PostMapping("/executeByFile")
    public Result<String, Object> executeByFile(@Validated ProjectFileExecutorVO projectFileExecutorVO) throws Exception {
        String userId = "schedule";
        String userName = "schedule";

        // 生成唯一的执行器id
        String executorId = projectScheduleExecutorService.generateExecutorId();

        // 创建执行参数对象
        ProjectExecutorParam projectExecutorParam = new ProjectExecutorParam();
        BeanUtils.copyProperties(projectFileExecutorVO, projectExecutorParam);
        projectExecutorParam.setExecutorId(executorId);
        projectExecutorParam.setUserId(userId);
        projectExecutorParam.setUserName(userName);

        // 检查项目是否在运行
        projectScheduleExecutorService.checkExists(projectExecutorParam.getProjectId());

        final StringBuilder log = new StringBuilder();
        // 异步执行
        projectScheduleExecutorService.executeByFile(log, projectFileExecutorVO.getProjectFile(), projectExecutorParam
                , (destination, webSocketMessage) -> {
                    // 打印日志
                    logger.info("executorId: {} ,message: {}", executorId, JsonUtils.toString(webSocketMessage));

                    final ProjectExecutorDTO content = (ProjectExecutorDTO) webSocketMessage.getContent();
                    // 保存日志
                    if (log.length() < maxLogLength && content != null) {
                        log.append(content.getLog());
                    }
                });
        // 将执行器id返回
        return Result.success(executorId, log.toString());
    }

    @GetMapping("/stop")
    public Result<Boolean, Object> stop(@RequestParam("projectId") String projectId) {
        // 停止
        boolean stop = projectScheduleExecutorService.stop(projectId);
        // 返回
        return Result.success(stop);
    }

}

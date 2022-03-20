package com.youngdatafan.di.run.management.server.controller;

import com.youngdatafan.dataintegration.core.exception.ValidationException;
import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.dataintegration.core.util.JsonUtils;
import com.youngdatafan.dataintegration.core.util.StatusCode;
import com.youngdatafan.di.run.management.server.feign.ProjectServiceApiClient;
import com.youngdatafan.di.run.management.server.service.ProjectExecutorService;
import com.youngdatafan.di.run.management.server.vo.ProjectExecutorParam;
import com.youngdatafan.di.run.management.server.vo.ProjectFileExecutorVO;
import com.youngdatafan.di.run.management.server.vo.ProjectIdExecutorVO;
import com.youngdatafan.di.run.management.server.vo.ProjectStopVO;
import com.youngdatafan.di.run.management.server.websocket.DePrincipal;
import com.youngdatafan.portal.common.project.dto.ProjectFileDTO;
import java.security.Principal;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

// import com.dp.portal.common.project.dto.ProjectFileDTO;

/**
 * @author gavin
 * @since 2020/2/13 10:27 上午
 */
@Controller
public class ProjectExecutorController {

    private static final Logger logger = LoggerFactory.getLogger(ProjectExecutorController.class);

    private final SimpMessagingTemplate template;
    private final ProjectExecutorService projectExecutorService;
    private final ProjectServiceApiClient projectServiceApiClient;

    @Autowired
    public ProjectExecutorController(SimpMessagingTemplate template, ProjectExecutorService projectExecutorService, ProjectServiceApiClient projectServiceApiClient) {
        this.template = template;
        this.projectExecutorService = projectExecutorService;
        this.projectServiceApiClient = projectServiceApiClient;
    }

    @MessageMapping("/executeById")
    @SendToUser("/executeById")
    public Result<String, Object> executeById(Principal principal, @Validated ProjectIdExecutorVO projectIdExecutorVO) {
        DePrincipal dePrincipal = (DePrincipal) principal;
        String userId = dePrincipal.getName();
        String userName = dePrincipal.getUserName();

        // 生成唯一的执行器id
        String executorId = projectExecutorService.generateExecutorId(projectIdExecutorVO.getProjectId());

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
        projectExecutorService.checkExists(userId, projectExecutorParam.getProjectId());
        // 异步执行
        projectExecutorService.asyncExecuteByFile(projectFile.getProjectFile(), projectExecutorParam
                , (destination, webSocketMessage) -> {
                    // 发送消息给前端
                    template.convertAndSendToUser(userId, destination, webSocketMessage);
                });

        // 将执行器id返回
        return Result.success(executorId);
    }

    @MessageMapping("/executeByFile")
    @SendToUser("/executeByFile")
    public Result<String, Object> executeByFile(Principal principal, @Validated ProjectFileExecutorVO projectFileExecutorVO) {
        DePrincipal dePrincipal = (DePrincipal) principal;
        String userId = dePrincipal.getName();
        String userName = dePrincipal.getUserName();

        // 生成唯一的执行器id
        String executorId = projectExecutorService.generateExecutorId(projectFileExecutorVO.getProjectId());

        // 创建执行参数对象
        ProjectExecutorParam projectExecutorParam = new ProjectExecutorParam();
        BeanUtils.copyProperties(projectFileExecutorVO, projectExecutorParam);
        projectExecutorParam.setExecutorId(executorId);
        projectExecutorParam.setUserId(userId);
        projectExecutorParam.setUserName(userName);

        // 检查项目是否在运行
        projectExecutorService.checkExists(userId, projectExecutorParam.getProjectId());
        // 异步执行
        projectExecutorService.asyncExecuteByFile(projectFileExecutorVO.getProjectFile(), projectExecutorParam
                , (destination, webSocketMessage) -> {
                    // 发送消息给前端
                    template.convertAndSendToUser(userId, destination, webSocketMessage);
                });
        // 将执行器id返回
        return Result.success(executorId);
    }

    @MessageMapping("/stop")
    @SendToUser("/stop")
    public Result<Boolean, Object> stop(Principal principal, @Validated ProjectStopVO projectStopVO) {
        // 停止
        boolean stop = projectExecutorService.stop(principal.getName(), projectStopVO);
        // 返回
        return Result.success(stop);
    }

    @MessageMapping("/pauseResume")
    @SendToUser("/pauseResume")
    public Result<Boolean, Object> pauseResume(String executorId) {
        boolean pause = projectExecutorService.pauseResume(executorId);

        // 将执行器id返回
        return Result.success(pause);
    }

    @MessageMapping("/executorIsExists")
    @SendToUser("/executorIsExists")
    public Result<Boolean, Object> executorIsExists(String executorId) {
        boolean exists = projectExecutorService.executorIsExists(executorId);

        // 将执行器id返回
        return Result.success(exists);
    }

}

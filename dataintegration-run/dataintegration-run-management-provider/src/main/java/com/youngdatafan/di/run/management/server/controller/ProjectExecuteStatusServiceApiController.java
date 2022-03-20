package com.youngdatafan.di.run.management.server.controller;

import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.di.run.management.server.api.ProjectExecuteStatusServiceApi;
import com.youngdatafan.di.run.management.server.dto.ProjectHistoryExecuteDTO;
import com.youngdatafan.di.run.management.server.service.ProjectExecutorService;
import com.youngdatafan.di.run.management.server.vo.ProjectHistoryExecuteVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author gavin
 * @since 2020/2/13 10:27 上午
 */
@RestController
@RequestMapping("/status")
public class ProjectExecuteStatusServiceApiController implements ProjectExecuteStatusServiceApi {

    private final ProjectExecutorService projectExecutorService;

    @Autowired
    public ProjectExecuteStatusServiceApiController(ProjectExecutorService projectExecutorService) {
        this.projectExecutorService = projectExecutorService;
    }

    @Override
    public Result<List<ProjectHistoryExecuteDTO>, Object> getRunningProject(String userId) {
        final List<ProjectHistoryExecuteDTO> projectHistoryExecute = projectExecutorService.selectRunningProject(userId);
        return Result.success(projectHistoryExecute);
    }

    @Override
    public Result<PageInfo<ProjectHistoryExecuteDTO>, Object> selectUserHistoryExecute(String userId, ProjectHistoryExecuteVO projectHistoryExecuteVO
            , int pageNum, int pageSize) {
        // 设置分页规则
        PageHelper.startPage(pageNum, pageSize);

        PageInfo<ProjectHistoryExecuteDTO> pageInfo = new PageInfo<>(projectExecutorService.selectUserHistoryExecute(userId,projectHistoryExecuteVO));
        return Result.success(pageInfo);
    }
}

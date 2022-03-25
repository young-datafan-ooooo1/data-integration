package com.youngdatafan.di.run.management.server.controller;

import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.di.run.management.server.api.ProjectPreviewServiceApi;
import com.youngdatafan.di.run.management.server.dto.ProjectPreviewExecutorDTO;
import com.youngdatafan.di.run.management.server.service.ProjectPreviewExecutorService;
import com.youngdatafan.di.run.management.server.vo.ProjectFilePreviewExecutorVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * previewExecutor.
 *
 * @author gavin
 * @since 2020/2/13 10:27 上午
 */
@RestController
@RequestMapping("/previewExecutor")
public class ProjectPreviewExecutorController implements ProjectPreviewServiceApi {

    private final ProjectPreviewExecutorService previewExecutorService;

    @Autowired
    public ProjectPreviewExecutorController(ProjectPreviewExecutorService previewExecutorService) {
        this.previewExecutorService = previewExecutorService;
    }

    @Override
    public Result<ProjectPreviewExecutorDTO, Object> executeByFile(ProjectFilePreviewExecutorVO previewExecutorVO) throws Exception {
        return Result.success(previewExecutorService.executeByFile(previewExecutorVO));
    }
}
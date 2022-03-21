package com.youngdatafan.portal.common.project.service;

import com.youngdatafan.portal.common.project.entity.DpPortalProject;
import com.youngdatafan.portal.common.project.entity.DpPortalProjectFile;
import com.youngdatafan.portal.common.project.mapper.DpPortalProjectFileMapper;
import com.youngdatafan.portal.common.project.vo.ProjectAddVO;
import com.youngdatafan.portal.common.project.vo.ProjectFileVO;
import com.youngdatafan.portal.common.project.vo.ProjectUpdateVO;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * AsyncProjectService.
 *
 * @author gavin
 * @since 2020/2/12 11:03 上午
 */
@Slf4j
@Service
public class AsyncProjectService {

    private final DpPortalProjectFileMapper projectFileMapper;

    @Autowired
    public AsyncProjectService(DpPortalProjectFileMapper projectFileMapper) {
        this.projectFileMapper = projectFileMapper;
    }

    /**
     * asyncInsertProjectFile.
     *
     * @param projectAddVO 项目信息
     * @param project      DpPortalProject
     */
    @Async("asyncTaskExecutor")
    public void asyncInsertProjectFile(ProjectAddVO projectAddVO, DpPortalProject project) {
        ProjectFileVO projectFileVO = projectAddVO.getProjectFileVO();
        if (projectFileVO != null) {
            DpPortalProjectFile dpPortalProjectFile = new DpPortalProjectFile();
            BeanUtils.copyProperties(projectFileVO, dpPortalProjectFile);

            dpPortalProjectFile.setProjectId(project.getProjectId());
            dpPortalProjectFile.setCreateTime(new Date());

            projectFileMapper.insert(dpPortalProjectFile);
        }
    }

    /**
     * asyncTaskExecutor.
     *
     * @param projectUpdateVO projectUpdateVO
     */
    @Async("asyncTaskExecutor")
    public void asyncUpdateProjectFile(ProjectUpdateVO projectUpdateVO) {
        ProjectFileVO projectFileVO = projectUpdateVO.getProjectFileVO();

        // 版本号+1
        //projectFileVO.setProjectVersion(projectFileVO.getProjectVersion() + 1);
        DpPortalProjectFile dpPortalProjectFile = new DpPortalProjectFile();
        BeanUtils.copyProperties(projectFileVO, dpPortalProjectFile);

        dpPortalProjectFile.setProjectId(projectUpdateVO.getProjectId());

        projectFileMapper.updateByPrimaryKeySelective(dpPortalProjectFile);
    }

}

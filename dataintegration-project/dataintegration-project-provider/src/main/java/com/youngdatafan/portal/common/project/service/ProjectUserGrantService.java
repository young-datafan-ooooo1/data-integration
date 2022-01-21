package com.youngdatafan.portal.common.project.service;

import com.youngdatafan.portal.common.project.dto.ProjectUserGrantDTO;
import com.youngdatafan.portal.common.project.dto.UserInfoDTO;
import com.youngdatafan.portal.common.project.mapper.DpPortalProjectUserGrantMapper;
import com.youngdatafan.portal.common.project.vo.ProjectUserGrantInsertVO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author gavin
 * @since 2020/2/12 1:07 下午
 */
@Service
public class ProjectUserGrantService {

    private final DpPortalProjectUserGrantMapper portalProjectUserGrantMapper;

    @Autowired
    public ProjectUserGrantService(DpPortalProjectUserGrantMapper portalProjectUserGrantMapper) {
        this.portalProjectUserGrantMapper = portalProjectUserGrantMapper;
    }

    public List<ProjectUserGrantDTO> selectUserGrant(String projectId) {
        return portalProjectUserGrantMapper.selectUserGrant(projectId);
    }

    public void deleteByProjectIds(String[] projectIds) {
        portalProjectUserGrantMapper.deleteByProjectIds(projectIds);
    }

    public void deleteByProjectId(String projectId) {
        portalProjectUserGrantMapper.deleteByProjectId(projectId);
    }

    public void reGrant(@Validated @RequestBody ProjectUserGrantInsertVO projectUserGrantInsertVO) {
        portalProjectUserGrantMapper.deleteByProjectId(projectUserGrantInsertVO.getProjectId());

        if (CollectionUtils.isNotEmpty(projectUserGrantInsertVO.getProjectUserGrants())) {
            portalProjectUserGrantMapper.insertGrant(projectUserGrantInsertVO.getProjectId(), projectUserGrantInsertVO.getProjectUserGrants());
        }
    }

    public List<UserInfoDTO> selectNotGrantedUser(String userId, String projectId, String describe) {
        return portalProjectUserGrantMapper.selectNotGrantedUser(projectId, userId,describe);
    }
}

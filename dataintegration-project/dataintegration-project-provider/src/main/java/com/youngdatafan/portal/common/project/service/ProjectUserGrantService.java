package com.youngdatafan.portal.common.project.service;

import com.youngdatafan.portal.common.project.dto.ProjectUserGrantDTO;
import com.youngdatafan.portal.common.project.dto.UserInfoDTO;
import com.youngdatafan.portal.common.project.mapper.DpPortalProjectUserGrantMapper;
import com.youngdatafan.portal.common.project.vo.ProjectUserGrantInsertVO;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * ProjectUserGrantService.
 *
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

    /**
     * 查询用户授权.
     * @param projectId 项目id
     * @return list
     */
    public List<ProjectUserGrantDTO> selectUserGrant(String projectId) {
        return portalProjectUserGrantMapper.selectUserGrant(projectId);
    }

    /**
     * 删除项目id.
     * @param projectIds 项目ids
     */
    public void deleteByProjectIds(String[] projectIds) {
        portalProjectUserGrantMapper.deleteByProjectIds(projectIds);
    }

    /**
     * 删除项目id.
     * @param projectId 项目id
     */
    public void deleteByProjectId(String projectId) {
        portalProjectUserGrantMapper.deleteByProjectId(projectId);
    }

    /**
     * 授权.
     * @param projectUserGrantInsertVO projectUserGrantInsertVO
     */
    public void reGrant(@Validated @RequestBody ProjectUserGrantInsertVO projectUserGrantInsertVO) {
        portalProjectUserGrantMapper.deleteByProjectId(projectUserGrantInsertVO.getProjectId());

        if (CollectionUtils.isNotEmpty(projectUserGrantInsertVO.getProjectUserGrants())) {
            portalProjectUserGrantMapper.insertGrant(projectUserGrantInsertVO.getProjectId(), projectUserGrantInsertVO.getProjectUserGrants());
        }
    }

    /**
     * 查询为授权.
     * @param userId 用户id
     * @param projectId 项目id
     * @param describe 描述
     * @return list
     */
    public List<UserInfoDTO> selectNotGrantedUser(String userId, String projectId, String describe) {
        return portalProjectUserGrantMapper.selectNotGrantedUser(projectId, userId, describe);
    }
}

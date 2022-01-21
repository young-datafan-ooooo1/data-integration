package com.youngdatafan.portal.common.project.mapper;

import com.youngdatafan.portal.common.project.dto.ProjectDTO;
import com.youngdatafan.portal.common.project.dto.UserInfoDTO;
import com.youngdatafan.portal.common.project.entity.DpPortalProject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DpPortalProjectMapper {
    int deleteByPrimaryKey(String projectId);

    int insert(DpPortalProject record);

    int insertSelective(DpPortalProject record);

    DpPortalProject selectByPrimaryKey(String projectId);

    int updateByPrimaryKeySelective(DpPortalProject record);

    int updateByPrimaryKey(DpPortalProject record);

    List<ProjectDTO> selectLikeByProjectName(@Param("projectName") String projectName, @Param("projectType") String projectType
            , @Param("userId") String userId);

    List<ProjectDTO> selectLikeByGroupId(@Param("projectName") String projectName, @Param("groupId") String groupId
            , @Param("projectType") String projectType
            , @Param("userId") String userId);

    List<ProjectDTO> selectMyProject(@Param("projectName") String projectName, @Param("groupName") String groupName, @Param("userId") String userId
            , @Param("projectType") String projectType);

    List<UserInfoDTO> selectGrantMyProject(@Param("projectName") String projectName, @Param("userId") String userId
            , @Param("projectType") String projectType);

    List<ProjectDTO> selectAllProject(@Param("projectName") String projectName, @Param("userId") String userId
            , @Param("projectType") String projectType);

    int selectExists(DpPortalProject record);

    int deleteByProjectIds(@Param("projectIds") String[] projectIds);
}

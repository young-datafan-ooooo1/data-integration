package com.youngdatafan.portal.common.project.mapper;

import com.youngdatafan.portal.common.project.dto.ProjectUserGrantDTO;
import com.youngdatafan.portal.common.project.dto.UserInfoDTO;
import com.youngdatafan.portal.common.project.entity.DpPortalProjectUserGrant;
import com.youngdatafan.portal.common.project.vo.ProjectUserGrantVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DpPortalProjectUserGrantMapper {
    int insert(DpPortalProjectUserGrant record);

    int insertSelective(DpPortalProjectUserGrant record);

    List<ProjectUserGrantDTO> selectUserGrant(@Param("projectId") String projectId);

    int deleteByProjectId(@Param("projectId") String projectId);

    int insertGrant(@Param("projectId") String projectId, @Param("projectUserGrants") List<ProjectUserGrantVO> projectUserGrants);

    int deleteByProjectIds(@Param("projectIds") String[] projectIds);

    List<UserInfoDTO> selectNotGrantedUser(@Param("projectId") String projectId, @Param("userId") String userId, @Param("describe") String describe);
}
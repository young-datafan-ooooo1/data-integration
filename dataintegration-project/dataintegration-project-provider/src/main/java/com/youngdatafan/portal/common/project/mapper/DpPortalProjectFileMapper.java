package com.youngdatafan.portal.common.project.mapper;

import com.youngdatafan.portal.common.project.dto.ProjectFileDTO;
import com.youngdatafan.portal.common.project.entity.DpPortalProjectFile;
import org.apache.ibatis.annotations.Param;

public interface DpPortalProjectFileMapper {
    int deleteByPrimaryKey(String projectId);

    int insert(DpPortalProjectFile record);

    int insertSelective(DpPortalProjectFile record);

    ProjectFileDTO selectByPrimaryKey(String projectId);

    int updateByPrimaryKeySelective(DpPortalProjectFile record);

    int updateByPrimaryKey(DpPortalProjectFile record);


    int deleteByProjectIds(@Param("projectIds") String[] projectIds);
}
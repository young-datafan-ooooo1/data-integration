package com.youngdatafan.portal.common.project.mapper;

import org.apache.ibatis.annotations.Param;


/**
 * DpPortalProjectOnlineMapper.
 */
public interface DpPortalProjectOnlineMapper {
    /**
     * 通过Id删除.
     *
     * @param projectId 项目id
     * @return int
     */
    int deleteByPrimaryKey(String projectId);

    /**
     * 批量删除.
     *
     * @param projectIds 项目Id
     * @return int
     */
    int batchDelete(@Param("projectIds") String[] projectIds);
}

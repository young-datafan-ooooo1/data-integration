package com.youngdatafan.portal.common.project.mapper;

import org.apache.ibatis.annotations.Param;

/**
 * @Author: jeremychen
 * @Descripition:
 * @Date:2020/4/10 10:40 上午
 */
public interface DpPortalProjectOnlineMapper {
    int deleteByPrimaryKey(String projectId);

    int batchDelete(@Param("projectIds") String[] projectIds);
}
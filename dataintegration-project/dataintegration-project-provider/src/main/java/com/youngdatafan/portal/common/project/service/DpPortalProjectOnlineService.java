package com.youngdatafan.portal.common.project.service;

import com.youngdatafan.portal.common.project.mapper.DpPortalProjectOnlineMapper;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * DpPortalProjectOnlineService.
 */
@Service
public class DpPortalProjectOnlineService {

    @Resource
    private DpPortalProjectOnlineMapper dpPortalProjectOnlineMapper;

    /**
     * 通过项目ID删除.
     *
     * @param projectId 项目Id
     * @return int
     */
    public int deleteByPrimaryKey(String projectId) {
        return dpPortalProjectOnlineMapper.deleteByPrimaryKey(projectId);
    }

    /**
     * 通过项目id批量删除.
     *
     * @param projectIds 项目ids
     * @return int
     */
    public int batchDelete(String[] projectIds) {
        return dpPortalProjectOnlineMapper.batchDelete(projectIds);
    }

}

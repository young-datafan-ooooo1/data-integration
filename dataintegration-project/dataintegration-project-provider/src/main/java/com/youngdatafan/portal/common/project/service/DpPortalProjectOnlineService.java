package com.youngdatafan.portal.common.project.service;

import com.youngdatafan.portal.common.project.mapper.DpPortalProjectOnlineMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author: jeremychen
 * @Descripition:
 * @Date:2020/4/10 10:40 上午
 */
@Service
public class DpPortalProjectOnlineService {

    @Resource
    private DpPortalProjectOnlineMapper dpPortalProjectOnlineMapper;


    public int deleteByPrimaryKey(String projectId) {
        return dpPortalProjectOnlineMapper.deleteByPrimaryKey(projectId);
    }

    public int batchDelete(String[] projectIds) {
        return dpPortalProjectOnlineMapper.batchDelete(projectIds);
    }


}

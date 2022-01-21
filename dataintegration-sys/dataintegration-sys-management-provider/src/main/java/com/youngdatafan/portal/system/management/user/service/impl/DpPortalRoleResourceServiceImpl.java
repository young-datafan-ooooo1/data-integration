package com.youngdatafan.portal.system.management.user.service.impl;

import com.youngdatafan.portal.system.management.user.entity.DpPortalRoleResource;
import com.youngdatafan.portal.system.management.user.mapper.DpPortalRoleResourceMapper;
import com.youngdatafan.portal.system.management.user.service.DpPortalRoleResourceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: jeremychen
 * @Descripition:
 * @Date:2020/2/11 7:56 下午
 */
@Service
public class DpPortalRoleResourceServiceImpl implements DpPortalRoleResourceService {

    @Resource
    private DpPortalRoleResourceMapper dpPortalRoleResourceMapper;

    @Override
    public int insert(DpPortalRoleResource record) {
        return dpPortalRoleResourceMapper.insert(record);
    }

    @Override
    public int insertSelective(DpPortalRoleResource record) {
        return dpPortalRoleResourceMapper.insertSelective(record);
    }

    @Override
    public int deleteByRoleId(String roleId) {
        return dpPortalRoleResourceMapper.deleteByRoleId(roleId);
    }

    @Override
    public int batchInsert(List<DpPortalRoleResource> records) {
        return dpPortalRoleResourceMapper.batchInsert(records);
    }

    @Override
    public int deleteByRoleIds(List<String> roleIds) {
        return dpPortalRoleResourceMapper.deleteByRoleIds(roleIds);
    }

    @Override
    public int deleteByResId(String resourceId) {
        return dpPortalRoleResourceMapper.deleteByResId(resourceId);
    }

}

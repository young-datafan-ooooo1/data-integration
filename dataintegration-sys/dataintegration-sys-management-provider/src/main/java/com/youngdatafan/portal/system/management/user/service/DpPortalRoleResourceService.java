package com.youngdatafan.portal.system.management.user.service;

import com.youngdatafan.portal.system.management.user.entity.DpPortalRoleResource;

import java.util.List;

/**
 * @Author: jeremychen
 * @Descripition:
 * @Date:2020/2/11 7:56 下午
 */
public interface DpPortalRoleResourceService {


    int insert(DpPortalRoleResource record);

    int insertSelective(DpPortalRoleResource record);

    int deleteByRoleId(String roleId);

    int batchInsert(List<DpPortalRoleResource> records);

    int deleteByRoleIds(List<String> roleIds);

    int deleteByResId(String resourceId);


}

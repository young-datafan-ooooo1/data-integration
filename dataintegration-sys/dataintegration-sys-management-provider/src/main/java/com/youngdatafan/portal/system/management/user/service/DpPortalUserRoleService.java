package com.youngdatafan.portal.system.management.user.service;

import com.youngdatafan.portal.system.management.user.entity.DpPortalUserRole;

import java.util.List;

/**
 * @Author: jeremychen
 * @Descripition:
 * @Date:2020/2/10 6:14 下午
 */
public interface DpPortalUserRoleService {


    int insert(DpPortalUserRole record);

    int insertSelective(DpPortalUserRole record);

    List<String> selectRoleIdsByUserId(String userId);

    List<DpPortalUserRole> selectRolesByUserId(String userId);

    int batchInsert(List<DpPortalUserRole> records);

    int deleteByUserId(String userId);

    int deleteByUserIds(List<String> userIds);

    int deleteByRoleId(String roleId);




}

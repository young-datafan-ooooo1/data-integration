package com.youngdatafan.portal.system.management.user.mapper;

import com.youngdatafan.portal.system.management.user.entity.DpPortalUserRole;

import java.util.List;

/**
 * @Author: jeremychen
 * @Descripition:
 * @Date:2020/2/10 6:14 下午
 */
public interface DpPortalUserRoleMapper {
    int insert(DpPortalUserRole record);

    int insertSelective(DpPortalUserRole record);

    int batchInsert(List<DpPortalUserRole> records);

    List<DpPortalUserRole> selectRolesByUserId(String userId);

    int deleteByUserId(String userId);

    int deleteByRoleId(String roleId);

    int deleteByUserIds(List<String> userIds);

}
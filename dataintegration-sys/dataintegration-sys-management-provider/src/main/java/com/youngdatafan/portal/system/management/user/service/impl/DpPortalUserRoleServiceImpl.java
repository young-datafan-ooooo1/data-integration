package com.youngdatafan.portal.system.management.user.service.impl;

import com.youngdatafan.portal.system.management.user.entity.DpPortalUserRole;
import com.youngdatafan.portal.system.management.user.mapper.DpPortalUserRoleMapper;
import com.youngdatafan.portal.system.management.user.service.DpPortalUserRoleService;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * DpPortalUserRoleServiceImpl.
 */
@Service
public class DpPortalUserRoleServiceImpl implements DpPortalUserRoleService {

    @Resource
    private DpPortalUserRoleMapper dpPortalUserRoleMapper;

    @Override
    public int insert(DpPortalUserRole record) {
        return dpPortalUserRoleMapper.insert(record);
    }

    @Override
    public int insertSelective(DpPortalUserRole record) {
        return dpPortalUserRoleMapper.insertSelective(record);
    }

    @Override
    public List<String> selectRoleIdsByUserId(String userId) {
        List<String> roles = new ArrayList<>();
        List<DpPortalUserRole> dpPortalUserRoles = dpPortalUserRoleMapper.selectRolesByUserId(userId);
        dpPortalUserRoles.forEach(o -> {
            roles.add(o.getRoleId());
        });
        return roles;
    }

    @Override
    public List<DpPortalUserRole> selectRolesByUserId(String userId) {
        return dpPortalUserRoleMapper.selectRolesByUserId(userId);
    }

    @Override
    public int batchInsert(List<DpPortalUserRole> records) {
        return dpPortalUserRoleMapper.batchInsert(records);
    }

    @Override
    public int deleteByUserId(String userId) {
        return dpPortalUserRoleMapper.deleteByUserId(userId);
    }

    @Override
    public int deleteByUserIds(List<String> userIds) {
        return dpPortalUserRoleMapper.deleteByUserIds(userIds);
    }

    @Override
    public int deleteByRoleId(String roleId) {
        return dpPortalUserRoleMapper.deleteByRoleId(roleId);
    }

}

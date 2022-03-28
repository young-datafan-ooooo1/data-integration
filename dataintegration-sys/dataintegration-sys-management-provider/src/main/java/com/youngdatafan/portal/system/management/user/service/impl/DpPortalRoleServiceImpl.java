package com.youngdatafan.portal.system.management.user.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.youngdatafan.portal.system.management.common.utils.PageInfoUtil;
import com.youngdatafan.portal.system.management.user.dto.RoleDTO;
import com.youngdatafan.portal.system.management.user.entity.DpPortalRole;
import com.youngdatafan.portal.system.management.user.mapper.DpPortalRoleMapper;
import com.youngdatafan.portal.system.management.user.service.DpPortalRoleService;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * DpPortalRoleServiceImpl.
 */
@Service
public class DpPortalRoleServiceImpl implements DpPortalRoleService {

    @Resource
    private DpPortalRoleMapper dpPortalRoleMapper;

    @Override
    public int deleteByPrimaryKey(String roleId) {
        return dpPortalRoleMapper.deleteByPrimaryKey(roleId);
    }

    @Override
    public int insert(DpPortalRole record) {
        return dpPortalRoleMapper.insert(record);
    }

    @Override
    public int insertSelective(DpPortalRole record) {
        return dpPortalRoleMapper.insertSelective(record);
    }

    @Override
    public DpPortalRole selectByPrimaryKey(String roleId) {
        return dpPortalRoleMapper.selectByPrimaryKey(roleId);
    }

    @Override
    public int updateByPrimaryKeySelective(DpPortalRole record) {
        return dpPortalRoleMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(DpPortalRole record) {
        return dpPortalRoleMapper.updateByPrimaryKey(record);
    }

    @Override
    public int checkRoleName(String roleName, String roleId) {
        return dpPortalRoleMapper.checkRoleName(roleName, roleId);
    }

    @Override
    public List<RoleDTO> selectAllRoles(String createUserId) {
        List<DpPortalRole> dpPortalRoles = dpPortalRoleMapper.selectAllRole(createUserId);
        List<RoleDTO> roleDTOS = new ArrayList<>();
        dpPortalRoles.forEach(o -> {
            RoleDTO roleDTO = new RoleDTO();
            BeanUtils.copyProperties(o, roleDTO);
            roleDTOS.add(roleDTO);
        });

        return roleDTOS;
    }

    @Override
    public PageInfo<RoleDTO> selectAllRolesPage(String keyWord, String createUserId, Page page) {
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        List<DpPortalRole> dpPortalRoles = dpPortalRoleMapper.selectAllRoleByKeyWord(keyWord, createUserId);
        PageInfo<DpPortalRole> dpPortalRolePageInfo = new PageInfo<>(dpPortalRoles);
        return PageInfoUtil.pageInfo2PageInfoDTO(dpPortalRolePageInfo, RoleDTO.class);
    }

    @Override
    public List<DpPortalRole> selectGrantedRolesByUserId(String userId) {
        return dpPortalRoleMapper.selectGrantedRolesByUserId(userId);
    }

    @Override
    public int deleteByPrimaryKeys(List<String> roleIds, String createUserId) {
        return dpPortalRoleMapper.deleteByPrimaryKeys(roleIds, createUserId);
    }

}

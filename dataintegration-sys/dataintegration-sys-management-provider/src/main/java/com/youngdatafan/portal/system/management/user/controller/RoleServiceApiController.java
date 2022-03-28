package com.youngdatafan.portal.system.management.user.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.dataintegration.core.util.StatusCode;
import com.youngdatafan.dataintegration.core.util.UUIDUtils;
import com.youngdatafan.portal.system.management.common.utils.BaseController;
import com.youngdatafan.portal.system.management.user.api.RoleServiceApi;
import com.youngdatafan.portal.system.management.user.dto.RoleDTO;
import com.youngdatafan.portal.system.management.user.entity.DpPortalRole;
import com.youngdatafan.portal.system.management.user.entity.DpPortalRoleResource;
import com.youngdatafan.portal.system.management.user.service.DpPortalRoleResourceService;
import com.youngdatafan.portal.system.management.user.service.DpPortalRoleService;
import com.youngdatafan.portal.system.management.user.service.DpPortalUserRoleService;
import com.youngdatafan.portal.system.management.user.vo.RoleAddVo;
import com.youngdatafan.portal.system.management.user.vo.RoleUpdateVo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * RoleServiceApiController.
 */
@RestController
@RequestMapping("/role")
public class RoleServiceApiController extends BaseController<DpPortalRole> implements RoleServiceApi {

    private final DpPortalRoleService dpPortalRoleService;

    private final DpPortalRoleResourceService dpPortalRoleResourceService;

    private final DpPortalUserRoleService dpPortalUserRoleService;

    @Autowired
    public RoleServiceApiController(DpPortalRoleService dpPortalRoleService, DpPortalRoleResourceService dpPortalRoleResourceService,
                                    DpPortalUserRoleService dpPortalUserRoleService) {
        this.dpPortalRoleService = dpPortalRoleService;
        this.dpPortalRoleResourceService = dpPortalRoleResourceService;
        this.dpPortalUserRoleService = dpPortalUserRoleService;
    }

    @Override
    @Transactional
    public Result<RoleDTO, Object> add(RoleAddVo roleAddVo, String optUserId) {

        int i = dpPortalRoleService.checkRoleName(roleAddVo.getRoleName(), null);
        if (i > 0) {
            return Result.fail(StatusCode.CODE_10010.getCode(), null, "角色名称已存在");
        }
        DpPortalRole dpPortalRole = new DpPortalRole();
        dpPortalRole.setRoleId(UUIDUtils.generateUUID());
        dpPortalRole.setRoleName(roleAddVo.getRoleName());
        dpPortalRole.setDescribe(roleAddVo.getDescribe());
        dpPortalRole.setStatus(roleAddVo.getStatus());
        dpPortalRole.setCreateTime(new Date());
        dpPortalRole.setCreateUserId(optUserId);
        dpPortalRoleService.insert(dpPortalRole);
        RoleDTO roleDTO = new RoleDTO();
        BeanUtils.copyProperties(dpPortalRole, roleDTO);
        return Result.success(roleDTO);
    }

    @Override
    @Transactional
    public Result<RoleDTO, Object> update(RoleUpdateVo roleUpdateVo, String optUserId) {

        int i = dpPortalRoleService.checkRoleName(roleUpdateVo.getRoleName(), roleUpdateVo.getRoleId());
        if (i > 0) {
            return Result.fail(StatusCode.CODE_10010.getCode(), null, "角色名称已存在");
        }
        DpPortalRole dpPortalRole = new DpPortalRole();
        dpPortalRole.setRoleId(roleUpdateVo.getRoleId());
        dpPortalRole.setRoleName(roleUpdateVo.getRoleName());
        dpPortalRole.setDescribe(roleUpdateVo.getDescribe());
        dpPortalRole.setStatus(roleUpdateVo.getStatus());
        dpPortalRole.setUpdateTime(new Date());
        dpPortalRole.setCreateUserId(optUserId);
        dpPortalRoleService.updateByPrimaryKey(dpPortalRole);
//        if (roleUpdateVo.getStatus() != "0") {
//            dpPortalUserRoleService.deleteByRoleId(roleUpdateVo.getRoleId());
//        }
        RoleDTO roleDTO = new RoleDTO();
        BeanUtils.copyProperties(dpPortalRole, roleDTO);
        return Result.success(roleDTO);
    }

    @Override
    public Result<List<RoleDTO>, Object> selectAllRoles(String optUserId) {
        return Result.success(dpPortalRoleService.selectAllRoles(optUserId));
    }

    @Override
    public Result<PageInfo<RoleDTO>, Object> selectAllRolesPage(String pageSize, String curPage, String keyWord, String optUserId) {
        Page page = this.initPage(pageSize, curPage);
        return Result.success(dpPortalRoleService.selectAllRolesPage(keyWord, optUserId, page));
    }

    @Override
    @Transactional
    public Result<Boolean, Object> grantResouces(String roleId, String resources) {

        dpPortalRoleResourceService.deleteByRoleId(roleId);
        List<DpPortalRoleResource> roleResources = new ArrayList<>();
        String[] resrouceIds = resources.split(",");
        for (int i = 0; i < resrouceIds.length; i++) {
            DpPortalRoleResource dpPortalRoleResource = new DpPortalRoleResource();
            dpPortalRoleResource.setRoleId(roleId);
            dpPortalRoleResource.setResourceId(resrouceIds[i]);
            roleResources.add(dpPortalRoleResource);
        }
        int i = dpPortalRoleResourceService.batchInsert(roleResources);
        return Result.success(i > 0);
    }

    @Override
    @Transactional
    public Result<Boolean, Object> deleteRoles(String roleIds, String optUserId) {

        List<String> strings = Arrays.asList(roleIds.split(","));
        int i = dpPortalRoleService.deleteByPrimaryKeys(strings, optUserId);
        if (i > 0) {
            dpPortalRoleResourceService.deleteByRoleIds(strings);
        }
        return Result.success(i > 0);
    }

    @Override
    public Result<List<String>, Object> selectRoleIdsByUserId(String userId) {
        return Result.success(dpPortalUserRoleService.selectRoleIdsByUserId(userId));
    }

}

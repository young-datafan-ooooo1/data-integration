package com.youngdatafan.portal.system.management.user.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.dataintegration.core.util.StatusCode;
import com.youngdatafan.portal.system.management.common.utils.BaseController;
import com.youngdatafan.portal.system.management.user.api.ResourceServiceApi;
import com.youngdatafan.portal.system.management.user.dto.ResourceDTO;
import com.youngdatafan.portal.system.management.user.entity.DpPortalResource;
import com.youngdatafan.portal.system.management.user.entity.DpPortalRoleResource;
import com.youngdatafan.portal.system.management.user.service.DpPortalResourceService;
import com.youngdatafan.portal.system.management.user.service.DpPortalRoleResourceService;
import com.youngdatafan.portal.system.management.user.service.DpPortalUserRoleService;
import com.youngdatafan.portal.system.management.user.vo.ResouceUpdateOrderVO;
import com.youngdatafan.portal.system.management.user.vo.ResourceAddVO;
import com.youngdatafan.portal.system.management.user.vo.ResourceUpdateVO;
import java.util.Date;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ResourceServiceApiController.
 */
@RestController
@RequestMapping("/resource")
public class ResourceServiceApiController extends BaseController<ResourceDTO> implements ResourceServiceApi {

    private final DpPortalResourceService dpPortalResourceService;

    private final DpPortalRoleResourceService dpPortalRoleResourceService;

    private final DpPortalUserRoleService dpPortalUserRoleService;

    @Autowired
    public ResourceServiceApiController(DpPortalResourceService dpPortalResourceService,
                                        DpPortalRoleResourceService dpPortalRoleResourceService,
                                        DpPortalUserRoleService dpPortalUserRoleService) {
        this.dpPortalResourceService = dpPortalResourceService;
        this.dpPortalRoleResourceService = dpPortalRoleResourceService;
        this.dpPortalUserRoleService = dpPortalUserRoleService;
    }

    @Override
    public Result<List<ResourceDTO>, Object> selectAllResource(String optUserId) {
        return Result.success(dpPortalResourceService.selectAllResourceByUserId(optUserId));
    }

    @Override
    public Result<List<ResourceDTO>, Object> selectAllValidResource(String optUserId) {
        return Result.success(dpPortalResourceService.selectAllValidResourceByUserId(optUserId));
    }

    @Override
    public Result<List<ResourceDTO>, Object> selectAllGrantedResource(String userId) {
        return Result.success(dpPortalResourceService.selectAllResourceByUserId(userId));
    }

    @Override
    @Transactional
    public Result<ResourceDTO, Object> add(ResourceAddVO resourceAddVO, String optUserId) {

//        if (optUserId == null || optUserId.equals("") || !optUserId.equals("00000000")) {
//            return Result.fail(StatusCode.CODE_10010.getCode(), null, "只有超级管理员能操作菜单");
//        }
        int i = dpPortalResourceService.checkResourceId(resourceAddVO.getResId());
        if (i > 0) {
            return Result.fail(StatusCode.CODE_10010.getCode(), null, "菜单资源Id已存在");
        }

        DpPortalResource dpPortalResource = new DpPortalResource();
        dpPortalResource.setResId(resourceAddVO.getResId());
        dpPortalResource.setResName(resourceAddVO.getResName());
        dpPortalResource.setResLevel(resourceAddVO.getResLevel());
        dpPortalResource.setResOrder(resourceAddVO.getResOrder());
        dpPortalResource.setResPid(resourceAddVO.getResPid());
        dpPortalResource.setResType(resourceAddVO.getResType());
        dpPortalResource.setResUrl(resourceAddVO.getResUrl());
        dpPortalResource.setDescribe(resourceAddVO.getDescribe());
        dpPortalResource.setStatus(resourceAddVO.getStatus());
        dpPortalResource.setRouteUrl(resourceAddVO.getRouteUrl());
        dpPortalResource.setCreateTime(new Date());
        dpPortalResourceService.insertSelective(dpPortalResource);
        ResourceDTO resourceDTO = new ResourceDTO();
        BeanUtils.copyProperties(dpPortalResource, resourceDTO);

        List<String> roles = dpPortalUserRoleService.selectRoleIdsByUserId(optUserId);
        for (String role : roles) {
            DpPortalRoleResource dpPortalRoleResource = new DpPortalRoleResource();
            dpPortalRoleResource.setResourceId(dpPortalResource.getResId());
            dpPortalRoleResource.setRoleId(role);
            dpPortalRoleResourceService.insert(dpPortalRoleResource);
        }

        return Result.success(resourceDTO);
    }

    @Override
    public Result<ResourceDTO, Object> update(ResourceUpdateVO resourceUpdateDTO, String optUserId) {

//        if (optUserId == null || optUserId.equals("") || !optUserId.equals("00000000")) {
//            return Result.fail(StatusCode.CODE_10010.getCode(), null, "只有超级管理员能操作菜单");
//        }
        DpPortalResource dpPortalResource = new DpPortalResource();
        dpPortalResource.setResId(resourceUpdateDTO.getResId());
        dpPortalResource.setResName(resourceUpdateDTO.getResName());
        dpPortalResource.setResLevel(resourceUpdateDTO.getResLevel());
        dpPortalResource.setResOrder(resourceUpdateDTO.getResOrder());
        dpPortalResource.setResPid(resourceUpdateDTO.getResPid());
        dpPortalResource.setResType(resourceUpdateDTO.getResType());
        dpPortalResource.setResUrl(resourceUpdateDTO.getResUrl());
        dpPortalResource.setStatus(resourceUpdateDTO.getStatus());
        dpPortalResource.setDescribe(resourceUpdateDTO.getDescribe());
        dpPortalResource.setRouteUrl(resourceUpdateDTO.getRouteUrl());
        dpPortalResource.setUpdateTime(new Date());
        dpPortalResourceService.updateByPrimaryKeySelective(dpPortalResource);
        ResourceDTO resourceDTO = new ResourceDTO();
        BeanUtils.copyProperties(dpPortalResource, resourceDTO);

        return Result.success(resourceDTO);

    }

    @Override
    @Transactional
    public Result<Boolean, Object> deleteResource(String resId, String optUserId) {
        int check = dpPortalResourceService.checkIsHavingChild(resId);
        if (check > 0) {
            return Result.fail(StatusCode.CODE_10010.getCode(), null, "此资源菜单已经关联子菜单资源，请先删除子菜单资源");
        }

        int i = dpPortalResourceService.deleteByPrimaryKey(resId);
        if (i > 0) {
            dpPortalRoleResourceService.deleteByResId(resId);
        }

        return Result.success(i > 0);
    }

    @Override
    public Result<List<ResourceDTO>, Object> selectGrantedResources(String roleId) {
        return Result.success(dpPortalResourceService.selectGrantedResources(roleId));
    }

    @Override
    public Result<Boolean, Object> checkResourceName(String resName) {
        int i = dpPortalResourceService.checkResourceName(resName);
        return Result.success(i == 0);
    }

    @Override
    public Result<PageInfo<ResourceDTO>, Object> selectResourceByResNamePage(String pageSize, String curPage, String resName) {
        Page page = this.initPage(pageSize, curPage);
        PageInfo<ResourceDTO> pageInfo = dpPortalResourceService.selectAllResourceByResName(resName, page);
        return Result.success(pageInfo);
    }

    @Override
    public Result<List<ResourceDTO>, Object> selectResourceTree(String userId) {
        return Result.success(dpPortalResourceService.selectResourceTree(userId, null, true));
    }

    @Override
    public Result<List<ResourceDTO>, Object> selectResourceTreeForPortal(String userId) {
        return Result.success(dpPortalResourceService.selectResourceTree(userId, null, false));
    }

    @Override
    public Result<List<ResourceDTO>, Object> selectResourceTreeForSubPlatform(String userId, String parentResId) {
        return Result.success(dpPortalResourceService.selectResourceTree(userId, parentResId, false));
    }

    @Override
    public Result<List<ResourceDTO>, Object> selectGrantedResourcesTree(String roleId) {
        return Result.success(dpPortalResourceService.selectGrantedResourcesTree(roleId));
    }

    @Override
    public Result<Boolean, Object> updateResourceOrder(List<ResouceUpdateOrderVO> resouceUpdateOrderVOS) {
        if (resouceUpdateOrderVOS == null) {
            return Result.fail(StatusCode.CODE_10010.getCode(), false, "参数不能为空");
        }
        dpPortalResourceService.updateResourceOrder(resouceUpdateOrderVOS);
        return Result.success(true);
    }
}

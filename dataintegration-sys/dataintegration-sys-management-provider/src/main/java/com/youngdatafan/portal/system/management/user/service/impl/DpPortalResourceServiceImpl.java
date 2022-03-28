package com.youngdatafan.portal.system.management.user.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.youngdatafan.portal.system.management.common.utils.PageInfoUtil;
import com.youngdatafan.portal.system.management.user.dto.ResourceDTO;
import com.youngdatafan.portal.system.management.user.entity.DpPortalResource;
import com.youngdatafan.portal.system.management.user.mapper.DpPortalResourceMapper;
import com.youngdatafan.portal.system.management.user.service.DpPortalResourceService;
import com.youngdatafan.portal.system.management.user.vo.ResouceUpdateOrderVO;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * DpPortalResourceServiceImpl.
 */
@Service
public class DpPortalResourceServiceImpl implements DpPortalResourceService {

    @Resource
    private DpPortalResourceMapper dpPortalResourceMapper;

    @Override
    public int deleteByPrimaryKey(String resId) {
        return dpPortalResourceMapper.deleteByPrimaryKey(resId);
    }

    @Override
    public int insert(DpPortalResource record) {
        return dpPortalResourceMapper.insert(record);
    }

    @Override
    public int insertSelective(DpPortalResource record) {
        return dpPortalResourceMapper.insertSelective(record);
    }

    @Override
    public DpPortalResource selectByPrimaryKey(String resId) {
        return dpPortalResourceMapper.selectByPrimaryKey(resId);
    }

    @Override
    public int updateByPrimaryKeySelective(DpPortalResource record) {
        return dpPortalResourceMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(DpPortalResource record) {
        return dpPortalResourceMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<ResourceDTO> selectAllResourceByUserId(String userId) {
        List<DpPortalResource> dpPortalResources = dpPortalResourceMapper.selectAllResourceByUserId(userId);
        List<ResourceDTO> resourceDTOS = new ArrayList<>();
        dpPortalResources.forEach(o -> {
            ResourceDTO resourceDTO = new ResourceDTO();
            BeanUtils.copyProperties(o, resourceDTO);
            resourceDTOS.add(resourceDTO);
        });
        return resourceDTOS;
    }

    @Override
    public List<ResourceDTO> selectAllValidResourceByUserId(String userId) {
        List<DpPortalResource> dpPortalResources = dpPortalResourceMapper.selectAllValidResourceByUserId(userId);
        List<ResourceDTO> resourceDTOS = new ArrayList<>();
        dpPortalResources.forEach(o -> {
            ResourceDTO resourceDTO = new ResourceDTO();
            BeanUtils.copyProperties(o, resourceDTO);
            resourceDTOS.add(resourceDTO);
        });
        return resourceDTOS;
    }

    @Override
    public List<ResourceDTO> selectGrantedResources(String roleId) {
        List<DpPortalResource> dpPortalResources = dpPortalResourceMapper.selectGrantedResources(roleId);
        List<ResourceDTO> resourceDTOS = new ArrayList<>();
        dpPortalResources.forEach(o -> {
            ResourceDTO resourceDTO = new ResourceDTO();
            BeanUtils.copyProperties(o, resourceDTO);
            resourceDTOS.add(resourceDTO);
        });
        return resourceDTOS;
    }

    @Override
    public int checkResourceName(String resName) {
        return dpPortalResourceMapper.checkResourceName(resName);
    }

    @Override
    public int checkResourceId(String resId) {
        return dpPortalResourceMapper.checkResourceId(resId);
    }

    @Override
    public PageInfo<ResourceDTO> selectAllResourceByResName(String resName, Page page) {
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        List<DpPortalResource> dpPortalResources = dpPortalResourceMapper.selectAllResourceByResName(resName);
        PageInfo<DpPortalResource> portalResourcePageInfo = new PageInfo<>(dpPortalResources);
        PageInfo<ResourceDTO> pageInfo = PageInfoUtil.pageInfo2PageInfoDTO(portalResourcePageInfo, ResourceDTO.class);
        return pageInfo;
    }

    @Override
    public int checkIsHavingChild(String resId) {
        return dpPortalResourceMapper.checkIsHavingChild(resId);
    }

    @Override
    public List<ResourceDTO> selectResourceTree(String userId, String parentResId, boolean queryAll) {
        List<DpPortalResource> dpPortalResources = dpPortalResourceMapper.selectResourceTree(userId);
        List<DpPortalResource> parent;
        if (parentResId != null && !"".equals(parentResId)) {
            parent = dpPortalResources.stream().filter(o -> o.getResPid().equals(parentResId)).collect(Collectors.toList());
        } else {
            parent = dpPortalResources.stream().filter(o -> null == o.getResPid() || "".equals(o.getResPid())).collect(Collectors.toList());
        }
        List<ResourceDTO> resourceDTOS = new ArrayList<>();
        for (DpPortalResource dpPortalResource : parent) {
            ResourceDTO dto1 = new ResourceDTO();
            BeanUtils.copyProperties(dpPortalResource, dto1);
            resourceDTOS.add(dto1);
            generateTree(dpPortalResources, dto1, queryAll);
        }
        return resourceDTOS;
    }

    @Override
    public List<ResourceDTO> selectGrantedResourcesTree(String roleId) {
        List<DpPortalResource> dpPortalResources = dpPortalResourceMapper.selectGrantedResourcesTree(roleId);
        List<DpPortalResource> parent = dpPortalResources.stream().filter(o -> null == o.getResPid() || o.getResPid().equals("")).collect(Collectors.toList());
        List<ResourceDTO> resourceDTOS = new ArrayList<>();
        for (DpPortalResource dpPortalResource : parent) {
            ResourceDTO dto1 = new ResourceDTO();
            BeanUtils.copyProperties(dpPortalResource, dto1);
            resourceDTOS.add(dto1);
            generateTree(dpPortalResources, dto1, true);
        }
        return resourceDTOS;
    }

    @Transactional
    @Override
    public void updateResourceOrder(List<ResouceUpdateOrderVO> resouceUpdateOrderVOS) {
        for (ResouceUpdateOrderVO resouceUpdateOrderVO : resouceUpdateOrderVOS) {
            dpPortalResourceMapper.updateResourceOrder(resouceUpdateOrderVO.getResId(), resouceUpdateOrderVO.getOrder(), resouceUpdateOrderVO.getResPid(), resouceUpdateOrderVO.getResLevel());
        }
    }

    /**
     * 获取树形资源结构.
     *
     * @param list list
     * @param dto  dto
     */
    private void generateTree(List<DpPortalResource> list, ResourceDTO dto, boolean queryAll) {
        if (!queryAll && dto.getResType().equals("3")) {
            return;
        }
        List<DpPortalResource> child = list.stream().filter(o -> o.getResPid().equals(dto.getResId())).collect(Collectors.toList());
        List<ResourceDTO> resourceDTOS = new ArrayList<>();
        for (DpPortalResource dpPortalResource : child) {
            ResourceDTO dto1 = new ResourceDTO();
            BeanUtils.copyProperties(dpPortalResource, dto1);
            resourceDTOS.add(dto1);
            generateTree(list, dto1, queryAll);
        }
        dto.setChildResources(resourceDTOS);

    }

}

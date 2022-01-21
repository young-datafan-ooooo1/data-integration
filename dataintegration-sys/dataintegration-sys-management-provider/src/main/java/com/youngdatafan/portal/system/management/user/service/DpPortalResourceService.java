package com.youngdatafan.portal.system.management.user.service;

import com.youngdatafan.portal.system.management.user.dto.ResourceDTO;
import com.youngdatafan.portal.system.management.user.entity.DpPortalResource;
import com.youngdatafan.portal.system.management.user.vo.ResouceUpdateOrderVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @Author: jeremychen
 * @Descripition:
 * @Date:2020/2/11 5:24 下午
 */
public interface DpPortalResourceService {


    int deleteByPrimaryKey(String resId);

    int insert(DpPortalResource record);

    int insertSelective(DpPortalResource record);

    DpPortalResource selectByPrimaryKey(String resId);

    int updateByPrimaryKeySelective(DpPortalResource record);

    int updateByPrimaryKey(DpPortalResource record);

    List<ResourceDTO> selectAllResourceByUserId(String userId);

    List<ResourceDTO> selectAllValidResourceByUserId(String userId);

    List<ResourceDTO> selectGrantedResources(String roleId);

    int checkResourceName(String resName);

    int checkResourceId(String resId);

    PageInfo<ResourceDTO> selectAllResourceByResName(String resName, Page page);

    int checkIsHavingChild(String resId);

    List<ResourceDTO> selectResourceTree(String userId, String parentResId, boolean queryAll);

    List<ResourceDTO> selectGrantedResourcesTree(String roleId);

    void updateResourceOrder(List<ResouceUpdateOrderVO> resouceUpdateOrderVOS);


}

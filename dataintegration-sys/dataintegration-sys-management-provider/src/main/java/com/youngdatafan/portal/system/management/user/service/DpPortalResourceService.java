package com.youngdatafan.portal.system.management.user.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.youngdatafan.portal.system.management.user.dto.ResourceDTO;
import com.youngdatafan.portal.system.management.user.entity.DpPortalResource;
import com.youngdatafan.portal.system.management.user.vo.ResouceUpdateOrderVO;
import java.util.List;

/**
 * DpPortalResourceService.
 */
public interface DpPortalResourceService {

    /**
     * deleteByPrimaryKey.
     *
     * @param resId id
     * @return int
     */
    int deleteByPrimaryKey(String resId);

    /**
     * insert.
     *
     * @param record DpPortalResource
     * @return int
     */
    int insert(DpPortalResource record);

    /**
     * insertSelective.
     *
     * @param record DpPortalResource
     * @return int
     */
    int insertSelective(DpPortalResource record);

    /**
     * selectByPrimaryKey.
     *
     * @param resId id
     * @return DpPortalResource
     */
    DpPortalResource selectByPrimaryKey(String resId);

    /**
     * updateByPrimaryKeySelective.
     *
     * @param record DpPortalResource
     * @return int
     */
    int updateByPrimaryKeySelective(DpPortalResource record);

    /**
     * updateByPrimaryKey.
     *
     * @param record DpPortalResource
     * @return int
     */
    int updateByPrimaryKey(DpPortalResource record);

    /**
     * selectAllResourceByUserId.
     *
     * @param userId userId
     * @return list
     */
    List<ResourceDTO> selectAllResourceByUserId(String userId);

    /**
     * selectAllValidResourceByUserId.
     *
     * @param userId userId
     * @return list
     */
    List<ResourceDTO> selectAllValidResourceByUserId(String userId);

    /**
     * selectGrantedResources.
     *
     * @param roleId roleId
     * @return list
     */
    List<ResourceDTO> selectGrantedResources(String roleId);

    /**
     * checkResourceName.
     *
     * @param resName resName
     * @return int
     */
    int checkResourceName(String resName);

    /**
     * checkResourceId.
     *
     * @param resId resId
     * @return int
     */
    int checkResourceId(String resId);

    /**
     * selectAllResourceByResName.
     *
     * @param resName resName
     * @param page    page
     * @return PageInfo
     */
    PageInfo<ResourceDTO> selectAllResourceByResName(String resName, Page page);

    /**
     * checkIsHavingChild.
     *
     * @param resId resId
     * @return int
     */
    int checkIsHavingChild(String resId);

    /**
     * selectResourceTree.
     *
     * @param userId      userId
     * @param parentResId parentResId
     * @param queryAll    queryAll
     * @return list
     */
    List<ResourceDTO> selectResourceTree(String userId, String parentResId, boolean queryAll);

    /**
     * selectGrantedResourcesTree.
     *
     * @param roleId roleId
     * @return list¬
     */
    List<ResourceDTO> selectGrantedResourcesTree(String roleId);

    /**
     * updateResourceOrder.
     *
     * @param resouceUpdateOrderVOS list
     */
    void updateResourceOrder(List<ResouceUpdateOrderVO> resouceUpdateOrderVOS);

}

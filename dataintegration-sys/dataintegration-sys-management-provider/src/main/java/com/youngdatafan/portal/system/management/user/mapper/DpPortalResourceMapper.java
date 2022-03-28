package com.youngdatafan.portal.system.management.user.mapper;

import com.youngdatafan.portal.system.management.user.entity.DpPortalResource;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * DpPortalResourceMapper.
 */
public interface DpPortalResourceMapper {
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
     * @param record record
     * @return int
     */
    int insert(DpPortalResource record);

    /**
     * insertSelective.
     *
     * @param record record
     * @return int
     */
    int insertSelective(DpPortalResource record);

    /**
     * selectByPrimaryKey.
     *
     * @param resId resId
     * @return DpPortalResource
     */
    DpPortalResource selectByPrimaryKey(String resId);

    /**
     * updateByPrimaryKeySelective.
     *
     * @param record record
     * @return int
     */
    int updateByPrimaryKeySelective(DpPortalResource record);

    /**
     * updateByPrimaryKey.
     *
     * @param record record
     * @return int
     */
    int updateByPrimaryKey(DpPortalResource record);

    /**
     * selectAllResourceByUserId.
     *
     * @param userId userId
     * @return List
     */
    List<DpPortalResource> selectAllResourceByUserId(@Param("userId") String userId);

    /**
     * selectAllValidResourceByUserId.
     *
     * @param userId userId
     * @return List
     */
    List<DpPortalResource> selectAllValidResourceByUserId(@Param("userId") String userId);

    /**
     * selectGrantedResources.
     *
     * @param roleId roleId
     * @return list
     */
    List<DpPortalResource> selectGrantedResources(@Param("roleId") String roleId);

    /**
     * checkResourceName.
     *
     * @param resName resName
     * @return int
     */
    int checkResourceName(@Param("resName") String resName);

    /**
     * checkResourceId.
     *
     * @param resId id
     * @return int
     */
    int checkResourceId(@Param("resId") String resId);

    /**
     * selectAllResourceByResName.
     *
     * @param resName resName
     * @return list
     */
    List<DpPortalResource> selectAllResourceByResName(@Param("resName") String resName);

    /**
     * checkIsHavingChild.
     *
     * @param resId id
     * @return int
     */
    int checkIsHavingChild(String resId);

    /**
     * selectResourceTree.
     *
     * @param userId id
     * @return int
     */
    List<DpPortalResource> selectResourceTree(@Param("userId") String userId);

    /**
     * selectGrantedResourcesTree.
     *
     * @param roleId id
     * @return list
     */
    List<DpPortalResource> selectGrantedResourcesTree(@Param("roleId") String roleId);

    /**
     * updateResourceOrder.
     *
     * @param resId    id
     * @param order    order
     * @param resPid   pid
     * @param resLevel resLevel
     * @return int
     */
    int updateResourceOrder(@Param("resId") String resId, @Param("order") Integer order, @Param("resPid") String resPid, @Param("resLevel") Integer resLevel);

}

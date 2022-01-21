package com.youngdatafan.portal.system.management.user.mapper;

import com.youngdatafan.portal.system.management.user.entity.DpPortalResource;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: jeremychen
 * @Descripition:
 * @Date:2020/2/11 5:24 下午
 */
public interface DpPortalResourceMapper {
    int deleteByPrimaryKey(String resId);

    int insert(DpPortalResource record);

    int insertSelective(DpPortalResource record);

    DpPortalResource selectByPrimaryKey(String resId);

    int updateByPrimaryKeySelective(DpPortalResource record);

    int updateByPrimaryKey(DpPortalResource record);

    List<DpPortalResource> selectAllResourceByUserId(@Param("userId") String userId);

    List<DpPortalResource> selectAllValidResourceByUserId(@Param("userId") String userId);

    List<DpPortalResource> selectGrantedResources(@Param("roleId") String roleId);

    int checkResourceName(@Param("resName") String resName);

    int checkResourceId(@Param("resId") String resId);


    List<DpPortalResource> selectAllResourceByResName(@Param("resName") String resName);

    int checkIsHavingChild(String resId);

    List<DpPortalResource> selectResourceTree(@Param("userId") String userId);

    List<DpPortalResource> selectGrantedResourcesTree(@Param("roleId") String roleId);

    int updateResourceOrder(@Param("resId") String resId, @Param("order") Integer order, @Param("resPid") String resPid, @Param("resLevel") Integer resLevel);


}
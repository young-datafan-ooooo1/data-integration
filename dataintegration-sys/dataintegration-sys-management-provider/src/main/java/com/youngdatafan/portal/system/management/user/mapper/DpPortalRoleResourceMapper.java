package com.youngdatafan.portal.system.management.user.mapper;

import com.youngdatafan.portal.system.management.user.entity.DpPortalRoleResource;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: jeremychen
 * @Descripition:
 * @Date:2020/2/11 7:56 下午
 */
public interface DpPortalRoleResourceMapper {
    int insert(DpPortalRoleResource record);

    int insertSelective(DpPortalRoleResource record);

    int deleteByRoleId(@Param("roleId") String roleId);

    int deleteByRoleIds(@Param("roleIds") List<String> roleIds);

    int batchInsert(List<DpPortalRoleResource> records);

    int deleteByResId(String resourceId);
}
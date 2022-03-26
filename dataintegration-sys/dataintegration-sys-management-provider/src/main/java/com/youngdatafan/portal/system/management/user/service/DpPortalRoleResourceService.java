package com.youngdatafan.portal.system.management.user.service;

import com.youngdatafan.portal.system.management.user.entity.DpPortalRoleResource;
import java.util.List;

/**
 * DpPortalRoleResourceService.
 */
public interface DpPortalRoleResourceService {

    /**
     * insert.
     *
     * @param record DpPortalRoleResource
     * @return int
     */
    int insert(DpPortalRoleResource record);

    /**
     * insertSelective.
     *
     * @param record DpPortalRoleResource
     * @return int
     */
    int insertSelective(DpPortalRoleResource record);

    /**
     * deleteByRoleId.
     *
     * @param roleId roleId
     * @return int
     */
    int deleteByRoleId(String roleId);

    /**
     * batchInsert.
     *
     * @param records List
     * @return int
     */
    int batchInsert(List<DpPortalRoleResource> records);

    /**
     * deleteByRoleIds.
     *
     * @param roleIds roleIds
     * @return int
     */
    int deleteByRoleIds(List<String> roleIds);

    /**
     * deleteByResId.
     *
     * @param resourceId resourceId
     * @return int
     */
    int deleteByResId(String resourceId);

}

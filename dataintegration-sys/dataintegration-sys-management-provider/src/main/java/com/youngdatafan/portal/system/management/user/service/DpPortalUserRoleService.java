package com.youngdatafan.portal.system.management.user.service;

import com.youngdatafan.portal.system.management.user.entity.DpPortalUserRole;
import java.util.List;

/**
 * DpPortalUserRoleService.
 */
public interface DpPortalUserRoleService {

    /**
     * insert.
     *
     * @param record DpPortalUserRole
     * @return int
     */
    int insert(DpPortalUserRole record);

    /**
     * DpPortalUserRole.
     *
     * @param record DpPortalUserRole
     * @return int
     */
    int insertSelective(DpPortalUserRole record);

    /**
     * selectRoleIdsByUserId.
     *
     * @param userId userId
     * @return list
     */
    List<String> selectRoleIdsByUserId(String userId);

    /**
     * selectRolesByUserId.
     *
     * @param userId userId
     * @return list
     */
    List<DpPortalUserRole> selectRolesByUserId(String userId);

    /**
     * batchInsert.
     *
     * @param records records
     * @return int
     */
    int batchInsert(List<DpPortalUserRole> records);

    /**
     * deleteByUserId.
     *
     * @param userId userId
     * @return int
     */
    int deleteByUserId(String userId);

    /**
     * deleteByUserIds.
     *
     * @param userIds deleteByUserIds
     * @return int
     */
    int deleteByUserIds(List<String> userIds);

    /**
     * deleteByRoleId.
     *
     * @param roleId roleId
     * @return int
     */
    int deleteByRoleId(String roleId);

}

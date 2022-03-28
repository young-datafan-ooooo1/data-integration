package com.youngdatafan.portal.system.management.user.mapper;

import com.youngdatafan.portal.system.management.user.entity.DpPortalUserRole;
import java.util.List;

/**
 * DpPortalUserRoleMapper.
 */
public interface DpPortalUserRoleMapper {
    /**
     * insert.
     *
     * @param record record
     * @return int
     */
    int insert(DpPortalUserRole record);

    /**
     * insert.
     *
     * @param record record
     * @return int
     */
    int insertSelective(DpPortalUserRole record);

    /**
     * insert.
     *
     * @param records records
     * @return int
     */
    int batchInsert(List<DpPortalUserRole> records);

    /**
     * selectRolesByUserId.
     *
     * @param userId userId
     * @return list
     */
    List<DpPortalUserRole> selectRolesByUserId(String userId);

    /**
     * userId.
     *
     * @param userId userId
     * @return int
     */
    int deleteByUserId(String userId);

    /**
     * deleteByRoleId.
     *
     * @param roleId id
     * @return int
     */
    int deleteByRoleId(String roleId);

    /**
     * deleteByUserIds.
     * @param userIds id
     * @return int
     */
    int deleteByUserIds(List<String> userIds);

}

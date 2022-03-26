package com.youngdatafan.portal.system.management.user.mapper;

import com.youngdatafan.portal.system.management.user.entity.DpPortalRoleResource;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * DpPortalRoleResourceMapper.
 */
public interface DpPortalRoleResourceMapper {
    /**
     * insert.
     *
     * @param record record
     * @return int
     */
    int insert(DpPortalRoleResource record);

    /**
     * insertSelective.
     *
     * @param record record
     * @return int
     */
    int insertSelective(DpPortalRoleResource record);

    /**
     * deleteByRoleId.
     *
     * @param roleId roleId
     * @return int
     */
    int deleteByRoleId(@Param("roleId") String roleId);

    /**
     * deleteByRoleIds.
     *
     * @param roleIds roleIds
     * @return int
     */
    int deleteByRoleIds(@Param("roleIds") List<String> roleIds);

    /**
     * batchInsert.
     *
     * @param records records
     * @return int
     */
    int batchInsert(List<DpPortalRoleResource> records);

    /**
     * deleteByResId.
     *
     * @param resourceId resourceId
     * @return int
     */
    int deleteByResId(String resourceId);
}

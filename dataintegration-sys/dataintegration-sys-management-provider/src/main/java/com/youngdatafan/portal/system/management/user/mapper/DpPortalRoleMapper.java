package com.youngdatafan.portal.system.management.user.mapper;

import com.youngdatafan.portal.system.management.user.entity.DpPortalRole;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * DpPortalRoleMapper.
 */
public interface DpPortalRoleMapper {
    /**
     * deleteByPrimaryKey.
     *
     * @param roleId roleId
     * @return int
     */
    int deleteByPrimaryKey(String roleId);

    /**
     * insert.
     *
     * @param record record
     * @return int
     */
    int insert(DpPortalRole record);

    /**
     * insertSelective.
     *
     * @param record record
     * @return int
     */
    int insertSelective(DpPortalRole record);

    /**
     * selectByPrimaryKey.
     *
     * @param roleId roleId
     * @return int
     */
    DpPortalRole selectByPrimaryKey(String roleId);

    /**
     * updateByPrimaryKeySelective.
     *
     * @param record record
     * @return record
     */
    int updateByPrimaryKeySelective(DpPortalRole record);

    /**
     * updateByPrimaryKey.
     *
     * @param record record
     * @return int
     */
    int updateByPrimaryKey(DpPortalRole record);

    /**
     * checkRoleName.
     *
     * @param roleName roleName
     * @param roleId   roleId
     * @return int
     */
    int checkRoleName(@Param("roleName") String roleName, @Param("roleId") String roleId);

    /**
     * selectAllRole.
     *
     * @param createUserId createUserId
     * @return list
     */
    List<DpPortalRole> selectAllRole(@Param("createUserId") String createUserId);

    /**
     * selectAllRoleByKeyWord.
     *
     * @param keyWord      KeyWord
     * @param createUserId createUserId
     * @return list
     */
    List<DpPortalRole> selectAllRoleByKeyWord(@Param("keyWord") String keyWord, @Param("createUserId") String createUserId);

    /**
     * selectGrantedRolesByUserId.
     *
     * @param userId userId
     * @return list
     */
    List<DpPortalRole> selectGrantedRolesByUserId(@Param("userId") String userId);

    /**
     * deleteByPrimaryKeys.
     *
     * @param roleIds      roleIds
     * @param createUserId createUserId
     * @return int
     */
    int deleteByPrimaryKeys(@Param("roleIds") List<String> roleIds, @Param("createUserId") String createUserId);
}

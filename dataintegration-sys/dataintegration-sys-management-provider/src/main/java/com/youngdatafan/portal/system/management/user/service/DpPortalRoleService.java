package com.youngdatafan.portal.system.management.user.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.youngdatafan.portal.system.management.user.dto.RoleDTO;
import com.youngdatafan.portal.system.management.user.entity.DpPortalRole;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * DpPortalRoleService.
 */
public interface DpPortalRoleService {

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
     * @param record DpPortalRole
     * @return int
     */
    int insert(DpPortalRole record);

    /**
     * insertSelective.
     *
     * @param record DpPortalRole
     * @return int
     */
    int insertSelective(DpPortalRole record);

    /**
     * selectByPrimaryKey.
     *
     * @param roleId DpPortalRole
     * @return DpPortalRole
     */
    DpPortalRole selectByPrimaryKey(String roleId);

    /**
     * updateByPrimaryKeySelective.
     *
     * @param record DpPortalRole
     * @return int
     */
    int updateByPrimaryKeySelective(DpPortalRole record);

    /**
     * updateByPrimaryKey.
     *
     * @param record DpPortalRole
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
    int checkRoleName(String roleName, String roleId);

    /**
     * selectAllRoles.
     *
     * @param createUserId createUserId
     * @return list
     */
    List<RoleDTO> selectAllRoles(String createUserId);

    /**
     * selectAllRolesPage.
     *
     * @param keyWord      keyWord
     * @param createUserId createUserId
     * @param page         page
     * @return PageInfo
     */
    PageInfo<RoleDTO> selectAllRolesPage(String keyWord, String createUserId, Page page);

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
     * @param roleIds      deleteByPrimaryKeys
     * @param createUserId createUserId
     * @return int
     */
    int deleteByPrimaryKeys(List<String> roleIds, String createUserId);

}

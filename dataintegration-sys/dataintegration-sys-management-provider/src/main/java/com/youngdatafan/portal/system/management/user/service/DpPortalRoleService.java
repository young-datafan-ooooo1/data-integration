package com.youngdatafan.portal.system.management.user.service;

import com.youngdatafan.portal.system.management.user.dto.RoleDTO;
import com.youngdatafan.portal.system.management.user.entity.DpPortalRole;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: jeremychen
 * @Descripition:
 * @Date:2020/2/11 3:03 下午
 */
public interface DpPortalRoleService {


    int deleteByPrimaryKey(String roleId);

    int insert(DpPortalRole record);

    int insertSelective(DpPortalRole record);

    DpPortalRole selectByPrimaryKey(String roleId);

    int updateByPrimaryKeySelective(DpPortalRole record);

    int updateByPrimaryKey(DpPortalRole record);

    int checkRoleName(String roleName, String roleId);

    List<RoleDTO> selectAllRoles(String createUserId);

    PageInfo<RoleDTO> selectAllRolesPage(String keyWord, String createUserId, Page page);

    List<DpPortalRole> selectGrantedRolesByUserId(@Param("userId") String userId);

    int deleteByPrimaryKeys(List<String> roleIds, String createUserId);


}

package com.youngdatafan.portal.system.management.user.mapper;

import com.youngdatafan.portal.system.management.user.entity.DpPortalRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: jeremychen
 * @Descripition:
 * @Date:2020/2/11 3:03 下午
 */
public interface DpPortalRoleMapper {
    int deleteByPrimaryKey(String roleId);

    int insert(DpPortalRole record);

    int insertSelective(DpPortalRole record);

    DpPortalRole selectByPrimaryKey(String roleId);

    int updateByPrimaryKeySelective(DpPortalRole record);

    int updateByPrimaryKey(DpPortalRole record);

    int checkRoleName(@Param("roleName") String roleName, @Param("roleId") String roleId);

    List<DpPortalRole> selectAllRole(@Param("createUserId") String createUserId);

    List<DpPortalRole> selectAllRoleByKeyWord(@Param("keyWord") String KeyWord, @Param("createUserId") String createUserId);

    List<DpPortalRole> selectGrantedRolesByUserId(@Param("userId") String userId);

    int deleteByPrimaryKeys(@Param("roleIds") List<String> roleIds, @Param("createUserId") String createUserId);
}
package com.youngdatafan.portal.system.management.user.mapper;

import com.youngdatafan.portal.system.management.user.entity.DpPortalUser;
import com.youngdatafan.portal.system.management.user.entity.UserRoleEntity;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @Author: jeremychen
 * @Descripition:
 * @Date:2020/2/10 2:14 下午
 */
public interface DpPortalUserMapper {
    int deleteByPrimaryKey(String userId);

    int insert(DpPortalUser record);

    int insertSelective(DpPortalUser record);

    DpPortalUser selectByPrimaryKey(String userId);

    DpPortalUser selectByUserName(@Param("userName") String userName);

    DpPortalUser selectLoginInfoByUserName(@Param("userName") String userName);

    Integer checkExistsByUserName(String userName);

    int updateByPrimaryKeySelective(DpPortalUser record);

    int updateByPrimaryKey(DpPortalUser record);

    int deleteByUserId(@Param("userId") String userId, @Param("createUserId") String createUserId);

    int updateByUserNameSelective(DpPortalUser record);

    List<DpPortalUser> selectUsers(@Param("keyWord") String keyWord, @Param("createUserId") String createUserId);

    int deleteByUserIds(@Param("userIds") List<String> userIds, @Param("createUserId") String createUserId);

    int updateStatus(@Param("userId") String userId, @Param("status") int status);

    int updateLastLoginTime(@Param("userId") String userId, @Param("lastLoginTime") Date lastLoginTime);

    List<UserRoleEntity> selectUserRoleByUserName(@Param("userName") String userName);

    List<DpPortalUser> selectAuditingUser(@Param("userId") String userId);
}
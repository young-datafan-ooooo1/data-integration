package com.youngdatafan.portal.system.management.user.service;

import com.youngdatafan.portal.system.management.user.dto.UserDTO;
import com.youngdatafan.portal.system.management.user.entity.DpPortalUser;
import com.youngdatafan.portal.system.management.user.entity.UserRoleEntity;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.Date;
import java.util.List;

/**
 * @Author: jeremychen
 * @Descripition:
 * @Date:2020/2/10 2:14 下午
 */
public interface DpPortalUserService {


    int deleteByPrimaryKey(String userId);

    int insert(DpPortalUser record);

    int insertSelective(DpPortalUser record);

    DpPortalUser selectByPrimaryKey(String userId);

    int updateByPrimaryKeySelective(DpPortalUser record);

    int updateByPrimaryKey(DpPortalUser record);

    DpPortalUser selectByUserName(String userName);

    DpPortalUser selectLoginInfoByUserName(String userName);

    int deleteByUserId(String userName, String userId);

    Integer checkExistsByUserName(String userName);

    int updateByUserNameSelective(DpPortalUser record);

    PageInfo<UserDTO> selectUsers(String keyWord, Page<DpPortalUser> page, String userId);

    int deleteByUserIds(List<String> userIds, String createUserId);

    int getPasswordErrLongTime(String userName);

    void addPasswordErrLongTime(String userName);

    void resetPasswordErrLongTime(String userName);

    int updateStatus(String userId, int status);

    int updateLastLoginTime(String userId, Date lastLoginTime);

    List<UserRoleEntity> selectUserRoleByUserName(String userName);

    List<DpPortalUser> selectAuditingUser(String userId);


}

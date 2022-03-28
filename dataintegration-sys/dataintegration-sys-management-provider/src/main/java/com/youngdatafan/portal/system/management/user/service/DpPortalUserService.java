package com.youngdatafan.portal.system.management.user.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.youngdatafan.portal.system.management.user.dto.UserDTO;
import com.youngdatafan.portal.system.management.user.entity.DpPortalUser;
import com.youngdatafan.portal.system.management.user.entity.UserRoleEntity;
import java.util.Date;
import java.util.List;

/**
 * DpPortalUserService.
 */
public interface DpPortalUserService {

    /**
     * DpPortalUserService.
     *
     * @param userId id
     * @return int
     */
    int deleteByPrimaryKey(String userId);

    /**
     * insert.
     *
     * @param record DpPortalUser
     * @return int
     */
    int insert(DpPortalUser record);

    /**
     * insertSelective.
     *
     * @param record DpPortalUser
     * @return int
     */
    int insertSelective(DpPortalUser record);

    /**
     * selectByPrimaryKey.
     *
     * @param userId id
     * @return DpPortalUser
     */
    DpPortalUser selectByPrimaryKey(String userId);

    /**
     * updateByPrimaryKeySelective.
     *
     * @param record DpPortalUser
     * @return int
     */
    int updateByPrimaryKeySelective(DpPortalUser record);

    /**
     * updateByPrimaryKey.
     *
     * @param record DpPortalUser
     * @return int
     */
    int updateByPrimaryKey(DpPortalUser record);

    /**
     * selectByUserName.
     *
     * @param userName userName
     * @return DpPortalUser
     */
    DpPortalUser selectByUserName(String userName);

    /**
     * selectLoginInfoByUserName.
     *
     * @param userName userName
     * @return DpPortalUser
     */
    DpPortalUser selectLoginInfoByUserName(String userName);

    /**
     * deleteByUserId.
     *
     * @param userName userName
     * @param userId   userId
     * @return int
     */
    int deleteByUserId(String userName, String userId);

    /**
     * checkExistsByUserName.
     *
     * @param userName userName
     * @return Integer
     */
    Integer checkExistsByUserName(String userName);

    /**
     * updateByUserNameSelective.
     *
     * @param record DpPortalUser
     * @return int
     */
    int updateByUserNameSelective(DpPortalUser record);

    /**
     * selectUsers.
     *
     * @param keyWord keyWord
     * @param page    page
     * @param userId  id
     * @return PageInfo
     */
    PageInfo<UserDTO> selectUsers(String keyWord, Page<DpPortalUser> page, String userId);

    /**
     * deleteByUserIds.
     *
     * @param userIds      userIds
     * @param createUserId createUserId
     * @return int
     */
    int deleteByUserIds(List<String> userIds, String createUserId);

    /**
     * getPasswordErrLongTime.
     *
     * @param userName userName
     * @return int
     */
    int getPasswordErrLongTime(String userName);

    /**
     * addPasswordErrLongTime.
     *
     * @param userName userName
     */
    void addPasswordErrLongTime(String userName);

    /**
     * resetPasswordErrLongTime.
     *
     * @param userName userName
     */
    void resetPasswordErrLongTime(String userName);

    /**
     * updateStatus.
     *
     * @param userId id
     * @param status status
     * @return int
     */
    int updateStatus(String userId, int status);

    /**
     * updateLastLoginTime.
     *
     * @param userId        userId
     * @param lastLoginTime lastLoginTime
     * @return int
     */
    int updateLastLoginTime(String userId, Date lastLoginTime);

    /**
     * selectUserRoleByUserName.
     *
     * @param userName userName
     * @return list
     */
    List<UserRoleEntity> selectUserRoleByUserName(String userName);

    /**
     * selectAuditingUser.
     *
     * @param userId userId
     * @return userId
     */
    List<DpPortalUser> selectAuditingUser(String userId);

}

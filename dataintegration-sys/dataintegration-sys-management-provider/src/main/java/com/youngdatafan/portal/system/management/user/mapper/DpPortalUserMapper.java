package com.youngdatafan.portal.system.management.user.mapper;

import com.youngdatafan.portal.system.management.user.entity.DpPortalUser;
import com.youngdatafan.portal.system.management.user.entity.UserRoleEntity;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * DpPortalUserMapper.
 */
public interface DpPortalUserMapper {

    /**
     * deleteByPrimaryKey.
     *
     * @param userId id
     * @return int
     */
    int deleteByPrimaryKey(String userId);

    /**
     * insert.
     *
     * @param record record
     * @return int
     */
    int insert(DpPortalUser record);

    /**
     * insertSelective.
     *
     * @param record record
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
     * selectByUserName.
     *
     * @param userName userName
     * @return DpPortalUser
     */
    DpPortalUser selectByUserName(@Param("userName") String userName);

    /**
     * selectLoginInfoByUserName.
     *
     * @param userName userName
     * @return DpPortalUser
     */
    DpPortalUser selectLoginInfoByUserName(@Param("userName") String userName);

    /**
     * checkExistsByUserName.
     *
     * @param userName userName
     * @return Integer
     */
    Integer checkExistsByUserName(String userName);

    /**
     * updateByPrimaryKeySelective.
     *
     * @param record record
     * @return int
     */
    int updateByPrimaryKeySelective(DpPortalUser record);

    /**
     * updateByPrimaryKey.
     *
     * @param record record
     * @return int
     */
    int updateByPrimaryKey(DpPortalUser record);

    /**
     * deleteByUserId.
     *
     * @param userId       id
     * @param createUserId createUserId
     * @return int
     */
    int deleteByUserId(@Param("userId") String userId, @Param("createUserId") String createUserId);

    /**
     * updateByUserNameSelective.
     *
     * @param record record
     * @return int
     */
    int updateByUserNameSelective(DpPortalUser record);

    /**
     * selectUsers.
     *
     * @param keyWord      keyWord
     * @param createUserId createUserId
     * @return list
     */
    List<DpPortalUser> selectUsers(@Param("keyWord") String keyWord, @Param("createUserId") String createUserId);

    /**
     * deleteByUserIds.
     *
     * @param userIds      id
     * @param createUserId cId
     * @return int
     */
    int deleteByUserIds(@Param("userIds") List<String> userIds, @Param("createUserId") String createUserId);

    /**
     * updateStatus.
     *
     * @param userId updateStatus
     * @param status * @param status
     * @return int
     */
    int updateStatus(@Param("userId") String userId, @Param("status") int status);

    /**
     * updateLastLoginTime.
     *
     * @param userId        id
     * @param lastLoginTime time
     * @return int
     */
    int updateLastLoginTime(@Param("userId") String userId, @Param("lastLoginTime") Date lastLoginTime);

    /**
     * selectUserRoleByUserName.
     *
     * @param userName name
     * @return list
     */
    List<UserRoleEntity> selectUserRoleByUserName(@Param("userName") String userName);

    /**
     * selectAuditingUser.
     * @param userId id
     * @return list
     */
    List<DpPortalUser> selectAuditingUser(@Param("userId") String userId);
}

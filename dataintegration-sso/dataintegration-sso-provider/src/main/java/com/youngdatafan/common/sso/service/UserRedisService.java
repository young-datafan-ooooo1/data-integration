package com.youngdatafan.common.sso.service;

import com.youngdatafan.common.sso.entity.LoginInfo;

/**
 * UserRedisService.
 */
public interface UserRedisService {
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
     * addLoginStatus.
     *
     * @param loginInfo loginInfo
     */
    void addLoginStatus(LoginInfo loginInfo);

    /**
     * getLoginStatus.
     *
     * @param userName userName
     * @return LoginInfo
     */
    LoginInfo getLoginStatus(String userName);

    /**
     * removeLoginStatus.
     *
     * @param userName userName
     */
    void removeLoginStatus(String userName);

    /**
     * 添加tokenuid 缓存.
     *
     * @param tokenUid                    tokenUid
     * @param tokenUidCacheTimeoutSeconds tokenUidCacheTimeoutSeconds
     */
    void addTokenUidCache(String tokenUid, long tokenUidCacheTimeoutSeconds);

    /**
     * 删除 tokenuid 缓存.
     *
     * @param token token
     */
    void delTokenUidCache(String token);
}

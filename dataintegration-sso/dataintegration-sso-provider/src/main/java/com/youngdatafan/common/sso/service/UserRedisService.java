package com.youngdatafan.common.sso.service;

import com.youngdatafan.common.sso.entity.LoginInfo;

/**
 * @Author: jeremychen
 * @Descripition:
 * @Date:2020/3/13 2:36 下午
 */
public interface UserRedisService {
    int getPasswordErrLongTime(String userName);

    void addPasswordErrLongTime(String userName);

    void resetPasswordErrLongTime(String userName);

    void addLoginStatus(LoginInfo loginInfo);

    LoginInfo getLoginStatus(String userName);

    void removeLoginStatus(String userName);

    /**
     * 添加tokenuid 缓存
     */
    void addTokenUidCache(String tokenUid, long tokenUidCacheTimeoutSeconds);

    /**
     * 删除 tokenuid 缓存
     */
    void delTokenUidCache(String token);
}

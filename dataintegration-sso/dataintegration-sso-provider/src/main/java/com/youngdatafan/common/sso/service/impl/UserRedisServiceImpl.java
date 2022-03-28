package com.youngdatafan.common.sso.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.impl.NullClaim;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.youngdatafan.common.sso.config.JwtTokenEnhancer;
import com.youngdatafan.common.sso.entity.LoginInfo;
import com.youngdatafan.common.sso.service.UserRedisService;
import com.youngdatafan.dataintegration.core.util.Md5Utils;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * UserRedisServiceImpl.
 */
@Service
public class UserRedisServiceImpl implements UserRedisService {

    private String ssoUserLoginErrorCnt = "sso_user_login_error_cnt";

    private String ssoUserLoginStatusInfo = "sso_user_login_status_info";

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Value("${login.status.expireTime}")
    private long loginStatusExpireTime;

    @Override
    public int getPasswordErrLongTime(String userName) {

        Object times = stringRedisTemplate.opsForValue().get(Md5Utils.encode(ssoUserLoginErrorCnt + "#" + userName, "utf-8", false));
        if (times != null) {
            return Integer.valueOf(times.toString());
        } else {
            return 0;
        }
    }

    @Override
    public void addPasswordErrLongTime(String userName) {
        Object object = stringRedisTemplate.opsForValue().get(Md5Utils.encode(ssoUserLoginErrorCnt + "#" + userName, "utf-8", false));
        if (object == null) {
            stringRedisTemplate.opsForValue().set(Md5Utils.encode(ssoUserLoginErrorCnt + "#" + userName, "utf-8", false), String.valueOf(1));
        } else {
            int time = Integer.valueOf(object.toString());
            time = time + 1;
            stringRedisTemplate.opsForValue().set(Md5Utils.encode(ssoUserLoginErrorCnt + "#" + userName, "utf-8", false), String.valueOf(time));

        }

    }

    @Override
    public void resetPasswordErrLongTime(String userName) {
        stringRedisTemplate.opsForValue().set(Md5Utils.encode(ssoUserLoginErrorCnt + "#" + userName, "utf-8", false), String.valueOf(0), loginStatusExpireTime, TimeUnit.MILLISECONDS);

    }

    @Override
    public void addLoginStatus(LoginInfo loginInfo) {
        stringRedisTemplate.opsForValue()
            .set(Md5Utils.encode(ssoUserLoginStatusInfo + "#" + loginInfo.getUserName(), "utf-8", false), loginInfo.toJSON(), loginStatusExpireTime, TimeUnit.MILLISECONDS);

    }

    @Override
    public LoginInfo getLoginStatus(String userName) {
        String info = stringRedisTemplate.opsForValue().get(Md5Utils.encode(ssoUserLoginStatusInfo + "#" + userName, "utf-8", false));
        if (info != null && !"".equals(info)) {
            return LoginInfo.toObject(info);
        } else {
            return null;
        }
    }

    @Override
    public void removeLoginStatus(String userName) {
        stringRedisTemplate.delete(Md5Utils.encode(ssoUserLoginStatusInfo + "#" + userName, "utf-8", false));
    }

    @Override
    public void addTokenUidCache(String tokenUid, long tokenUidCacheTimeoutSeconds) {
        // 保存token uid到redis
        stringRedisTemplate.opsForValue().set(JwtTokenEnhancer.TOKEN_LAST_ACCESS_TIME + tokenUid, String.valueOf(System.currentTimeMillis()), tokenUidCacheTimeoutSeconds, TimeUnit.SECONDS);
    }

    @Override
    public void delTokenUidCache(String token) {
        final DecodedJWT decode = JWT.decode(token);
        final Map<String, Claim> claims = decode.getClaims();
        String tokenUid = claims.getOrDefault("token_uid", new NullClaim()).asString();
        stringRedisTemplate.delete(JwtTokenEnhancer.TOKEN_LAST_ACCESS_TIME + tokenUid);
    }

}

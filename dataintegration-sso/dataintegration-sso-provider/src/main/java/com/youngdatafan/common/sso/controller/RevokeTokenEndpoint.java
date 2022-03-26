package com.youngdatafan.common.sso.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.impl.NullClaim;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.youngdatafan.common.sso.config.JwtTokenEnhancer;
import com.youngdatafan.dataintegration.core.model.Result;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * RevokeTokenEndpoint.
 */
@RestController
public class RevokeTokenEndpoint {

    private final StringRedisTemplate redisTemplate;

    @Resource
    private ConsumerTokenServices consumerTokenServices;

    @Autowired
    public RevokeTokenEndpoint(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * revokeToken.
     *
     * @param token    token
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @return Result
     */
    @GetMapping("/oauth/logout")
    @ResponseBody
    public Result<Boolean, Object> revokeToken(@RequestHeader("Authorization") String token, HttpServletRequest request, HttpServletResponse response) {
        final boolean success = consumerTokenServices.revokeToken(token);
        // 删除token uid缓存
        deleteTokenUidCache(token);

        // 删除所有cookies信息
        Cookie[] cookies = request.getCookies();
        if (ArrayUtils.isNotEmpty(cookies)) {
            for (Cookie cookie : cookies) {
                cookie.setMaxAge(0);
                cookie.setPath("/");
                cookie.setValue("");
                response.addCookie(cookie);
            }
        }

        return Result.success(true, success ? "注销成功" : "注销失败");
    }

    /**
     * 删除token uid缓存.
     *
     * @param token token
     */
    private void deleteTokenUidCache(String token) {
        final DecodedJWT decode = JWT.decode(token);
        final Map<String, Claim> claims = decode.getClaims();
        String tokenUid = claims.getOrDefault("token_uid", new NullClaim()).asString();
        if (tokenUid != null) {
            redisTemplate.delete(JwtTokenEnhancer.TOKEN_LAST_ACCESS_TIME + tokenUid);
        }
    }
}

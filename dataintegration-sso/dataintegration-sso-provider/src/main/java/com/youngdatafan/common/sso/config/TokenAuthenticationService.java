package com.youngdatafan.common.sso.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * JWT生成，和验签
 *
 * @author dengguangming
 */

public class TokenAuthenticationService {

    public static final Logger logger = LoggerFactory.getLogger(TokenAuthenticationService.class);
    public static final String JWT_TOKEN_TYPE = "Bearer";


    // JWT验证方法
    static Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response, TokenStore tokenStore) {

        logger.info("request url is {}", request.getRequestURI());

        String token = request.getParameter("Authorization");
        token = token == null || token.equals("") ? request.getHeader("Authorization") : token;
        if (null == token || token.equals("")) {
            logger.error("Authorization 不能为空");
            return null;
        }
        //去除Bearer头
        if (token.startsWith(JWT_TOKEN_TYPE)) {
            token = token.substring(JWT_TOKEN_TYPE.length() + 1);
        }
        OAuth2Authentication oAuth2Authentication = tokenStore.readAuthentication(token);
//        logger.info("获取到登录信息：{}", JSONObject.toJSON(oAuth2Authentication));
        return oAuth2Authentication;

    }


}
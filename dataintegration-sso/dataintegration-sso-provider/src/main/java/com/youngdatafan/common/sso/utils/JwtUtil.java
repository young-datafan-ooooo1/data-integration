package com.youngdatafan.common.sso.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

/**
 * JwtUtil.
 */
public class JwtUtil {


    /**
     * 生成签名,15分钟后过期.
     *
     * @param tokenSecret tokenSecret
     * @param expireTime  expireTime
     * @param username    username
     * @param userId      userId
     * @param roles       roles
     * @return String
     */
    public static String sign(String tokenSecret, Long expireTime, String username, String userId, List<String> roles) {
        //过期时间
        Date date = new Date(System.currentTimeMillis() + expireTime);
        //私钥及加密算法
        Algorithm algorithm = Algorithm.HMAC256(tokenSecret);
        //设置头信息
        Map<String, Object> header = new HashMap<>(2);
        header.put("typ", "JWT");
        header.put("alg", "HS256");
        //附带username和userID生成签名
        return JWT.create().withHeader(header).withClaim("loginName", username)
            .withClaim("userId", userId).withClaim("roles", org.springframework.util.StringUtils.collectionToDelimitedString(roles, ",")).withExpiresAt(date).sign(algorithm);
    }

    /**
     * verity.
     *
     * @param token       token
     * @param tokenSecret tokenSecret
     * @return boolean
     */
    public static boolean verity(String token, String tokenSecret) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(tokenSecret);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        } catch (JWTVerificationException e) {
            return false;
        }

    }

    /**
     * 验证JWt并且获取Chaim.
     *
     * @param jwtSecret 秘钥
     * @param token     jwt密文
     * @return java.util.Map
     */
    public static Map<String, Claim> verityAndGetChaim(String jwtSecret, String token) {
        if (StringUtils.isBlank(token)) {
            throw new NullPointerException("token");
        }

        Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaims();
    }

    /**
     * getRolesFromToken.
     *
     * @param secret getRolesFromToken
     * @param token  token
     * @return String
     */
    public static String getRolesFromToken(String secret, String token) {
        Map<String, Claim> map = verityAndGetChaim(secret, token);
        return map.get("roles").asString();
    }

    /**
     * getUserNameFromToken.
     *
     * @param secret secret
     * @param token  token
     * @return String
     */
    public static String getUserNameFromToken(String secret, String token) {
        Map<String, Claim> map = verityAndGetChaim(secret, token);
        return map.get("loginName").asString();

    }

}

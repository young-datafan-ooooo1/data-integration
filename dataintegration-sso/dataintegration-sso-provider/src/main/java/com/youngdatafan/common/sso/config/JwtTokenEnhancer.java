package com.youngdatafan.common.sso.config;

import com.youngdatafan.common.sso.service.UserRedisService;
import com.youngdatafan.dataintegration.core.util.UUIDUtils;
import java.util.HashMap;
import java.util.Map;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

/**
 * JwtTokenEnhancer.
 *
 * @author gavin
 * @since 2020/3/16 7:08 下午
 */
public class JwtTokenEnhancer implements TokenEnhancer {
    public static final String TOKEN_LAST_ACCESS_TIME = "LastAccess_";

    private final UserRedisService userRedisService;

    private final long tokenUidCacheTimeoutSeconds;

    public JwtTokenEnhancer(UserRedisService userRedisService, long tokenUidCacheTimeoutSeconds) {
        this.userRedisService = userRedisService;
        this.tokenUidCacheTimeoutSeconds = tokenUidCacheTimeoutSeconds;
    }

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken oAuth2AccessToken, OAuth2Authentication oAuth2Authentication) {
        DpUser dpUser = (DpUser) oAuth2Authentication.getPrincipal();
        final String tokenUid = UUIDUtils.generateUUID32();

        Map<String, Object> info = new HashMap<>(5);
        // 添加userId
        info.put("user_id", dpUser.getUserId());
        info.put("login_ip", dpUser.getLoginIp());
        info.put("user_name_cn", dpUser.getUserNameCn());
        info.put("login_name", dpUser.getUsername());
        info.put("tenant_id", dpUser.getTenantId());
        info.put("token_uid", tokenUid);

        // 添加tokenuid 缓存
        userRedisService.addTokenUidCache(tokenUid, tokenUidCacheTimeoutSeconds);

        ((DefaultOAuth2AccessToken) oAuth2AccessToken).setAdditionalInformation(info);
        return oAuth2AccessToken;
    }
}

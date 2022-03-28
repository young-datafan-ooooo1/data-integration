package com.youngdatafan.common.sso.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * JwtAuthorizationProperties.
 *
 * @author gavin
 * @since 2020/3/17 11:05 上午
 */
@Data
@Component
@RefreshScope
@ConfigurationProperties(prefix = "dp.oauth2")
public class JwtAuthorizationProperties {

    private String withClient;

    private String clientSecret;

    private String[] grantTypes;

    private String[] scopes;

    private String[] redirectUris;

    private int accessTokenValiditySeconds;

    private int refreshTokenValiditySeconds;

    private String keyStoreKeyLocation;

    private String keyStorepass;

    private String keyPairAlias;

    private String keyPairPassword;

}

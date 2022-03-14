package com.youngdatafan.common.sso.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @author gavin
 * @since 2020/3/17 11:05 上午
 */
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

    public String getWithClient() {
        return withClient;
    }

    public void setWithClient(String withClient) {
        this.withClient = withClient;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String[] getGrantTypes() {
        return grantTypes;
    }

    public void setGrantTypes(String[] grantTypes) {
        this.grantTypes = grantTypes;
    }

    public String[] getScopes() {
        return scopes;
    }

    public void setScopes(String[] scopes) {
        this.scopes = scopes;
    }

    public int getAccessTokenValiditySeconds() {
        return accessTokenValiditySeconds;
    }

    public void setAccessTokenValiditySeconds(int accessTokenValiditySeconds) {
        this.accessTokenValiditySeconds = accessTokenValiditySeconds;
    }

    public int getRefreshTokenValiditySeconds() {
        return refreshTokenValiditySeconds;
    }

    public void setRefreshTokenValiditySeconds(int refreshTokenValiditySeconds) {
        this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
    }

    public String getKeyStoreKeyLocation() {
        return keyStoreKeyLocation;
    }

    public void setKeyStoreKeyLocation(String keyStoreKeyLocation) {
        this.keyStoreKeyLocation = keyStoreKeyLocation;
    }

    public String getKeyStorepass() {
        return keyStorepass;
    }

    public void setKeyStorepass(String keyStorepass) {
        this.keyStorepass = keyStorepass;
    }

    public String getKeyPairAlias() {
        return keyPairAlias;
    }

    public void setKeyPairAlias(String keyPairAlias) {
        this.keyPairAlias = keyPairAlias;
    }

    public String getKeyPairPassword() {
        return keyPairPassword;
    }

    public void setKeyPairPassword(String keyPairPassword) {
        this.keyPairPassword = keyPairPassword;
    }

    public String[] getRedirectUris() {
        return redirectUris;
    }

    public void setRedirectUris(String[] redirectUris) {
        this.redirectUris = redirectUris;
    }
}

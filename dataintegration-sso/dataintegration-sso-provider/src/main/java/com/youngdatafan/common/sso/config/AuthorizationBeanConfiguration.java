package com.youngdatafan.common.sso.config;

import com.youngdatafan.common.sso.service.UserRedisService;
import java.security.KeyPair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;

/**
 * AuthorizationBeanConfiguration.
 *
 * @author gavin
 * @since 2020/3/17 10:09 上午
 */
@Configuration
public class AuthorizationBeanConfiguration {

    @Value("${jwt.tokenUidCacheTimeoutSeconds:86400}")
    private long tokenUidCacheTimeoutSeconds;

    /**
     * keyPair.
     *
     * @param jwtAuthorizationProperties jwtAuthorizationProperties
     * @return KeyPair
     */
    @Bean
    public KeyPair keyPair(JwtAuthorizationProperties jwtAuthorizationProperties) {
        KeyStoreKeyFactory keyStoreKeyFactory =
            new KeyStoreKeyFactory(new ClassPathResource(jwtAuthorizationProperties.getKeyStoreKeyLocation()), jwtAuthorizationProperties.getKeyStorepass().toCharArray());
        return keyStoreKeyFactory.getKeyPair(jwtAuthorizationProperties.getKeyPairAlias(), jwtAuthorizationProperties.getKeyPairPassword().toCharArray());
    }

    /**
     * accessTokenConverter.
     *
     * @param keyPair keyPair
     * @return JwtAccessTokenConverter
     */
    @Bean
    public JwtAccessTokenConverter accessTokenConverter(KeyPair keyPair) {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setKeyPair(keyPair);
        return jwtAccessTokenConverter;
    }

    /**
     * jwtTokenEnhancer.
     *
     * @param userRedisService jwtTokenEnhancer
     * @return JwtTokenEnhancer
     */
    @Bean
    public JwtTokenEnhancer jwtTokenEnhancer(UserRedisService userRedisService) {
        return new JwtTokenEnhancer(userRedisService, tokenUidCacheTimeoutSeconds);
    }

}

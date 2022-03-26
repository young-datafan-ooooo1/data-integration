package com.youngdatafan.common.sso.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;

/**
 * AuthorizationCodeConfiguration.
 */
@Configuration
public class AuthorizationCodeConfiguration {

    /**
     * authorizationCodeServices.
     *
     * @param redisConnectionFactory redisConnectionFactory
     * @return AuthorizationCodeServices
     */
    @Bean
    public AuthorizationCodeServices authorizationCodeServices(RedisConnectionFactory redisConnectionFactory) {
        return new RedisAuthorizationCodeServices(redisConnectionFactory);
    }

}

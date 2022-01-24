package com.youngdatafan.common.sso.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;

/**
 * @Author: jeremychen
 * @Descripition:
 * @Date:2020/3/23 2:58 下午
 */
@Configuration
public class AuthorizationCodeConfiguration {

    @Bean
    public AuthorizationCodeServices authorizationCodeServices(RedisConnectionFactory redisConnectionFactory) {
        return new RedisAuthorizationCodeServices(redisConnectionFactory);
    }

}

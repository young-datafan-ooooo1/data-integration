package com.youngdatafan.common.sso.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * TokenStoreConfiguration.
 *
 * @author gavin
 * @since 2020/3/17 9:59 上午
 */
@Configuration
public class TokenStoreConfiguration {

    /**
     * redisTokenStore.
     *
     * @param redisConnectionFactory RedisConnectionFactory
     * @return TokenStore
     */
    @Bean
    public TokenStore redisTokenStore(RedisConnectionFactory redisConnectionFactory) {
        return new RedisTokenStore(redisConnectionFactory);
    }
}

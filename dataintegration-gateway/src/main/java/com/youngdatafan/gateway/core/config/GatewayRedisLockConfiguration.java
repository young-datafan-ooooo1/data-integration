package com.youngdatafan.gateway.core.config;

import com.youngdatafan.gateway.core.util.Constans;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.integration.redis.util.RedisLockRegistry;

/**
 * redis分布式锁配置
 *
 * @author gavin
 * @since 2020/8/28 3:59 下午
 */
@Configuration
public class GatewayRedisLockConfiguration {

    @Bean
    public RedisLockRegistry redisLockRegistry(RedisConnectionFactory redisConnectionFactory) {
        return new RedisLockRegistry(redisConnectionFactory, Constans.GATEWAY_LOCK_NAME);
    }
}


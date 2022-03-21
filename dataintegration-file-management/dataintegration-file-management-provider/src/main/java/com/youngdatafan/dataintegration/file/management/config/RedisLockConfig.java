package com.youngdatafan.dataintegration.file.management.config;

import com.youngdatafan.dataintegration.file.management.utils.RedisKeyConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.integration.redis.util.RedisLockRegistry;

/**
 * redis分布式锁.
 *
 * @author songxiaolang
 * @since 2022-01-04 18:09
 */
@Configuration
public class RedisLockConfig {

    /**
     * 创建redis分布式锁.
     *
     * @param redisConnectionFactory redis连接工厂.
     * @return redis分布式锁.
     */
    @Bean(destroyMethod = "destroy")
    public RedisLockRegistry redisLockRegistry(RedisConnectionFactory redisConnectionFactory) {
        return new RedisLockRegistry(redisConnectionFactory, RedisKeyConstants.ESCAT_FILE_SUFFIX);
    }
}

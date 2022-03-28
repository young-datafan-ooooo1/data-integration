package com.youngdatafan.portal.system.management.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * redis消息发布配置.
 *
 * @author gavin
 * @since 2020/6/13 2:17 下午
 */
@Configuration
public class PublisherConfig {

    /**
     * template.
     *
     * @param connectionFactory RedisConnectionFactory
     * @return StringRedisTemplate
     */
    @Bean
    public StringRedisTemplate template(RedisConnectionFactory connectionFactory) {
        return new StringRedisTemplate(connectionFactory);
    }
}

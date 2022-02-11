/*! ******************************************************************************
 * kettle 引擎配置文件
 *
 ******************************************************************************/

package com.youngdatafan.kettle.springboot.core.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

/**
 * 引擎redis操作工具类
 *
 * @author gavin
 */
@ConditionalOnClass({DataSource.class, RedisTemplate.class})
@Component
public class EngineRedisTemplate {

    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public EngineRedisTemplate(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private static class EngineRedisTemplateHelper {
        private static EngineRedisTemplate INSTANCE;
    }

    public static EngineRedisTemplate getInstance() {
        return EngineRedisTemplateHelper.INSTANCE;
    }

    @PostConstruct
    public void init() {
        // 赋值到Helper类
        EngineRedisTemplateHelper.INSTANCE = this;
    }

    public RedisTemplate<String, String> getRedisTemplate() {
        return redisTemplate;
    }
}

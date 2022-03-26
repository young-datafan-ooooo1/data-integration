package com.youngdatafan.portal.system.management.gateway.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * 消息发布.
 *
 * @author gavin
 * @since 2020/6/13 2:18 下午
 */
@Service
public class RedisPublisherService {

    private final StringRedisTemplate stringRedisTemplate;

    @Autowired
    public RedisPublisherService(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * pubMsg.
     *
     * @param channel channel
     * @param msg     msg
     */
    public void pubMsg(String channel, String msg) {
        stringRedisTemplate.convertAndSend(channel, msg);
    }
}

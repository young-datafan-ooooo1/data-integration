package com.youngdatafan.common.sso.config;

import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.common.util.SerializationUtils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.code.RandomValueAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.store.redis.JdkSerializationStrategy;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStoreSerializationStrategy;
import org.springframework.util.Assert;

/**
 * RedisAuthorizationCodeServices.
 */
public class RedisAuthorizationCodeServices extends RandomValueAuthorizationCodeServices {

    private static final String PREFIX = "authorization_code";

    private final RedisConnectionFactory redisConnectionFactory;

    private RedisTokenStoreSerializationStrategy serializationStrategy = new JdkSerializationStrategy();

    public RedisAuthorizationCodeServices(RedisConnectionFactory redisConnectionFactory) {
        Assert.notNull(redisConnectionFactory, "RedisConnection required");
        this.redisConnectionFactory = redisConnectionFactory;
    }

    @Override
    protected void store(String s, OAuth2Authentication authentication) {
        RedisConnection conn = this.redisConnectionFactory.getConnection();
        byte[] code = this.serializeKey(s);
        byte[] serializedAuth = this.serialize((Object) authentication);

        try {
            conn.openPipeline();
            conn.set(code, serializedAuth);
            conn.closePipeline();
        } finally {
            conn.close();
        }

    }

    private byte[] serializeKey(String object) {
        return this.serialize(PREFIX + object);
    }

    @Override
    protected OAuth2Authentication remove(String s) {
        RedisConnection conn = this.redisConnectionFactory.getConnection();
        byte[] code = this.serializeKey(s);
        OAuth2Authentication authentication;
        try {
            authentication = SerializationUtils.deserialize(conn.get(code));
        } catch (NullPointerException e) {
            return null;
        }

        if (authentication != null) {
            conn.del(new byte[][] {code});
        }

        return authentication;
    }

    private byte[] serialize(Object object) {
        return this.serializationStrategy.serialize(object);
    }
}

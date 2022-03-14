package com.youngdatafan.gateway.core.config;

import com.youngdatafan.gateway.ratelimit.GatewayRateLimitReceiver;
import com.youngdatafan.gateway.route.GatewayRouteReceiver;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

/**
 * redis消息订阅
 *
 * @author gavin
 * @since 2020/6/13 2:45 下午
 */
@Configuration
public class RedisSubscriberConfiguration {

    @Value("${gateway.route.channel:gateway_route}")
    private String gatewayRouteChannel;

    @Value("${gateway.ratelimit.channel:gateway_ratelimit}")
    private String gatewayRateLimitChannel;

    @Value("${gateway.ratelimit.channel:gateway_tenant}")
    private String gatewayTenantChannel;

    /**
     * 创建连接工厂.
     */
    @Bean
    public RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory
        , @Qualifier("gatewayRouteListenerAdapter") MessageListenerAdapter gatewayRouteListenerAdapter
        , @Qualifier("gatewayRateLimitListenerAdapter") MessageListenerAdapter gatewayRateLimitListenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(gatewayRouteListenerAdapter, new PatternTopic(gatewayRouteChannel));
        container.addMessageListener(gatewayRateLimitListenerAdapter, new PatternTopic(gatewayRateLimitChannel));
        return container;
    }

    /**
     * 绑定消息监听者和接收监听的方法.
     */
    @Bean("gatewayRateLimitListenerAdapter")
    public MessageListenerAdapter listenerAdapterRateLimit(GatewayRateLimitReceiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }

    /**
     * 绑定消息监听者和接收监听的方法.
     */
    @Bean("gatewayRouteListenerAdapter")
    public MessageListenerAdapter listenerAdapter(GatewayRouteReceiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }

}

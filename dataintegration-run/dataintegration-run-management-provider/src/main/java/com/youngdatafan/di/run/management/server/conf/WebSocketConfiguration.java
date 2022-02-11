package com.youngdatafan.di.run.management.server.conf;


import com.youngdatafan.di.run.management.server.websocket.DeAuthHandshakeInterceptor;
import com.youngdatafan.di.run.management.server.websocket.DePrincipalHandshakeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

/**
 * 开启websocket支持
 *
 * @author gavin
 * @since 2020/2/12 3:56 下午
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    private final DePrincipalHandshakeHandler myDefaultHandshakeHandler;
    private final DeAuthHandshakeInterceptor sessionDeAuthHandshakeInterceptor;

    @Autowired
    public WebSocketConfiguration(DePrincipalHandshakeHandler myDefaultHandshakeHandler, DeAuthHandshakeInterceptor sessionDeAuthHandshakeInterceptor) {
        this.myDefaultHandshakeHandler = myDefaultHandshakeHandler;
        this.sessionDeAuthHandshakeInterceptor = sessionDeAuthHandshakeInterceptor;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //开启/websocket端点
        registry.addEndpoint("/websocket")
                .addInterceptors(sessionDeAuthHandshakeInterceptor)
                .setHandshakeHandler(myDefaultHandshakeHandler)
                //允许跨域访问
                .setAllowedOrigins("*");
        //使用sockJS，注意开启sockJS，使用websocket就会报错
//                .withSockJS();

    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        registration.setMessageSizeLimit(10 * 1024 * 1024);
        registration.setSendBufferSizeLimit(30 * 1024 * 1024);
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 自定义调度器，用于控制心跳线程
        ThreadPoolTaskScheduler te = new ThreadPoolTaskScheduler();
        te.setPoolSize(10);
        te.setThreadNamePrefix("wss-heartbeat-thread-");
        te.initialize();

        //订阅Broker名称
        registry.enableSimpleBroker("/user")
                .setHeartbeatValue(new long[]{30000, 30000}).setTaskScheduler(te);

    }
}

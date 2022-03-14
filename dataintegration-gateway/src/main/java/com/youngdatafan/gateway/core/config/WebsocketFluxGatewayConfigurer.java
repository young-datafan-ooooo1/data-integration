package com.youngdatafan.gateway.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.reactive.socket.server.WebSocketService;
import org.springframework.web.reactive.socket.server.support.HandshakeWebSocketService;
import org.springframework.web.reactive.socket.server.upgrade.ReactorNettyRequestUpgradeStrategy;

/**
 * @author gavin
 * @since 2020/7/23 4:12 下午
 */
@Configuration
public class WebsocketFluxGatewayConfigurer {

    @Value("${spring.cloud.gateway.httpclient.websocket.max-frame-payload-length:65536}")
    private Integer websocketMaxFramePayloadLength;

    @Bean
    @Primary
    public WebSocketService customWebSocketService() {
        ReactorNettyRequestUpgradeStrategy requestUpgradeStrategy = new ReactorNettyRequestUpgradeStrategy();
        requestUpgradeStrategy.setMaxFramePayloadLength(websocketMaxFramePayloadLength);
        return new HandshakeWebSocketService(requestUpgradeStrategy);
    }
}

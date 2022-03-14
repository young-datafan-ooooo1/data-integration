package com.youngdatafan.di.run.management.server.websocket;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

/**
 * @author gavin
 * @since 2020/2/20 12:41 下午
 */
@Component
public class DePrincipalHandshakeHandler extends DefaultHandshakeHandler {
    private static final Logger log = LoggerFactory.getLogger(DePrincipalHandshakeHandler.class);

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        String userId = request.getHeaders().getFirst("authorization-userId");
        String userName = request.getHeaders().getFirst("authorization-userName");

        if (StringUtils.isEmpty(userId)) {
            log.error("未登录系统，禁止登录websocket!");
            return null;
        }

        log.info(" DePrincipalHandshakeHandler login = {}", userId);
        return new DePrincipal(userId, userName);
    }

}

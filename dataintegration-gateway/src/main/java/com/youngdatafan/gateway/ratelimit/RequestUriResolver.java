package com.youngdatafan.gateway.ratelimit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * request uri分解器
 *
 * @author gavin
 */
@Component(RequestUriResolver.BEAN_NAME)
public class RequestUriResolver implements KeyResolver {
    public static final String BEAN_NAME = "requestUriResolver";

    private static final Logger log = LoggerFactory.getLogger(RequestUriResolver.class);

    public static String getKey(ServerWebExchange exchange) {
        final ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        Route route = exchange.getAttribute("org.springframework.cloud.gateway.support.ServerWebExchangeUtils.gatewayRoute");
        if (route == null) {
            log.debug("path: {}", path);
            return path;
        }

        // 路由id+路径
        path = "/" + route.getId() + path;

        log.debug("path: {}", path);
        return path;
    }

    @Override
    public Mono<String> resolve(ServerWebExchange exchange) {
        return Mono.just(getKey(exchange));
    }

}

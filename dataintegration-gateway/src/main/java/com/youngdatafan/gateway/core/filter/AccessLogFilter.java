package com.youngdatafan.gateway.core.filter;

import com.youngdatafan.gateway.core.util.IpUtil;
import com.youngdatafan.gateway.log.task.AsyncLogTask;
import com.youngdatafan.portal.system.management.log.vo.LogVO;
import java.net.URI;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 请求日志保存过滤器
 *
 * @author gavin
 * @since 2020/2/24 11:25 上午
 */
@Component
public class AccessLogFilter implements GlobalFilter, Ordered {

    private static final String START_TIME = "gateway_start_time";

    private final AsyncLogTask logTask;

    @Autowired
    public AccessLogFilter(AsyncLogTask logTask) {
        this.logTask = logTask;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest originalRequest = exchange.getRequest();
        URI originalRequestUrl = originalRequest.getURI();
        //只记录http的请求
        String scheme = originalRequestUrl.getScheme();
        if ((!"http".equals(scheme) && !"https".equals(scheme))) {
            return chain.filter(exchange);
        }

        // 日志对象
        final LogVO dpPortalLog = new LogVO();
        dpPortalLog.setReqTime(new Date());

        // 请求ip
        dpPortalLog.setReqIp(IpUtil.getIpAddress(originalRequest));

        // 请求内容大小
        final HttpHeaders headers = originalRequest.getHeaders();
        String contentLength = headers.getFirst(HttpHeaders.CONTENT_LENGTH);
        if (contentLength != null) {
            dpPortalLog.setReqSize(Integer.parseInt(contentLength));
        }

        // 请求用户信息
        String userId = headers.getFirst("authorization-userId");
        String userName = headers.getFirst("authorization-userName");
        dpPortalLog.setReqUserId(userId);
        dpPortalLog.setReqUserName(userName);

        // 请求路径
        String path = originalRequest.getURI().getPath();
        dpPortalLog.setReqUri(path);

        // 服务名称
        Route route = exchange.getAttribute("org.springframework.cloud.gateway.support.ServerWebExchangeUtils.gatewayRoute");
        if (route != null) {
            dpPortalLog.setServerName(route.getId());
        }

        exchange.getAttributes().put(START_TIME, System.currentTimeMillis());
        return chain.filter(exchange).doFinally(subscriber -> {
            // 执行时间统计
            Long startTime = exchange.getAttribute(START_TIME);
            if (startTime != null) {
                long executeTime = (System.currentTimeMillis() - startTime);
                dpPortalLog.setExcTime((int) executeTime);
            }

            // 日志保存
            logTask.put(dpPortalLog);
        });
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}

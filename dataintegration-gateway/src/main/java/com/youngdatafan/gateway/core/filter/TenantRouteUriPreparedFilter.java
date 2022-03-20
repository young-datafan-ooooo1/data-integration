package com.youngdatafan.gateway.core.filter;

import com.youngdatafan.dataintegration.core.util.Constans;
import com.youngdatafan.gateway.tenant.UserTenantRepository;
import java.net.URI;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.functors.EqualPredicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.RouteToRequestUrlFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR;

/**
 * 多租户预处理
 *
 * @author gavin
 * @since 2020/2/24 11:25 上午
 */
@Component
public class TenantRouteUriPreparedFilter implements GlobalFilter, Ordered {

    /**
     * 使用租户路由
     */
    public static final String USE_TENANT_ROUTE = "useTenantRoute";

    private static final Logger log = LoggerFactory.getLogger(TenantRouteUriPreparedFilter.class);

    private final UserTenantRepository userTenantRepository;

    @Autowired
    public TenantRouteUriPreparedFilter(UserTenantRepository userTenantRepository) {
        this.userTenantRepository = userTenantRepository;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        Route route = exchange.getAttribute(GATEWAY_ROUTE_ATTR);
        if (route == null) {
            return chain.filter(exchange);
        }

        // 是否使用租户路由
        boolean useTenantRoute = (boolean) route.getMetadata().getOrDefault(USE_TENANT_ROUTE, false);
        if (!useTenantRoute) {
            return chain.filter(exchange);
        }

        // 获取用户id
        String userId = exchange.getRequest().getHeaders().getFirst(Constans.AUTHORIZATION_USERID);
        // 获取租户id
        final String tenantId = tenantId(userId);
        // 获取租户服务集合
        List<String> tenantServers = getTenantServers(tenantId);

        log.debug("userID: {} ,tenantId: {} ，tenantServers：{}", getString(userId), getString(tenantId), getString(tenantServers));

        // 判断租户是否有这个服务
        if (tenantId != null && tenantServers != null && CollectionUtils.exists(tenantServers, EqualPredicate.getInstance(route.getId()))) {
            // 重试标志
            final Integer retryIteration = exchange.getAttribute("retry_iteration");
            // 跳过重试或者已经标记了租户id的路由
            if (retryIteration == null && !route.getId().endsWith(tenantId)) {
                // 获取租户路由
                route = getTenantRoute(route, tenantId);
            }
            // 覆盖路由规则
            exchange.getAttributes().put(GATEWAY_ROUTE_ATTR, route);
        }

        return chain.filter(exchange);
    }

    /**
     * 获取租户服务集合
     *
     * @param tenantId 租户id
     */
    private List<String> getTenantServers(String tenantId) {
        List<String> tenantServers = null;
        if (tenantId != null && userTenantRepository.containsUserTenantInfo(tenantId)) {
            tenantServers = userTenantRepository.getTenantServers(tenantId);
        }
        return tenantServers;
    }

    private String tenantId(String userId) {
        if (userTenantRepository.containsUserTenantRel(userId)) {
            return userTenantRepository.getTenantId(userId);
        }
        return null;
    }

    /**
     * 获取租户路由
     */
    private Route getTenantRoute(Route route, String tenantId) {
        try {
            route = Route.async()
                    .id(route.getId())
                    // 添加租户id
                    .uri(new URI(route.getUri().toString() + "-" + tenantId))
                    .order(route.getOrder())
                    .asyncPredicate(route.getPredicate())
                    .filters(route.getFilters())
                    .metadata(route.getMetadata()).build();

            log.debug("多租户智能路由切换：{}", route);
        } catch (Exception e) {
            log.warn("创建租户id失败，使用默认路由： {}", route, e);
        }
        return route;
    }

    @Override
    public int getOrder() {
        return RouteToRequestUrlFilter.ROUTE_TO_URL_FILTER_ORDER - 1;
    }

    /**
     * 获取字符串
     */
    public String getString(Object obj) {
        return String.valueOf(obj);
    }
}

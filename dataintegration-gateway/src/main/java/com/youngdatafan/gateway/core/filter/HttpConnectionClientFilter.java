package com.youngdatafan.gateway.core.filter;

import java.net.URI;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_SCHEME_PREFIX_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.addOriginalRequestUrl;

/**
 * 集成探索平台调度运行停止接口连接处理过滤器
 *
 * @author gavin
 * @since 2020/3/3 2:26 下午
 */
@Component
public class HttpConnectionClientFilter implements GlobalFilter, Ordered {
    /**
     * 实例id缓存key 前缀
     */
    public static final String INSTANCE_ID_CACHE_KEY_PREFIX = "instanceId_";
    private static final Log log = LogFactory.getLog(HttpConnectionClientFilter.class);
    private final RedisTemplate<String, String> redisTemplate;
   // @Autowired
   // private  SpringClientFactory loadBalancer;
    /**
     * 请求实例保持url请求参数key
     */
    @Value("${request.instance.keep.urlParamsKey:projectId}")
    private String urlParamsKey;

    @Value("${request.instance.keep.scheduleStopUrl:/scheduleExecute/stop}")
    private String scheduleStopUrl;

    public HttpConnectionClientFilter(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public int getOrder() {
        // 在ReactiveLoadBalancerClientFilter过滤器之前执行
        // org.springframework.cloud.gateway.filter.ReactiveLoadBalancerClientFilter.LOAD_BALANCER_CLIENT_FILTER_ORDER
        return 10150 - 1;
    }

    @Override
    @SuppressWarnings("Duplicates")
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        URI url = exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR);

        if (url == null || url.getScheme().startsWith("ws")
                || !url.getPath().equals(scheduleStopUrl)) {
            return chain.filter(exchange);
        }

        String instanceId = null;

        final String id = exchange.getRequest().getQueryParams().getFirst(urlParamsKey);
        if (id != null) {
            // 获取探索执行器实例id
            instanceId = redisTemplate.opsForValue().get(INSTANCE_ID_CACHE_KEY_PREFIX + id);
        }

        if (instanceId == null) {
            return chain.filter(exchange);
        }

        // preserve the original url
        addOriginalRequestUrl(exchange, url);

        String routeId = ((Route) exchange.getRequiredAttribute("org.springframework.cloud.gateway.support.ServerWebExchangeUtils.gatewayRoute")).getId();
        URI requestUrl = null;
        final String scheme = url.getScheme();
        final String query = url.getQuery();
        String path = url.getPath();
        if (query != null) {
            path = path + "?" + query;
        }

        try {
         /*   final List<Server> allServers = loadBalancer.getLoadBalancer(routeId).getAllServers();
            for (Server allServer : allServers) {
                if (((NacosServer) allServer).getMetadata().getOrDefault("instanceId", "").equals(instanceId)) {
                    requestUrl = new URI(StringUtils.join(scheme, "://", allServer.getHostPort(), path));
                    break;
                }
            }*/

            if (requestUrl != null) {
                if (log.isTraceEnabled()) {
                    log.trace("LoadBalancerClientFilter url chosen: " + requestUrl);
                }
                final Map<String, Object> attributes = exchange.getAttributes();
                attributes.put(GATEWAY_REQUEST_URL_ATTR, requestUrl);
                // 已经处理负载均衡策略了，所以置空lb前缀
                attributes.put(GATEWAY_SCHEME_PREFIX_ATTR, "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return chain.filter(exchange);
    }

}

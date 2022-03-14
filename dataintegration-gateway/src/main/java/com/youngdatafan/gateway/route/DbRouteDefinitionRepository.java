package com.youngdatafan.gateway.route;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author gavin
 * @since 2020/6/12 5:41 下午
 */
@Component
public class DbRouteDefinitionRepository implements RouteDefinitionRepository {
    private static final Logger log = LoggerFactory.getLogger(DbRouteDefinitionRepository.class);
    private final GatewayProperties gatewayProperties;
    /**
     * 本地规则
     */
    private final Set<String> localRouters = new HashSet<>();
    private volatile Map<String, RouteDefinition> routes = new ConcurrentHashMap<>();

    @Autowired
    public DbRouteDefinitionRepository(GatewayProperties gatewayProperties) {
        this.gatewayProperties = gatewayProperties;
    }

    @PostConstruct
    public void init() {
        log.info("加载配置文件中的路由规则.");
        if (gatewayProperties.getRoutes() != null) {
            for (RouteDefinition route : gatewayProperties.getRoutes()) {
                save(route);
                localRouters.add(route.getId());
            }
        }
    }

    public boolean containsKeyByLocal(String routeId) {
        return localRouters.contains(routeId);
    }

    public void save(RouteDefinition route) {
        log.info("保存路由规则：{}", route);
        routes.put(route.getId(), route);
    }

    public void delete(String routeId) {
        if (routes.containsKey(routeId)) {
            final RouteDefinition remove = routes.remove(routeId);
            log.info("删除路由规则：{}", remove);
        }
    }

    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        return route.flatMap(r -> {
            routes.put(r.getId(), r);
            return Mono.empty();
        });
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        return routeId.flatMap(id -> {
            if (routes.containsKey(id)) {
                routes.remove(id);
                return Mono.empty();
            }
            return Mono.defer(() -> Mono.error(
                    new NotFoundException("RouteDefinition not found: " + routeId)));
        });
    }

    public boolean containsKey(String id) {
        return routes.containsKey(id);
    }

    public RouteDefinition get(String id) {
        return routes.get(id);
    }

    public Collection<RouteDefinition> getRoutes() {
        return routes.values();
    }

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        return Flux.fromIterable(routes.values());
    }

    public int size() {
        return routes.size();
    }

}

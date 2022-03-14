package com.youngdatafan.gateway.route;

import com.datafan.dataintegration.core.lock.CacheLock;
import com.datafan.dataintegration.core.model.Result;
import com.datafan.dataintegration.core.util.JsonUtils;
import com.datafan.dataintegration.core.util.StatusCode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.youngdatafan.gateway.core.util.Constans;
import com.youngdatafan.gateway.route.bean.GatewayRouteStatus;
import com.youngdatafan.gateway.route.feign.GatewayRouteServiceApiClient;
import com.youngdatafan.portal.system.management.gateway.dto.DpGatewayRouteDTO;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 网关路由同步作业
 *
 * @author gavin
 * @create 2020/6/13 3:12 下午
 */
@Component
public class GatewayRouteSyncTask {
    private static final Logger log = LoggerFactory.getLogger(GatewayRouteSyncTask.class);

    private final DbRouteDefinitionRepository routeDefinitionRepository;
    private final GatewayRouteServiceApiClient gatewayRouteServiceApiClient;

    /**
     * 保存网关规则状态
     */
    private final Map<String, GatewayRouteStatus> gatewayRouteStatusMap = new ConcurrentHashMap<>();

    @Autowired
    public GatewayRouteSyncTask(DbRouteDefinitionRepository routeDefinitionRepository, GatewayRouteServiceApiClient gatewayRouteServiceApiClient) {
        this.routeDefinitionRepository = routeDefinitionRepository;
        this.gatewayRouteServiceApiClient = gatewayRouteServiceApiClient;
    }

    /**
     * dto转换成RouteDefinition对象
     */
    public static RouteDefinition dtoToRoute(DpGatewayRouteDTO dpGatewayRoute) throws URISyntaxException, java.io.IOException {
        final RouteDefinition route = new RouteDefinition();
        route.setId(dpGatewayRoute.getId());
        route.setUri(new URI(dpGatewayRoute.getUri()));

        // 过滤器
        final List<String> filter = JsonUtils.parseArray(dpGatewayRoute.getFilters(), new TypeReference<List<String>>() {
        });
        if (filter != null) {
            List<FilterDefinition> filters = new ArrayList<>(filter.size());
            for (String s : filter) {
                filters.add(new FilterDefinition(s));
            }
            route.setFilters(filters);
        }

        // Predicates
        final List<String> predicate = JsonUtils.parseArray(dpGatewayRoute.getPredicates(), new TypeReference<List<String>>() {
        });
        if (predicate != null) {
            List<PredicateDefinition> predicates = new ArrayList<>(predicate.size());
            for (String s : predicate) {
                predicates.add(new PredicateDefinition(s));
            }
            route.setPredicates(predicates);
        }

        // to json
//        route.setFilters(JsonUtils.parseArray(dpGatewayRoute.getFilters(), new TypeReference<List<FilterDefinition>>() {
//        }));
        // to json
//        route.setPredicates(JsonUtils.parseArray(dpGatewayRoute.getPredicates(), new TypeReference<List<PredicateDefinition>>() {
//        }));
        // to json
        final Map<String, Object> metadata = JsonUtils.parseObject(dpGatewayRoute.getMetadata(), new TypeReference<Map<String, Object>>() {
        });
        if (metadata != null) {
            route.setMetadata(metadata);
        }

        if (dpGatewayRoute.getOrder() != null) {
            route.setOrder(dpGatewayRoute.getOrder());
        }
        return route;
    }

    @PostConstruct
    public void init() {
        try {
            final Result<List<DpGatewayRouteDTO>, Object> listObjectResult = gatewayRouteServiceApiClient.selectAll();
            if (listObjectResult == null || !(listObjectResult.getCode().equals(StatusCode.CODE_10000.getCode())) ||
                    listObjectResult.getContent() == null) {
                return;
            }

            for (DpGatewayRouteDTO dpGatewayRoute : listObjectResult.getContent()) {
                try {
                    // 转换DTO并且保存规则
                    convertAndSave(dpGatewayRoute);
                } catch (Exception e) {
                    log.warn("init error route dto: {}", dpGatewayRoute, e);
                }
            }
        } catch (Exception e) {
            log.warn("初始化数据库规则失败，等待后续同步任务处理规则状态。", e);
        }
    }

    @Scheduled(initialDelay = 4000, fixedDelayString = "${dp.gateway.route.sync.fixedDelay:60000}")
    @CacheLock(key = Constans.LOCK_GATEWAY_ROUTE, expire = 60)
    public void syncRoute() {
        final long timeMillis = System.currentTimeMillis();

        try {
            sync();
            log.info("start syncRoute ,规则记录数：{} ,耗时: {}", routeDefinitionRepository.size()
                    , (System.currentTimeMillis() - timeMillis));
        } catch (Exception e) {
            log.error("规则同步异常", e);
        }
    }

    private void sync() {
        final Result<List<DpGatewayRouteDTO>, Object> listObjectResult = gatewayRouteServiceApiClient.selectAll();
        if (listObjectResult == null || !(listObjectResult.getCode().equals(StatusCode.CODE_10000.getCode())) ||
                listObjectResult.getContent() == null) {
            return;
        }

        for (DpGatewayRouteDTO dpGatewayRouteDTO : listObjectResult.getContent()) {
            try {
                syncDbRateLimit(dpGatewayRouteDTO);
            } catch (Exception e) {
                log.warn("save error route dto: {}", dpGatewayRouteDTO, e);
            }
        }

        // list to map
        final Map<String, DpGatewayRouteDTO> routeMap = listObjectResult.getContent().stream().collect(Collectors.toMap(DpGatewayRouteDTO::getId
                , DpGatewayRouteDTO -> DpGatewayRouteDTO));

        for (RouteDefinition route : routeDefinitionRepository.getRoutes()) {
            try {
                syncMemoryRateLimit(routeMap, route);
            } catch (Exception e) {
                log.warn("save error route config: {}", route, e);
            }
        }

        // 清理过期的状态
        for (Map.Entry<String, GatewayRouteStatus> stringGatewayRouteStatusEntry : gatewayRouteStatusMap.entrySet()) {
            if ((System.currentTimeMillis() - stringGatewayRouteStatusEntry.getValue().getLastAccessTime()) / 1000 > 300) {
                gatewayRouteStatusMap.remove(stringGatewayRouteStatusEntry.getKey());
            }
        }
    }

    private void syncMemoryRateLimit(Map<String, DpGatewayRouteDTO> routeMap, RouteDefinition route) {
        final String routeId = route.getId();
        if (routeDefinitionRepository.containsKeyByLocal(routeId)) {
            // 忽略本地规则配置
            return;
        }

        // //数据库没有，内存规则里有，则保存状态
        if (!routeMap.containsKey(routeId)) {

            final GatewayRouteStatus gatewayRouteStatus = saveStatus(routeId);
            // 上次都不一致，或者上次访问时间超过5分组，则删除规则
            if (gatewayRouteStatus.getNonExistentCount() > 3
                    || (System.currentTimeMillis() - gatewayRouteStatus.getLastAccessTime()) / 1000 > 300) {

                // 删除内存中规则
                routeDefinitionRepository.delete(routeId);
                gatewayRouteStatusMap.remove(routeId);
            }
        }
    }

    private void syncDbRateLimit(DpGatewayRouteDTO dpGatewayRouteDTO) throws URISyntaxException, IOException {
        final String routeId = dpGatewayRouteDTO.getId();

        if (routeDefinitionRepository.containsKey(routeId)) {
            //数据库规则更新，则更新内存中规则
            final RouteDefinition routeDefinition = routeDefinitionRepository.get(routeId);
            final RouteDefinition routeDefinition1 = dtoToRoute(dpGatewayRouteDTO);
            if (!routeDefinition.equals(routeDefinition1)) {
                log.info("捕获到路由规则更新：{}", routeDefinition1);
                routeDefinitionRepository.save(routeDefinition1);
            }

        } else {
            // 保存规则
            convertAndSave(dpGatewayRouteDTO);
        }
    }

    /**
     * 保存状态
     */
    private GatewayRouteStatus saveStatus(String routeId) {
        GatewayRouteStatus status;
        if (gatewayRouteStatusMap.containsKey(routeId)) {
            status = gatewayRouteStatusMap.get(routeId);
            status.addNonExistentCount();
        } else {
            status = new GatewayRouteStatus(1);
            gatewayRouteStatusMap.put(routeId, status);
        }
        status.setLastAccessTime(System.currentTimeMillis());

        return status;
    }

    /**
     * 转换DTO并且保存规则
     *
     * @param dpGatewayRoute DpGatewayRouteDTO
     */
    private void convertAndSave(DpGatewayRouteDTO dpGatewayRoute) throws IOException, URISyntaxException {
        final RouteDefinition route = dtoToRoute(dpGatewayRoute);

        // 保存规则
        routeDefinitionRepository.save(route);
    }

}

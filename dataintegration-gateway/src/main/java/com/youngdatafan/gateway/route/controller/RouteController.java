package com.youngdatafan.gateway.route.controller;

import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.gateway.route.DbRouteDefinitionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gavin
 * @since 2020/7/29 3:32 下午
 */
@RestController
@RequestMapping("/actuator/dp")
public class RouteController {

    private final DbRouteDefinitionRepository routeDefinitionRepository;

    @Autowired
    public RouteController(DbRouteDefinitionRepository routeDefinitionRepository) {
        this.routeDefinitionRepository = routeDefinitionRepository;
    }

    @GetMapping("/routes")
    public Result routers() {
        return Result.success(routeDefinitionRepository.getRoutes());
    }

}

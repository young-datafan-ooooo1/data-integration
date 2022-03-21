package com.youngdatafan.gateway.ratelimit.controller;

import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.gateway.ratelimit.DbRateLimitRepository;
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
public class RateLimitController {

    private final DbRateLimitRepository routeDefinitionRepository;

    @Autowired
    public RateLimitController(DbRateLimitRepository routeDefinitionRepository) {
        this.routeDefinitionRepository = routeDefinitionRepository;
    }

    @GetMapping("/rateLimits")
    public Result rateLimits() {
        return Result.success(routeDefinitionRepository.getRoutes());
    }

}

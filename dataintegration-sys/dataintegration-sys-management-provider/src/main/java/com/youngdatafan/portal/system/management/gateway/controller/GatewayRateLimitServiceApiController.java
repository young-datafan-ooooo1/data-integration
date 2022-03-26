package com.youngdatafan.portal.system.management.gateway.controller;

import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.portal.system.management.gateway.api.GatewayRateLimitServiceApi;
import com.youngdatafan.portal.system.management.gateway.dto.DpGatewayRateLimitDTO;
import com.youngdatafan.portal.system.management.gateway.service.GatewayRateLimitService;
import com.youngdatafan.portal.system.management.gateway.vo.DpGatewayRateLimitVO;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * GatewayRateLimitServiceApiController.
 *
 * @author gavin
 * @since 2020/6/13 1:45 下午
 */
@RestController
@RequestMapping("/gateway/rateLimit")
public class GatewayRateLimitServiceApiController implements GatewayRateLimitServiceApi {

    private final GatewayRateLimitService gatewayRateLimitService;

    @Autowired
    public GatewayRateLimitServiceApiController(GatewayRateLimitService gatewayRateLimitService) {
        this.gatewayRateLimitService = gatewayRateLimitService;
    }

    @Override
    public Result<DpGatewayRateLimitDTO, Object> upsert(DpGatewayRateLimitVO dpGatewayRouteVO) {
        return Result.success(gatewayRateLimitService.upsert(dpGatewayRouteVO));
    }

    @Override
    public Result<Integer, Object> delete(String id) {
        return Result.success(gatewayRateLimitService.deleteByPrimaryKey(id));
    }

    @Override
    public Result<List<DpGatewayRateLimitDTO>, Object> selectAll() {
        return Result.success(gatewayRateLimitService.selectAll());
    }
}

package com.youngdatafan.portal.system.management.gateway.controller;

import com.datafan.dataintegration.core.model.Result;
import com.youngdatafan.portal.system.management.gateway.api.GatewayRouteServiceApi;
import com.youngdatafan.portal.system.management.gateway.dto.DpGatewayRouteDTO;
import com.youngdatafan.portal.system.management.gateway.service.GatewayRouteService;
import com.youngdatafan.portal.system.management.gateway.vo.DpGatewayRouteVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author gavin
 * @create 2020/6/13 1:45 下午
 */
@RestController
@RequestMapping("/gateway/route")
public class GatewayRouteServiceApiController implements GatewayRouteServiceApi {

    private final GatewayRouteService gatewayRouteService;

    @Autowired
    public GatewayRouteServiceApiController(GatewayRouteService gatewayRouteService) {
        this.gatewayRouteService = gatewayRouteService;
    }

    @Override
    public Result<DpGatewayRouteDTO, Object> upsert(DpGatewayRouteVO dpGatewayRouteVO) {
        return Result.success(gatewayRouteService.upsert(dpGatewayRouteVO));
    }

    @Override
    public Result<Integer, Object> delete(String id) {
        return Result.success(gatewayRouteService.deleteByPrimaryKey(id));
    }

    @Override
    public Result<List<DpGatewayRouteDTO>, Object> selectAll() {
        return Result.success(gatewayRouteService.selectAll());
    }
}

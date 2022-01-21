package com.youngdatafan.portal.system.management.gateway.api;

import com.datafan.dataintegration.core.model.Result;
import com.youngdatafan.portal.system.management.gateway.dto.DpGatewayRateLimitDTO;
import com.youngdatafan.portal.system.management.gateway.vo.DpGatewayRateLimitVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: gavin
 */
@Api(tags = "网关限流API接口")
public interface GatewayRateLimitServiceApi {

    @ApiOperation(value = "添加或修改规则", produces = "application/json")
    @PostMapping(value = "/upsert")
    Result<DpGatewayRateLimitDTO, Object> upsert(@RequestBody DpGatewayRateLimitVO dpGatewayRouteVO);

    @ApiOperation(value = "删除规则", produces = "application/json")
    @DeleteMapping(value = "/delete/{id}")
    Result<Integer, Object> delete(@PathVariable("id") String id);

    @ApiOperation(value = "查询所有有效规则", produces = "application/json")
    @GetMapping(value = "/selectAll")
    Result<List<DpGatewayRateLimitDTO>, Object> selectAll();

}

package com.youngdatafan.portal.system.management.gateway.api;

import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.portal.system.management.gateway.dto.DpGatewayRouteDTO;
import com.youngdatafan.portal.system.management.gateway.vo.DpGatewayRouteVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: gavin
 */
@Api(tags = "网关路由API接口")
public interface GatewayRouteServiceApi {

    @ApiOperation(value = "添加或修改路由", produces = "application/json")
    @PostMapping(value = "/upsert")
    Result<DpGatewayRouteDTO, Object> upsert(@RequestBody DpGatewayRouteVO dpGatewayRouteVO);

    @ApiOperation(value = "删除路由", produces = "application/json")
    @DeleteMapping(value = "/delete/{id}")
    Result<Integer, Object> delete(@PathVariable("id") String id);

    @ApiOperation(value = "查询所有有效规则", produces = "application/json")
    @GetMapping(value = "/selectAll")
    Result<List<DpGatewayRouteDTO>, Object> selectAll();

}

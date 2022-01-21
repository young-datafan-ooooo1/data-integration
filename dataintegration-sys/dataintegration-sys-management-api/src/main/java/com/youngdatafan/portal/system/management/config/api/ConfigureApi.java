package com.youngdatafan.portal.system.management.config.api;

import com.datafan.dataintegration.core.model.Result;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

/**
 * @Author: jeremychen
 * @Descripition:
 * @Date:2020/5/14 5:55 下午
 */
public interface ConfigureApi {
    @ApiOperation(value = "获取前端配置", notes = "从配置中心获取前端配置", produces = "application/json")
    @GetMapping(value = "/getVueConfig")
    Result<Map<String, String>, Object> getVueConfig(@ApiParam("操作用户Id") @RequestHeader("authorization-userId") String userId, @ApiParam("操作用户Id") @RequestHeader("authorization-userName") String userName);

    @ApiOperation(value = "获取登录接口地址", notes = "获取登录接口地址", produces = "application/json")
    @GetMapping(value = "/getssoUrl")
    Result<String, Object> getssoUrl();
}

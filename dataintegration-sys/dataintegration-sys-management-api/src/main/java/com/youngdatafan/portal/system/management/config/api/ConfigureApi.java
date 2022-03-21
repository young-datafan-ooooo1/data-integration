package com.youngdatafan.portal.system.management.config.api;

import com.youngdatafan.dataintegration.core.model.Result;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * 获取前端配置.
 */
public interface ConfigureApi {
    /**
     * 获取前端配置.
     *
     * @param userId   用户id
     * @param userName 用户名称
     * @return Map
     */
    @ApiOperation(value = "获取前端配置", notes = "从配置中心获取前端配置", produces = "application/json")
    @GetMapping(value = "/getVueConfig")
    Result<Map<String, String>, Object> getVueConfig(@ApiParam("操作用户Id") @RequestHeader("authorization-userId") String userId,
                                                     @ApiParam("操作用户Id") @RequestHeader("authorization-userName") String userName);

    /**
     * 获取登录接口地址.
     *
     * @return String
     */
    @ApiOperation(value = "获取登录接口地址", notes = "获取登录接口地址", produces = "application/json")
    @GetMapping(value = "/getssoUrl")
    Result<String, Object> getssoUrl();
}

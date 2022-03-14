package com.youngdatafan.gateway.ratelimit.feign;


import com.youngdatafan.portal.system.management.gateway.api.GatewayRateLimitServiceApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 网关限流接口客户端.
 *
 * @author gavin
 * @since 2020/6/13 3:10 下午
 */
@FeignClient(name = "dataintegration-portal-system-management-provider", contextId = "portal-system-management-provider-rateLimit",path = "/gateway/rateLimit")
public interface GatewayRateLimitServiceApiClient extends GatewayRateLimitServiceApi {
}

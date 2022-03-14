package com.youngdatafan.gateway.log.feign;

import com.youngdatafan.portal.system.management.log.api.LogServiceApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 日志接口客户端.
 *
 * @author gavin
 */
@FeignClient(value = "dataintegration-portal-system-management-provider", contextId = "portal-system-management-provider-log", path = "/log")
public interface LogServiceApiClient extends LogServiceApi {
}

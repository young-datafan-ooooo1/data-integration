package com.youngdatafan.common.sso.feign;

import com.youngdatafan.portal.system.management.user.api.RoleServiceApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "dataintegration-portal-system-management-provider", path = "/role")
public interface RoleServiceApiClient extends RoleServiceApi {
}

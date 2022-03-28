package com.youngdatafan.common.sso.feign;

import com.youngdatafan.portal.system.management.user.api.UserServiceApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * UserServiceApiClient.
 */
@FeignClient(value = "dataintegration-portal-system-management-provider", path = "/user")
public interface UserServiceApiClient extends UserServiceApi {
}

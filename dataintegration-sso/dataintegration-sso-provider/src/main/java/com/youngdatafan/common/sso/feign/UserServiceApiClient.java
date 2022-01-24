package com.youngdatafan.common.sso.feign;

import com.youngdatafan.portal.system.management.user.api.UserServiceApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Author: jeremychen
 * @Descripition:
 * @Date:2020/3/13 2:21 下午
 */
@FeignClient(value = "dataintegration-portal-system-management-provider", path = "/user")
public interface UserServiceApiClient extends UserServiceApi {
}

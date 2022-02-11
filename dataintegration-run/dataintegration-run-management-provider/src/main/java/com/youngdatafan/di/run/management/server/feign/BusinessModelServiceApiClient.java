package com.youngdatafan.di.run.management.server.feign;

import com.youngdatafan.portal.model.management.businessmodel.api.BusinessModelServiceApi;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;


/**
 * @Author: jeremychen
 * @Descripition:
 * @Date:2020/2/19 3:09 下午
 */
@Component
@FeignClient(value = "dp-portal-model-management-provider",path = "/businessModel")
public interface BusinessModelServiceApiClient extends BusinessModelServiceApi {


}

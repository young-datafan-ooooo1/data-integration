package com.youngdatafan.di.run.management.server.feign;

import com.youngdatafan.portal.common.project.api.ProjectServiceApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 项目管理API接口 客户端
 *
 * @author gavin
 * @create 2020-02-19 16:33:31
 */
@FeignClient(value = "dp-common-project-provider", path = "/project")
public interface ProjectServiceApiClient extends ProjectServiceApi {

}

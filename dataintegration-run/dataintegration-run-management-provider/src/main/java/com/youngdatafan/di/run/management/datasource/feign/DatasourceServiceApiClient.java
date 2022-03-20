package com.youngdatafan.di.run.management.datasource.feign;

import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.portal.model.management.datasource.dto.DatasourceDTO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author gavin
 * @since 2020/2/21 3:00 下午
 */
@FeignClient(value = "dataintegration-portal-model-management-provider", path = "/datasource")
public interface DatasourceServiceApiClient {

    @GetMapping({"/datasourceName/{datasourceName}"})
    Result<DatasourceDTO, Object> getDatasourceByName(@ApiParam("数据源名称") @PathVariable("datasourceName") String var1);

    @GetMapping({"/datasourceId/{datasourceId}"})
    Result<DatasourceDTO, Object> getDatasourceById(@ApiParam("用户id") @RequestHeader(value = "authorization-userId",required = false) String var1, @ApiParam("数据源id") @PathVariable("datasourceId") String var2);


    @ApiOperation(value = "获取存储过程")
    @PostMapping(value = "/getProcedure")
    Result<String[],Object> getProcedure(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false)String userId,@RequestParam("databaseId") String databaseId);
}

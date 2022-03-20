package com.youngdatafan.di.run.management.steps.dblookup.api;

import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.di.run.management.steps.dblookup.vo.TableFieldVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.pentaho.di.core.exception.KettleException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;

@Api(tags = "csv文件输入接口")
public interface DbLookupApi {

    @ApiOperation(value = "获取字段", produces = "application/json")
    @PostMapping(value = "/getTableColumns")
    Result<List<TableFieldVO>, Object> getTableColumns(@RequestHeader("authorization-userId") String userId
            , @RequestBody String json, @RequestParam String tableName, @RequestParam String connectName, @RequestParam String schema) throws KettleException, IOException;

}

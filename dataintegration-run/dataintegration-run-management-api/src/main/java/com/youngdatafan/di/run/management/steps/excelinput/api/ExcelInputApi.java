package com.youngdatafan.di.run.management.steps.excelinput.api;

import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.di.run.management.steps.excelinput.vo.SheetFieldVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.pentaho.di.core.exception.KettleFileException;
import org.pentaho.di.core.exception.KettleXMLException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.io.IOException;
import java.util.List;

@Api(tags = "excel输入接口")
public interface ExcelInputApi {

    @ApiOperation(value = "获取来自头部数据的字段", produces = "application/json")
    @PostMapping(value = "/getSheetHeaderColumn")
    Result<List<SheetFieldVO>, Object> getSheetHeaderColumn(@RequestHeader("authorization-userId") String userId
            , @RequestBody String json) throws KettleXMLException, IOException, KettleFileException;

    @ApiOperation(value = "获取工作表名称", produces = "application/json")
    @PostMapping(value = "/getSheetName")
    Result<List<String>, Object> getSheetName(@RequestHeader("authorization-userId") String userId
            , @RequestBody String json) throws KettleXMLException, IOException, KettleFileException;

}

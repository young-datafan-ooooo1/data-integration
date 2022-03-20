package com.youngdatafan.di.run.management.server.api;

import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.di.run.management.server.dto.FunctionCategoryDTO;
import com.youngdatafan.di.run.management.server.dto.PluginCategoryDTO;
import com.youngdatafan.di.run.management.server.dto.PluginInfoDTO;
import com.youngdatafan.di.run.management.server.vo.CreateTableVO;
import com.youngdatafan.di.run.management.server.vo.ExecuteDDLVO;
import com.youngdatafan.di.run.management.server.vo.ExecuteFieldSumVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: jeremychen
 * @Descripition:
 * @Date:2020/2/18 3:46 下午
 */
@Api(tags = "探索插件管理接口")
public interface PluginManageServiceApi {

    @GetMapping(value = "/init")
    Result init();

    @ApiOperation(value = "根据用户id获取基本插件", notes = "根据用户id获取基本插件", produces = "application/json")
    @GetMapping(value = "/getAllBasicPluginInfoByUserId")
    Result<List<PluginInfoDTO<PluginInfoDTO>>,Object> getAllBasicPluginInfoByUserId(@RequestHeader("authorization-userId") String userId);

    @ApiOperation(value = "根据用户id获取业务插件", notes = "根据用户id获取业务插件", produces = "application/json")
    @GetMapping(value = "/getAllBusinessPluginInfoByUserId")
    Result<List<PluginCategoryDTO>, Object> getAllBusinessPluginInfoByUserId(@RequestHeader("authorization-userId") String userId, @RequestParam("pluginIds") String pluginIds);


    @ApiOperation(value = "根据用户id获取业务模型插件", notes = "根据用户id获取业务模型插件", produces = "application/json")
    @GetMapping(value = "/getAllBusinessModelPluginInfoByUserId")
    Result<List<PluginCategoryDTO>, Object> getAllBusinessModelPluginInfoByUserId(@RequestHeader("authorization-userId") String userId);


    @ApiOperation(value = "获取公式详情信息", notes = "获取公式详情信息", produces = "application/json")
    @GetMapping(value = "/getFuncDescribe")
    Result<List<FunctionCategoryDTO>, Object> getFuncDescribe(@RequestHeader("authorization-userId") String userId);


    @ApiOperation(value = "生成建表语句", notes = "根据传入字段生成建表语句", produces = "application/json")
    @PostMapping(value = "/getCreateTableDDL")
    Result getCreateTableDDL(@RequestHeader("authorization-userId") String userId, @RequestBody CreateTableVO createTableVO);

    @ApiOperation(value = "建表", notes = "根据传入字段建表", produces = "application/json")
    @PostMapping(value = "/executeCreateDDL")
    Result executeCreateDDL(@RequestHeader("authorization-userId") String userId, @RequestBody ExecuteDDLVO executeDDLVO);

    @ApiOperation(value = "获取字段统计信息", notes = "获取字段统计信息", produces = "application/json")
    @PostMapping(value = "/getFieldSumInfo")
    Result getFieldSumInfo(@RequestHeader("authorization-userId") String userId, @RequestHeader("authorization-userName") String userName, @Validated @RequestBody ExecuteFieldSumVO executeFieldSumVO);

    @ApiOperation(value = "获取字段最大最小值", notes = "获取字段最大最小值", produces = "application/json")
    @PostMapping(value = "/getFieldMaxMinInfo")
    Result getFieldMaxMinInfo(@RequestHeader("authorization-userId") String userId, @RequestHeader("authorization-userName") String userName, @Validated @RequestBody ExecuteFieldSumVO executeFieldSumVO);

    @ApiOperation(value = "获取之前步骤sql", notes = "获取字段最大最小值", produces = "application/json")
    @PostMapping(value = "/getPreStepSql")
    Result getPreStepSql(@RequestHeader("authorization-userId") String userId, @RequestHeader("authorization-userName") String userName, @Validated @RequestBody ExecuteFieldSumVO executeFieldSumVO);
}

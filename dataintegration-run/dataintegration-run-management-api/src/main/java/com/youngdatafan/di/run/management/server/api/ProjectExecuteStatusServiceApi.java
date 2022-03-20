package com.youngdatafan.di.run.management.server.api;

import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.di.run.management.server.dto.ProjectHistoryExecuteDTO;
import com.youngdatafan.di.run.management.server.vo.ProjectHistoryExecuteVO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author gavin
 * @since 2020/3/5 10:18 上午
 */
@Api(tags = "项目运行状态")
public interface ProjectExecuteStatusServiceApi {

    @ApiOperation(value = "查询正在运行的项目", produces = "application/json")
    @GetMapping(value = "/selectRunningProject")
    Result<List<ProjectHistoryExecuteDTO>, Object> getRunningProject(@RequestHeader("authorization-userId") String userId);

    @ApiOperation(value = "查询历史执行", notes = "根据用户id获取基本插件", produces = "application/json")
    @PostMapping(value = "/selectUserHistoryExecute")
    Result<PageInfo<ProjectHistoryExecuteDTO>, Object> selectUserHistoryExecute(@RequestHeader("authorization-userId") String userId
            , @Validated @RequestBody ProjectHistoryExecuteVO projectHistoryExecuteVO
            , @ApiParam("页数") @RequestParam int pageNum, @ApiParam("每页记录数") @RequestParam int pageSize);


}

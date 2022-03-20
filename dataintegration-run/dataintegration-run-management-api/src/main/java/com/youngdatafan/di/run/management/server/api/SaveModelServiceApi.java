package com.youngdatafan.di.run.management.server.api;

import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.di.run.management.server.dto.SavemodelHistoryDTO;
import com.youngdatafan.di.run.management.server.vo.SaveModelVO;
import com.youngdatafan.di.run.management.server.vo.SaveToModelVO;
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
@Api(tags = "保存模型接口")
public interface SaveModelServiceApi {

    @ApiOperation(value = "保存模型", produces = "application/json")
    @PostMapping(value = "/save")
    Result<Boolean, Object> save(@RequestHeader("authorization-userId") String userId
            , @RequestHeader("authorization-userName") String userName
            , @Validated @RequestBody SaveToModelVO saveToModelVO);

    @ApiOperation(value = "查询正在保存的模型", produces = "application/json")
    @GetMapping(value = "/selectRunning")
    Result<List<SavemodelHistoryDTO>, Object> getRunningProject(@RequestHeader("authorization-userId") String userId);

    @ApiOperation(value = "查询保存模型历史", produces = "application/json")
    @PostMapping(value = "/selectHistory")
    Result<PageInfo<SavemodelHistoryDTO>, Object> selectUserHistoryExecute(@RequestHeader("authorization-userId") String userId
            , @Validated @RequestBody SaveModelVO saveModelVO
            , @ApiParam("页数") @RequestParam int pageNum, @ApiParam("每页记录数") @RequestParam int pageSize);

    @ApiOperation(value = "删除模型", produces = "application/json")
    @DeleteMapping(value = "/delete/{modelId}")
    Result<Boolean, Object> delete(@RequestHeader("authorization-userId") String userId
            , @PathVariable("modelId") Long[] ids);

}

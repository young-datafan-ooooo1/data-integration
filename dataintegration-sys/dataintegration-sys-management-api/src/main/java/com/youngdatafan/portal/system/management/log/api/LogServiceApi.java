package com.youngdatafan.portal.system.management.log.api;

import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.portal.system.management.log.dto.LogDTO;
import com.youngdatafan.portal.system.management.log.vo.LogVO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

/**
 * @Author: jeremychen
 * @Descripition:
 * @Date:2020/2/12 1:54 下午
 */
@Api(tags = "日志管理API接口")
public interface LogServiceApi {

    @ApiOperation(value = "添加日志", notes = "添加日志", produces = "application/json")
    @PostMapping(value = "/add")
    Result<LogDTO, Object> add(@RequestBody LogVO logVO);

    @ApiOperation(value = "批量添加日志", notes = "批量添加日志", produces = "application/json")
    @PostMapping(value = "/batchAdd")
    Result<Integer, Object> batchAdd(@RequestBody List<LogVO> logs);

    @ApiOperation(value = "根据日期分页查询日志", notes = "根据日期分页查询日志", produces = "application/json")
    @GetMapping(value = "/getLogsByReqTimePage")
    Result<PageInfo<LogDTO>, Object> getLogsByReqTimePage(@RequestParam("pageSize") @ApiParam("每页条数") String pageSize, @RequestParam("curPage") @ApiParam("当前页") String curPage
            , @ApiParam(value = "开始时间", required = true) @RequestParam("startDate") Date startDate
            , @ApiParam(value = "结束时间", required = true) @RequestParam("endDate") Date endDate
            , @ApiParam(value = "用户名") @RequestParam(value = "userName", required = false) String userName);

}

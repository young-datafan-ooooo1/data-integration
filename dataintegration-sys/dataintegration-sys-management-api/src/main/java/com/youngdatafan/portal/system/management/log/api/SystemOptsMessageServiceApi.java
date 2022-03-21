package com.youngdatafan.portal.system.management.log.api;

import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.portal.system.management.log.vo.SystemOptsMessageVo;
import com.youngdatafan.portal.system.management.summary.dto.SystemOptsMessageDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * 系统操作消息接口.
 */
@Api(tags = "系统操作消息接口")
public interface SystemOptsMessageServiceApi {

    /**
     * 消息推送.
     *
     * @param systemOptsMessageVo systemOptsMessageVo
     * @return SystemOptsMessageDTO
     */
    @ApiOperation(value = "添加推送消息", notes = "添加推送消息", produces = "application/json")
    @PostMapping(value = "/add")
    Result<SystemOptsMessageDTO, Object> add(@Validated @RequestBody SystemOptsMessageVo systemOptsMessageVo);

    /**
     * 获取推送消息.
     *
     * @param userId userId
     * @return List
     */
    @ApiOperation(value = "获取推送消息", notes = "获取推送消息", produces = "application/json")
    @GetMapping(value = "/selectLast7DayMessage")
    Result<List<SystemOptsMessageDTO>, Object> selectLast7DayMessage(@ApiParam("操作用户Id") @RequestHeader("authorization-userId") String userId);
}

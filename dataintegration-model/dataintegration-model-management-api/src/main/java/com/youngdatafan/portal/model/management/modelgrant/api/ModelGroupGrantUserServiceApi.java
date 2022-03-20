package com.youngdatafan.portal.model.management.modelgrant.api;

import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.portal.model.management.modelgrant.dto.AddUserGrantGroupListDTO;
import com.youngdatafan.portal.model.management.modelgrant.vo.AddModelGroupGrantUserVO;
import com.youngdatafan.portal.model.management.modelgrant.vo.UpdateModelGroupGrantUserVO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>****************************************************************************</p>
 * <ul style="margin:15px;">
 * <li>Description : description</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2020/2/10 4:31 PM</li>
 * <li>Author      : ksice_xt</li>
 * </ul>
 * <p>****************************************************************************</p>
 */
public interface ModelGroupGrantUserServiceApi {

    @ApiOperation(value = "添加用户授权", notes = "添加用户授权信息的【用户名(PK)\n" +
            "是否有效\n" +
            "备注\n" +
            "关联的授权组id" +
            "关联的模型id】", produces = "application/json")
    @PostMapping(value = "/add")
        //todo:用户id到时候要修改
    Result add(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
               @RequestBody AddModelGroupGrantUserVO addModelGroupGrantUserVO);


    @ApiOperation(value = "修改用户授权", notes = "添加用户授权信息的【用户名(PK)\n" +
            "是否有效\n" +
            "备注\n" +
            "关联的授权组id" +
            "关联的模型id】", produces = "application/json")
    @PutMapping(value = "/update")
        //todo:用户id到时候要修改
    Result update(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                  @RequestBody UpdateModelGroupGrantUserVO updateModelGroupGrantUserVO);


    @ApiOperation(value = "获取用户授权信息", notes = "获取用户授权信息", produces = "application/json")
    @GetMapping(value = "/selectAll")
        //todo:用户id到时候要修改
    Result selectAll(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                     @ApiParam("用户名") @RequestParam(value = "userName", required = false) String userName,
                     @ApiParam("页码") @RequestParam(value = "curPage", defaultValue = "1") Integer curPage,
                     @ApiParam("行数") @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                     @ApiParam("授权组名称") @RequestParam(value = "modelGrantGroupName", required = false) String modelGrantGroupName);

    @ApiOperation(value = "删除用户授权信息", notes = "删除用户授权信息", produces = "application/json")
    @DeleteMapping(value = "/delete/{userGrantId}")
        //todo:用户id到时候要修改
    Result delete(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                  @ApiParam("用户授权id") @PathVariable("userGrantId") String userGrantId);


    @ApiOperation(value = "查询用户信息", notes = "查询用户姓名和id", produces = "application/json")
    @GetMapping(value = "/selectAll/users")
        //todo:用户id到时候要修改
    Result getUserIdAndName(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId);

    @ApiOperation(value = "查询授权组信息", notes = "查询用户姓名和id", produces = "application/json")
    @GetMapping(value = "/selectAll/modelGrantGroups")
        //todo:用户id到时候要修改
    Result<List<AddUserGrantGroupListDTO>, Object> getModelGrantGroup(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                                                                      @ApiParam("模型授权组") @RequestParam(value = "modelGroupName", required = false) String modelGroupName);


}


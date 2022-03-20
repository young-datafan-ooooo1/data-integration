package com.youngdatafan.portal.model.management.modelgrant.api;

import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.portal.model.management.modelgrant.dto.ModelNameAndGroupNameAndTypes;
import com.youngdatafan.portal.model.management.modelgrant.vo.AddModelGroupVO;
import com.youngdatafan.portal.model.management.modelgrant.vo.UpdateModelGroupVO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

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
public interface ModelGroupServiceApi {

    @ApiOperation(value = "添加授权组信息", notes = "添加数据源的【授权组名称(PK)\n" +
            "授权组描述\n" +
            "是否有效\n" +
            "关联的模型id】", produces = "application/json")
    @PostMapping(value = "/add")
        //todo:用户id到时候要修改
    Result add(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
               @RequestBody AddModelGroupVO addModelGroupVO);


    @ApiOperation(value = "删除授权组", notes = "根据授权组名称删除授权组", produces = "application/json")
    @DeleteMapping(value = "/delete/{modelGrantGroupName}")
    Result delete(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                  @ApiParam("模型授权组名称") @PathVariable("modelGrantGroupName") String modelGrantGroupName);

    @ApiOperation(value = "添加授权组信息", notes = "添加数据源的【授权组名称(PK)\n" +
            "授权组描述\n" +
            "是否有效\n" +
            "关联的模型id】", produces = "application/json")
    @PutMapping(value = "/update")
        //todo:用户id到时候要修改
    Result update(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                  @RequestBody UpdateModelGroupVO updateModelGroupVO);


    @ApiOperation(value = "查询授权组信息", notes = "根据用户id查询授权组信息进行分页筛选", produces = "application/json")
    @GetMapping(value = "/selectAll")
        //todo:用户id到时候要修改
    Result selectAll(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                     @ApiParam("页码") @RequestParam(value = "curPage", defaultValue = "1") Integer curPage,
                     @ApiParam("行数") @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                     @ApiParam("授权组名称") @RequestParam(value = "modelGrantGroupName", required = false) String modelGrantGroupName);


    @ApiOperation(value = "查询业务模型名称", notes = "查询所有的业务模型和授权组进行绑定", produces = "application/json")
    @GetMapping(value = "/selectAll/businessModel")
        //todo:用户id到时候要修改
    Result<ModelNameAndGroupNameAndTypes, Object> selectAllBusinessModel(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                                                                         @ApiParam("模型名称") @RequestParam(value = "modelName", required = false) String modelName,
                                                                         @ApiParam("模型分组名称") @RequestParam(value = "modelGroupName", required = false) String modelGroupNam,
                                                                         @ApiParam("模型分组类型") @RequestParam(value = "modelGroupType", required = false) String modelGroupType,
                                                                         @ApiParam("页码") @RequestParam(value = "curPage", defaultValue = "1") Integer curPage,
                                                                         @ApiParam("行数") @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize);

    @ApiOperation(value = "查询授权组名称是否重复", notes = "根据授权组名称查询是否重复", produces = "application/json")
    @GetMapping(value = "/queryModelGroupNameIsExits")
        //todo:用户id到时候要修改
    Result<Boolean, Object> queryModelGroupNameIsExits(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                                                       @ApiParam("授权组名称") @RequestParam(value = "modelGroupName", required = false) String modelGroupName);


}

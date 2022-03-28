package com.youngdatafan.portal.model.management.modelgrant.api;

import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.portal.model.management.modelgrant.dto.ModelNameAndGroupNameAndTypes;
import com.youngdatafan.portal.model.management.modelgrant.vo.AddModelGroupVO;
import com.youngdatafan.portal.model.management.modelgrant.vo.UpdateModelGroupVO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * ModelGroupServiceApi.
 */
public interface ModelGroupServiceApi {

    /**
     * 添加授权组信息.
     *
     * @param userId          userId
     * @param addModelGroupVO addModelGroupVO
     * @return Result
     */
    @PostMapping(value = "/add")
    Result add(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
               @RequestBody AddModelGroupVO addModelGroupVO);

    /**
     * 删除授权组.
     *
     * @param userId              userId
     * @param modelGrantGroupName modelGrantGroupName
     * @return Result
     */
    @ApiOperation(value = "删除授权组", notes = "根据授权组名称删除授权组", produces = "application/json")
    @DeleteMapping(value = "/delete/{modelGrantGroupName}")
    Result delete(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                  @ApiParam("模型授权组名称") @PathVariable("modelGrantGroupName") String modelGrantGroupName);

    /**
     * 添加授权组信息.
     *
     * @param userId             userId
     * @param updateModelGroupVO updateModelGroupVO
     * @return Result
     */
    @PutMapping(value = "/update")
    Result update(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                  @RequestBody UpdateModelGroupVO updateModelGroupVO);

    /**
     * 查询授权组信息.
     *
     * @param userId              userId
     * @param curPage             curPage
     * @param pageSize            pageSize
     * @param modelGrantGroupName modelGrantGroupName
     * @return Result
     */
    @ApiOperation(value = "查询授权组信息", notes = "根据用户id查询授权组信息进行分页筛选", produces = "application/json")
    @GetMapping(value = "/selectAll")
    Result selectAll(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                     @ApiParam("页码") @RequestParam(value = "curPage", defaultValue = "1") Integer curPage,
                     @ApiParam("行数") @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                     @ApiParam("授权组名称") @RequestParam(value = "modelGrantGroupName", required = false) String modelGrantGroupName);

    /**
     * 查询业务模型名称.
     *
     * @param userId         userId
     * @param modelName      modelName
     * @param modelGroupNam  modelGroupNam
     * @param modelGroupType modelGroupType
     * @param curPage        curPage
     * @param pageSize       pageSize
     * @return Result
     */
    @ApiOperation(value = "查询业务模型名称", notes = "查询所有的业务模型和授权组进行绑定", produces = "application/json")
    @GetMapping(value = "/selectAll/businessModel")
    Result<ModelNameAndGroupNameAndTypes, Object> selectAllBusinessModel(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                                                                         @ApiParam("模型名称") @RequestParam(value = "modelName", required = false) String modelName,
                                                                         @ApiParam("模型分组名称") @RequestParam(value = "modelGroupName", required = false) String modelGroupNam,
                                                                         @ApiParam("模型分组类型") @RequestParam(value = "modelGroupType", required = false) String modelGroupType,
                                                                         @ApiParam("页码") @RequestParam(value = "curPage", defaultValue = "1") Integer curPage,
                                                                         @ApiParam("行数") @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize);

    /**
     * 查询授权组名称是否重复.
     *
     * @param userId         userId
     * @param modelGroupName modelGroupName
     * @return Boolean
     */
    @ApiOperation(value = "查询授权组名称是否重复", notes = "根据授权组名称查询是否重复", produces = "application/json")
    @GetMapping(value = "/queryModelGroupNameIsExits")
    Result<Boolean, Object> queryModelGroupNameIsExits(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                                                       @ApiParam("授权组名称") @RequestParam(value = "modelGroupName", required = false) String modelGroupName);

}

package com.youngdatafan.portal.model.management.outmodel.api;

import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.portal.model.management.outmodel.dto.OutModelAndMetaDataListDTO;
import com.youngdatafan.portal.model.management.outmodel.dto.OutModelAndMetaDataWithPageDTO;
import com.youngdatafan.portal.model.management.outmodel.dto.OutModelGroupDTO;
import com.youngdatafan.portal.model.management.outmodel.dto.OutModelProjectIdAndNamesDTO;
import com.youngdatafan.portal.model.management.outmodel.vo.AddOutModelVO;
import com.youngdatafan.portal.model.management.outmodel.vo.UpdateOutModelVO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
public interface OutModelServiceApi {


    @ApiOperation(value = "添加输出类模型", produces = "application/json")
    @PostMapping(value = "/add")
    Result<String, Object> add(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId
            , @ApiParam("添加输出类模型对象") @Validated @RequestBody AddOutModelVO addOutModelVO);


    @ApiOperation(value = "删除输出类模型", notes = "根据模型id删除输出类模型", produces = "application/json")
    @DeleteMapping(value = "/delete/{outModelId}")
    Result<Boolean, Object> delete(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                                   @ApiParam("输出类模型id") @PathVariable("outModelId") String[] outModelId);


    @ApiOperation(value = "查询所有分组", notes = "查询所有分组", produces = "application/json")
    @GetMapping(value = "/getGroups")
    Result<List<OutModelGroupDTO>, Object> getAllGroup(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId);


    @ApiOperation(value = "查询所有输出类模型", notes = "查询所有分组", produces = "application/json")
    @GetMapping(value = "/getAllModel")
    Result<OutModelAndMetaDataWithPageDTO, Object> selectAllOutModel(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                                                                     @ApiParam("页码") @RequestParam(value = "curPage", defaultValue = "1") Integer curPage,
                                                                     @ApiParam("行数") @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                                                     @ApiParam("分组名称") @RequestParam(value = "groupId", required = false) String groupId,
                                                                     @ApiParam("项目名称") @RequestParam(value = "projectId", required = false) String projectId,
                                                                     @ApiParam("模型名称") @RequestParam(value = "modelName", required = false) String modelName,
                                                                     @ApiParam("是否有效") @RequestParam(value = "enabled", required = false) String enabled);


    @ApiOperation(value = "查询所有项目名称和id", notes = "查询所有分组", produces = "application/json")
    @GetMapping(value = "/getProjects")
    Result<List<OutModelProjectIdAndNamesDTO>, Object> getAllProject(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId);


    @ApiOperation(value = "查询自定义模型信息", notes = "根据模型名称查询模型信息", produces = "application/json")
    @GetMapping(value = "/select/outModelAndMetaData")
    Result<OutModelAndMetaDataListDTO, Object> selectOutModelAndMetaData(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                                                                         @ApiParam("modelName实为modelId") @RequestParam(value = "modelName") String modelName);

    @ApiOperation(value = "修改输出模型所有信息", notes = "修改输出模型所有信息", produces = "application/json")
    @PutMapping(value = "/update/all")
    Result<Boolean, Object> updateAll(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                                      @Valid @RequestBody UpdateOutModelVO updateOutModelVO);

    @ApiOperation(value = "修改模型部分信息", notes = "修改模型部分信息", produces = "application/json")
    @PutMapping(value = "/update/selective")
    Result<Boolean, Object> updateSelective(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                                            @Valid @RequestBody UpdateOutModelVO updateOutModelVO);

    @ApiOperation(value = "根据模型id查询输出模型", notes = "根据模型id查询输出模型", produces = "application/json")
    @GetMapping(value = "/model/exist")
    Result<Boolean, Object> outModelIsExist(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                                            @RequestParam("modelId") String modelId);
}

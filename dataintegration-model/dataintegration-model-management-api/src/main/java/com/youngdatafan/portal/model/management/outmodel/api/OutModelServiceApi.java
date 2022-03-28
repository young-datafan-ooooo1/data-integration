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
import java.util.List;
import javax.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 加输出类模型.
 */
public interface OutModelServiceApi {

    /**
     * 添加输出类模型.
     *
     * @param userId        userId
     * @param addOutModelVO addOutModelVO
     * @return String
     */
    @ApiOperation(value = "添加输出类模型", produces = "application/json")
    @PostMapping(value = "/add")
    Result<String, Object> add(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                               @ApiParam("添加输出类模型对象") @Validated @RequestBody AddOutModelVO addOutModelVO);

    /**
     * 删除输出类模型.
     *
     * @param userId     userId
     * @param outModelId outModelId
     * @return Boolean
     */
    @ApiOperation(value = "删除输出类模型", notes = "根据模型id删除输出类模型", produces = "application/json")
    @DeleteMapping(value = "/delete/{outModelId}")
    Result<Boolean, Object> delete(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                                   @ApiParam("输出类模型id") @PathVariable("outModelId") String[] outModelId);

    /**
     * 查询所有分组.
     *
     * @param userId userId
     * @return List
     */
    @ApiOperation(value = "查询所有分组", notes = "查询所有分组", produces = "application/json")
    @GetMapping(value = "/getGroups")
    Result<List<OutModelGroupDTO>, Object> getAllGroup(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId);

    /**
     * 查询所有输出类模型.
     *
     * @param userId    userId
     * @param curPage   curPage
     * @param pageSize  pageSize
     * @param groupId   groupId
     * @param projectId projectId
     * @param modelName modelName
     * @param enabled   enabled
     * @return OutModelAndMetaDataWithPageDTO
     */
    @ApiOperation(value = "查询所有输出类模型", notes = "查询所有分组", produces = "application/json")
    @GetMapping(value = "/getAllModel")
    Result<OutModelAndMetaDataWithPageDTO, Object> selectAllOutModel(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                                                                     @ApiParam("页码") @RequestParam(value = "curPage", defaultValue = "1") Integer curPage,
                                                                     @ApiParam("行数") @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                                                     @ApiParam("分组名称") @RequestParam(value = "groupId", required = false) String groupId,
                                                                     @ApiParam("项目名称") @RequestParam(value = "projectId", required = false) String projectId,
                                                                     @ApiParam("模型名称") @RequestParam(value = "modelName", required = false) String modelName,
                                                                     @ApiParam("是否有效") @RequestParam(value = "enabled", required = false) String enabled);

    /**
     * 查询所有项目名称和id.
     *
     * @param userId userId
     * @return 查询所有项目名称和id
     */
    @ApiOperation(value = "查询所有项目名称和id", notes = "查询所有分组", produces = "application/json")
    @GetMapping(value = "/getProjects")
    Result<List<OutModelProjectIdAndNamesDTO>, Object> getAllProject(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId);

    /**
     * 查询自定义模型信息.
     *
     * @param userId    userId
     * @param modelName modelName
     * @return OutModelAndMetaDataListDTO
     */
    @ApiOperation(value = "查询自定义模型信息", notes = "根据模型名称查询模型信息", produces = "application/json")
    @GetMapping(value = "/select/outModelAndMetaData")
    Result<OutModelAndMetaDataListDTO, Object> selectOutModelAndMetaData(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                                                                         @ApiParam("modelName实为modelId") @RequestParam(value = "modelName") String modelName);

    /**
     * 修改输出模型所有信息.
     *
     * @param userId           userId
     * @param updateOutModelVO updateOutModelVO
     * @return Boolean
     */
    @ApiOperation(value = "修改输出模型所有信息", notes = "修改输出模型所有信息", produces = "application/json")
    @PutMapping(value = "/update/all")
    Result<Boolean, Object> updateAll(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                                      @Valid @RequestBody UpdateOutModelVO updateOutModelVO);

    /**
     * 修改模型部分信息.
     *
     * @param userId           userId
     * @param updateOutModelVO updateOutModelVO
     * @return Boolean
     */
    @ApiOperation(value = "修改模型部分信息", notes = "修改模型部分信息", produces = "application/json")
    @PutMapping(value = "/update/selective")
    Result<Boolean, Object> updateSelective(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                                            @Valid @RequestBody UpdateOutModelVO updateOutModelVO);

    /**
     * 根据模型id查询输出模型.
     *
     * @param userId  userId
     * @param modelId modelId
     * @return Boolean
     */
    @ApiOperation(value = "根据模型id查询输出模型", notes = "根据模型id查询输出模型", produces = "application/json")
    @GetMapping(value = "/model/exist")
    Result<Boolean, Object> outModelIsExist(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                                            @RequestParam("modelId") String modelId);
}

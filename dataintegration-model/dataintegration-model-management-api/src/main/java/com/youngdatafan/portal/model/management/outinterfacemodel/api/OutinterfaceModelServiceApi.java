package com.youngdatafan.portal.model.management.outinterfacemodel.api;

import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.portal.model.management.datasource.dto.DatasourceDTO;
import com.youngdatafan.portal.model.management.outinterfacemodel.dto.BatchDownloadOutinterfaceModelDTO;
import com.youngdatafan.portal.model.management.outinterfacemodel.dto.OutinterfaceBasicModelAndMetaDataDTO;
import com.youngdatafan.portal.model.management.outinterfacemodel.dto.OutinterfaceGroupDTO;
import com.youngdatafan.portal.model.management.outinterfacemodel.dto.OutinterfaceModelDTO;
import com.youngdatafan.portal.model.management.outinterfacemodel.dto.OutinterfaceModelDatasourceDTO;
import com.youngdatafan.portal.model.management.outinterfacemodel.dto.OutinterfaceModelGroupDTO;
import com.youngdatafan.portal.model.management.outinterfacemodel.dto.OutinterfaceModelMetaDataDTO;
import com.youngdatafan.portal.model.management.outinterfacemodel.dto.OutinterfaceModelNameAndCnameDTO;
import com.youngdatafan.portal.model.management.outinterfacemodel.dto.OutinterfaceModelTypeAndGroupListDTO;
import com.youngdatafan.portal.model.management.outinterfacemodel.vo.AddOutinterfaceModelVO;
import com.youngdatafan.portal.model.management.outinterfacemodel.vo.OutinterfacePreviewDataVO;
import com.youngdatafan.portal.model.management.outinterfacemodel.vo.UpdateOutinterfaceModelVO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 获取模型分组集合.
 */
public interface OutinterfaceModelServiceApi {

    /**
     * 获取模型分组集合.
     *
     * @param userId userId
     * @return List
     */
    @ApiOperation(value = "获取模型分组集合", notes = "获取模型分组集合信息", produces = "application/json")
    @GetMapping(value = "/select/modelAndGroups")
    Result<List<OutinterfaceModelTypeAndGroupListDTO>, Object> selectModelAndGroups(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId);

    /**
     * 查询业务模型信息.
     *
     * @param userId                userId
     * @param outinterfaceModelName outinterfaceModelName
     * @return OutinterfaceModelDTO
     */
    @ApiOperation(value = "查询业务模型信息", notes = "根据模型名称查询模型信息", produces = "application/json")
    @GetMapping(value = "/select/outinterfaceModel")
    Result<OutinterfaceModelDTO, Object> selectOutinterfaceModel(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                                                                 @RequestParam(value = "modelName", required = false) String outinterfaceModelName);

    /**
     * 查询数据源信息.
     *
     * @param userId                userId
     * @param outinterfaceModelName outinterfaceModelName
     * @return OutinterfaceModelDatasourceDTO
     */
    @ApiOperation(value = "查询数据源信息", notes = "根据模型名称查询模型信息", produces = "application/json")
    @GetMapping(value = "/select/datasource")
    Result<OutinterfaceModelDatasourceDTO, Object> selectDatasource(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                                                                    @RequestParam(value = "modelName", required = false) String outinterfaceModelName);

    /**
     * 根据不同的模型类型查询数据源信息.
     *
     * @param userId                userId
     * @param outinterfaceModelName outinterfaceModelName
     * @param modeType              modeType
     * @return OutinterfaceModelDatasourceDTO
     */
    @ApiOperation(value = "根据不同的模型类型查询数据源信息", notes = "根据模型名称查询模型信息", produces = "application/json")
    @GetMapping(value = "/select/datasourceByModelName")
    Result<OutinterfaceModelDatasourceDTO, Object> selectDatasourceByModelName(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                                                                               @RequestParam(value = "modelName", required = false) String outinterfaceModelName,
                                                                               @RequestParam(value = "modelType", required = false) String modeType);

    /**
     * 查询业务模型元数据信息.
     *
     * @param userId                userId
     * @param outinterfaceModelName outinterfaceModelName
     * @return OutinterfaceModelMetaDataDTO
     */
    @ApiOperation(value = "查询业务模型元数据信息", notes = "根据模型名称查询模型元数据信息", produces = "application/json")
    @GetMapping(value = "/select/outinterfaceMetaData")
    Result<List<OutinterfaceModelMetaDataDTO>, Object> selectOutinterfaceModelMetaData(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                                                                                       @RequestParam(value = "modelName", required = false) String outinterfaceModelName);

    /**
     * 业务模型新增接口.
     *
     * @param userId                 userId
     * @param addOutinterfaceModelVO addOutinterfaceModelVO
     * @return DatasourceDTO
     */
    @PostMapping(value = "/add")
    Result<DatasourceDTO, Object> add(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                                      @RequestBody AddOutinterfaceModelVO addOutinterfaceModelVO);

    /**
     * 删除业务模型.
     *
     * @param userId                userId
     * @param outinterfaceModelName outinterfaceModelName
     * @return Boolean
     */
    @ApiOperation(value = "删除业务模型", notes = "根据模型名称删除业务模型", produces = "application/json")
    @DeleteMapping(value = "/delete/{outinterfaceModelName}")
    Result<Boolean, Object> delete(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                                   @PathVariable("outinterfaceModelName") @ApiParam("业务模型名称") String outinterfaceModelName);

    /**
     * 获取业务模型.
     *
     * @param userId         userId
     * @param curPage        curPage
     * @param pageSize       pageSize
     * @param modelName      modelName
     * @param group          group
     * @param basicModelName basicModelName
     * @return Result
     */
    @ApiOperation(value = "获取业务模型", notes = "根据用户id获取业务模型信息", produces = "application/json")
    @GetMapping(value = "/selectAll")
    Result selectAll(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                     @ApiParam("页码") @RequestParam(value = "curPage", defaultValue = "1") Integer curPage,
                     @ApiParam("行数") @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                     @ApiParam("业务模型名称") @RequestParam(value = "modelName", required = false) String modelName,
                     @ApiParam("所属分组") @RequestParam(value = "group", required = false) String group,
                     @ApiParam("基础模型名称") @RequestParam(value = "basicModelName", required = false) String basicModelName);


    /**
     * 更新基础模型.
     *
     * @param userId                    userId
     * @param updateOutinterfaceModelVO updateOutinterfaceModelVO
     * @return Result
     */
    @PutMapping(value = "/update")
    Result update(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                  @ApiParam("更新基础模型对象") @RequestBody UpdateOutinterfaceModelVO updateOutinterfaceModelVO);

    /**
     * 获取基础模型组.
     *
     * @param userId userId
     * @return List
     */
    @ApiOperation(value = "获取基础模型组", notes = "根据用户id获取业务模型信息", produces = "application/json")
    @GetMapping(value = "/getBasicModelGroup")
    Result<List<OutinterfaceModelGroupDTO>, Object> getBasicModelGroup(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId);

    /**
     * 获取基础模型组.
     *
     * @param userId          userId
     * @param basicModelGroup basicModelGroup
     * @return List
     */
    @ApiOperation(value = "获取基础模型组", notes = "根据用户id获取业务模型信息", produces = "application/json")
    @GetMapping(value = "/getBasicModel")
    Result<List<OutinterfaceModelNameAndCnameDTO>, Object> getBasicModels(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                                                                          @ApiParam("基础模型组") @RequestParam(value = "basicModelGroup", required = false) String basicModelGroup);

    /**
     * 获取基础模型组.
     *
     * @param userId     userId
     * @param basicModel basicModel
     * @return List
     */
    @ApiOperation(value = "获取基础模型组", notes = "根据用户id获取业务模型信息", produces = "application/json")
    @GetMapping(value = "/getBasicModelColumns")
    Result<List<OutinterfaceBasicModelAndMetaDataDTO>, Object> getBasicModelColumns(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                                                                                    @ApiParam("基础模型组") @RequestParam("basicModel") String basicModel);

    /**
     * 批量下载模型.
     *
     * @param userId     userId
     * @param modelNames modelNames
     * @return List
     */
    @ApiOperation(value = "批量下载模型", notes = "查询所有数据源", produces = "application/json")
    @PostMapping(value = "/batchDownload")
    Result<List<BatchDownloadOutinterfaceModelDTO>, Object> batchDownloadExcel(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                                                                               @ApiParam("模型名称") @RequestBody List<String> modelNames);

    /**
     * 批量上传模型.
     *
     * @param userId  userId
     * @param request request
     * @return Boolean
     */
    @ApiOperation(value = "批量上传模型", notes = "查询所有数据源", produces = "application/json")
    @PostMapping(value = "/batchUploadExcel")
    Result<Boolean, Object> batchUploadExcel(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId, HttpServletRequest request);

    /**
     * 查询所有分组.
     *
     * @param userId userId
     * @return List
     */
    @ApiOperation(value = "查询所有分组", notes = "查询所有分组", produces = "application/json")
    @GetMapping(value = "/getGroups")
    Result<List<OutinterfaceGroupDTO>, Object> getAllGroup(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId);

    /**
     * 查询业务名称是否重复.
     *
     * @param userId                 userId
     * @param outinterfaceModelName  outinterfaceModelName
     * @param outinterfaceModelGroup outinterfaceModelGroup
     * @return Boolean
     */
    @ApiOperation(value = "查询业务名称是否重复", notes = "批量新增模型", produces = "application/json")
    @GetMapping(value = "/queryOutinterfaceModelExists")
    Result<Boolean, Object> queryOutinterfaceModelExists(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                                                         @ApiParam("业务模型名称") @RequestParam("outinterfaceModelName") String outinterfaceModelName,
                                                         @ApiParam("业务模型分组") @RequestParam("outinterfaceModelGroup") String outinterfaceModelGroup);

    /**
     * 测试预览.
     *
     * @param userId                    userId
     * @param outinterfacePreviewDataVO outinterfacePreviewDataVO
     * @return List
     */
    @ApiOperation(value = "测试预览", notes = "测试预览", produces = "application/json")
    @PostMapping(value = "/testPreview")
    Result<List<Map<String, Object>>, Object> testPriview(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                                                          @RequestBody OutinterfacePreviewDataVO outinterfacePreviewDataVO);
}

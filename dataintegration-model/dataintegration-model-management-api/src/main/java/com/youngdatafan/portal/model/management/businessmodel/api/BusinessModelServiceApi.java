package com.youngdatafan.portal.model.management.businessmodel.api;

import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.portal.model.management.businessmodel.dto.BasicModelAndMetaDataDTO;
import com.youngdatafan.portal.model.management.businessmodel.dto.BasicModelGroupDTO;
import com.youngdatafan.portal.model.management.businessmodel.dto.BasicModelNameAndCnameDTO;
import com.youngdatafan.portal.model.management.businessmodel.dto.BatchDownloadBusinessModelDTO;
import com.youngdatafan.portal.model.management.businessmodel.dto.BusinessModelDTO;
import com.youngdatafan.portal.model.management.businessmodel.dto.BusinessModelMetaDataDTO;
import com.youngdatafan.portal.model.management.businessmodel.dto.GroupDTO;
import com.youngdatafan.portal.model.management.businessmodel.dto.ModelDatasourceDTO;
import com.youngdatafan.portal.model.management.businessmodel.dto.ModelTypeAndGroupListDTO;
import com.youngdatafan.portal.model.management.businessmodel.vo.AddBusinessModelVO;
import com.youngdatafan.portal.model.management.businessmodel.vo.BusinessPreviewDataVO;
import com.youngdatafan.portal.model.management.businessmodel.vo.UpdateBusinessModelVO;
import com.youngdatafan.portal.model.management.datasource.dto.DatasourceDTO;
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
 * 模型分组.
 */
public interface BusinessModelServiceApi {

    /**
     * 获取模型分组集合.
     *
     * @param userId userId
     * @return ModelTypeAndGroupListDTO
     */
    @ApiOperation(value = "获取模型分组集合", notes = "获取模型分组集合信息", produces = "application/json")
    @GetMapping(value = "/select/modelAndGroups")
    Result<List<ModelTypeAndGroupListDTO>, Object> selectModelAndGroups(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId);

    /**
     * 查询业务模型信息.
     *
     * @param userId            userId
     * @param businessModelName businessModelName
     * @return BusinessModelDTO
     */
    @ApiOperation(value = "查询业务模型信息", notes = "根据模型名称查询模型信息", produces = "application/json")
    @GetMapping(value = "/select/businessModel")
    Result<BusinessModelDTO, Object> selectBusinessModel(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                                                         @RequestParam(value = "modelName", required = false) String businessModelName);

    /**
     * 查询数据源信息.
     *
     * @param userId            userId
     * @param businessModelName businessModelName
     * @return ModelDatasourceDTO
     */
    @ApiOperation(value = "查询数据源信息", notes = "根据模型名称查询模型信息", produces = "application/json")
    @GetMapping(value = "/select/datasource")
    Result<ModelDatasourceDTO, Object> selectDatasource(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                                                        @RequestParam(value = "modelName", required = false) String businessModelName);

    /**
     * 根据不同的模型类型查询数据源信息.
     *
     * @param userId            userId
     * @param businessModelName businessModelName
     * @param modeType          modelType
     * @return ModelDatasourceDTO
     */
    @ApiOperation(value = "根据不同的模型类型查询数据源信息", notes = "根据模型名称查询模型信息", produces = "application/json")
    @GetMapping(value = "/select/datasourceByModelName")
    Result<ModelDatasourceDTO, Object> selectDatasourceByModelName(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                                                                   @RequestParam(value = "modelName", required = false) String businessModelName,
                                                                   @RequestParam(value = "modelType", required = false) String modeType);

    /**
     * 查询业务模型元数据信息.
     *
     * @param userId            userId
     * @param businessModelName businessModelName
     * @return List
     */
    @ApiOperation(value = "查询业务模型元数据信息", notes = "根据模型名称查询模型元数据信息", produces = "application/json")
    @GetMapping(value = "/select/businessMetaData")
    Result<List<BusinessModelMetaDataDTO>, Object> selectBusinessModelMetaData(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                                                                               @RequestParam(value = "modelName", required = false) String businessModelName);

    /**
     * add.
     *
     * @param userId             userId
     * @param addBusinessModelVO addBusinessModelVO
     * @return DatasourceDTO.
     */
    @PostMapping(value = "/add")
    Result<DatasourceDTO, Object> add(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                                      @RequestBody AddBusinessModelVO addBusinessModelVO);

    /**
     * 删除业务模型.
     *
     * @param userId            userId
     * @param businessModelName businessModelName
     * @return 删除业务模型
     */
    @ApiOperation(value = "删除业务模型", notes = "根据模型名称删除业务模型", produces = "application/json")
    @DeleteMapping(value = "/delete/{businessModelName}")
    Result<Boolean, Object> delete(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                                   @PathVariable("businessModelName") @ApiParam("业务模型名称") String businessModelName);

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
     * 更新基础模型对象.
     *
     * @param userId                userId
     * @param updateBusinessModelVO updateBusinessModelVO
     * @return Result
     */
    @PutMapping(value = "/update")
    Result update(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                  @ApiParam("更新基础模型对象") @RequestBody UpdateBusinessModelVO updateBusinessModelVO);

    /**
     * 获取基础模型组.
     *
     * @param userId userId
     * @return 获取基础模型组
     */
    @ApiOperation(value = "获取基础模型组", notes = "根据用户id获取业务模型信息", produces = "application/json")
    @GetMapping(value = "/getBasicModelGroup")
    Result<List<BasicModelGroupDTO>, Object> getBasicModelGroup(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId);

    /**
     * 获取基础模型组.
     *
     * @param userId          userId
     * @param basicModelGroup basicModelGroup
     * @return List
     */
    @ApiOperation(value = "获取基础模型组", notes = "根据用户id获取业务模型信息", produces = "application/json")
    @GetMapping(value = "/getBasicModel")
    Result<List<BasicModelNameAndCnameDTO>, Object> getBasicModels(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
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
    Result<List<BasicModelAndMetaDataDTO>, Object> getBasicModelColumns(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
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
    Result<List<BatchDownloadBusinessModelDTO>, Object> batchDownloadExcel(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                                                                           @ApiParam("模型名称") @RequestBody List<String> modelNames);

    /**
     * 批量上传模型.
     *
     * @param userId userId
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
    Result<List<GroupDTO>, Object> getAllGroup(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId);

    /**
     * 查询业务名称是否重复.
     *
     * @param userId             userId
     * @param businessModelName  businessModelName
     * @param businessModelGroup businessModelGroup
     * @return Boolean
     */
    @ApiOperation(value = "查询业务名称是否重复", notes = "批量新增模型", produces = "application/json")
    @GetMapping(value = "/queryBusinessModelExists")
    Result<Boolean, Object> queryBusinessModelExists(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                                                     @ApiParam("业务模型名称") @RequestParam("businessModelName") String businessModelName,
                                                     @ApiParam("业务模型分组") @RequestParam("businessModelGroup") String businessModelGroup);

    /**
     * 测试预览.
     *
     * @param userId                userId
     * @param businessPreviewDataVO businessPreviewDataVO
     * @return List
     */
    @ApiOperation(value = "测试预览", notes = "测试预览", produces = "application/json")
    @PostMapping(value = "/testPreview")
    Result<List<Map<String, Object>>, Object> testPriview(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                                                          @RequestBody BusinessPreviewDataVO businessPreviewDataVO);
}

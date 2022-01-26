package com.youngdatafan.portal.model.management.businessmodel.api;

import com.datafan.dataintegration.core.model.Result;
import com.youngdatafan.portal.model.management.businessmodel.dto.*;
import com.youngdatafan.portal.model.management.businessmodel.dto.BasicModelAndMetaDataDTO;
import com.youngdatafan.portal.model.management.businessmodel.dto.BasicModelGroupDTO;
import com.youngdatafan.portal.model.management.businessmodel.dto.BasicModelNameAndCnameDTO;
import com.youngdatafan.portal.model.management.businessmodel.dto.BusinessModelDTO;
import com.youngdatafan.portal.model.management.businessmodel.dto.BusinessModelMetaDataDTO;
import com.youngdatafan.portal.model.management.businessmodel.dto.GroupDTO;
import com.youngdatafan.portal.model.management.businessmodel.dto.ModelDatasourceDTO;
import com.youngdatafan.portal.model.management.businessmodel.dto.ModelTypeAndGroupListDTO;
import com.youngdatafan.portal.model.management.businessmodel.vo.AddBusinessModelVO;
import com.youngdatafan.portal.model.management.businessmodel.vo.BusinessPreviewDataVO;
import com.youngdatafan.portal.model.management.businessmodel.vo.UpdateBusinessModelVO;
import com.youngdatafan.portal.model.management.datasource.dto.DatasourceDTO;
import com.youngdatafan.portal.model.management.businessmodel.dto.BatchDownloadBusinessModelDTO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

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
public interface BusinessModelServiceApi {

    @ApiOperation(value = "获取模型分组集合", notes = "获取模型分组集合信息", produces = "application/json")
    @GetMapping(value = "/select/modelAndGroups")
    Result<List<ModelTypeAndGroupListDTO>, Object> selectModelAndGroups(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId);


    @ApiOperation(value = "查询业务模型信息", notes = "根据模型名称查询模型信息", produces = "application/json")
    @GetMapping(value = "/select/businessModel")
    Result<BusinessModelDTO, Object> selectBusinessModel(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                                                         @RequestParam(value = "modelName", required = false) String businessModelName);


    @ApiOperation(value = "查询数据源信息", notes = "根据模型名称查询模型信息", produces = "application/json")
    @GetMapping(value = "/select/datasource")
    Result<ModelDatasourceDTO, Object> selectDatasource(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                                                        @RequestParam(value = "modelName", required = false) String businessModelName);

    @ApiOperation(value = "根据不同的模型类型查询数据源信息", notes = "根据模型名称查询模型信息", produces = "application/json")
    @GetMapping(value = "/select/datasourceByModelName")
    Result<ModelDatasourceDTO, Object> selectDatasourceByModelName(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                                                                   @RequestParam(value = "modelName", required = false) String businessModelName,
                                                                   @RequestParam(value = "modelType", required = false) String modeType);


    @ApiOperation(value = "查询业务模型元数据信息", notes = "根据模型名称查询模型元数据信息", produces = "application/json")
    @GetMapping(value = "/select/businessMetaData")
        //todo:需要添加一个用户id
    Result<List<BusinessModelMetaDataDTO>, Object> selectBusinessModelMetaData(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                                                                               @RequestParam(value = "modelName", required = false) String businessModelName);


    @ApiOperation(value = "业务模型新增接口", notes = "添加业务模型模型的【模型名称(PK)\n" +
            "模型中文名\n" +
            "关联基础模型名称\n" +
            "模型描述\n" +
            "数据过滤条件\n" +
            "是否启用\n" +
            "查询SQL\n" +
            "创建时间\n" +
            "修改时间\n" +
            "创建者】", produces = "application/json")
    @PostMapping(value = "/add")
    Result<DatasourceDTO, Object> add(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                                      @RequestBody AddBusinessModelVO addBusinessModelVO);


    @ApiOperation(value = "删除业务模型", notes = "根据模型名称删除业务模型", produces = "application/json")
    @DeleteMapping(value = "/delete/{businessModelName}")
    Result<Boolean, Object> delete(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                                   @PathVariable("businessModelName") @ApiParam("业务模型名称") String businessModelName);


    @ApiOperation(value = "获取业务模型", notes = "根据用户id获取业务模型信息", produces = "application/json")
    @GetMapping(value = "/selectAll")
        //todo:需要添加一个用户id
    Result selectAll(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                     @ApiParam("页码") @RequestParam(value = "curPage", defaultValue = "1") Integer curPage,
                     @ApiParam("行数") @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                     @ApiParam("业务模型名称") @RequestParam(value = "modelName", required = false) String modelName,
                     @ApiParam("所属分组") @RequestParam(value = "group", required = false) String group,
                     @ApiParam("基础模型名称") @RequestParam(value = "basicModelName", required = false) String basicModelName);


    @ApiOperation(value = "更新基础模型", notes = "添加基础模型的【模型名称(PK)\n" +
            "模型中文名\n" +
            "关联基础模型名称\n" +
            "模型描述\n" +
            "数据过滤条件\n" +
            "是否启用\n" +
            "查询SQL\n" +
            "创建时间\n" +
            "修改时间\n" +
            "创建者】", produces = "application/json")
    @PutMapping(value = "/update")
    Result update(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                  @ApiParam("更新基础模型对象") @RequestBody UpdateBusinessModelVO updateBusinessModelVO);


    @ApiOperation(value = "获取基础模型组", notes = "根据用户id获取业务模型信息", produces = "application/json")
    @GetMapping(value = "/getBasicModelGroup")
        //todo:需要添加一个用户id
    Result<List<BasicModelGroupDTO>, Object> getBasicModelGroup(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId);


    @ApiOperation(value = "获取基础模型组", notes = "根据用户id获取业务模型信息", produces = "application/json")
    @GetMapping(value = "/getBasicModel")
        //todo:需要添加一个用户id
    Result<List<BasicModelNameAndCnameDTO>, Object> getBasicModels(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                                                                   @ApiParam("基础模型组") @RequestParam(value = "basicModelGroup", required = false) String basicModelGroup);


    @ApiOperation(value = "获取基础模型组", notes = "根据用户id获取业务模型信息", produces = "application/json")
    @GetMapping(value = "/getBasicModelColumns")
        //todo:需要添加一个用户id
    Result<List<BasicModelAndMetaDataDTO>, Object> getBasicModelColumns(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                                                                        @ApiParam("基础模型组") @RequestParam("basicModel") String basicModel);


    @ApiOperation(value = "批量下载模型", notes = "查询所有数据源", produces = "application/json")
    @PostMapping(value = "/batchDownload")
    Result<List<BatchDownloadBusinessModelDTO>, Object> batchDownloadExcel(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                                                                           @ApiParam("模型名称") @RequestBody List<String> modelNames);

    @ApiOperation(value = "批量上传模型", notes = "查询所有数据源", produces = "application/json")
    @PostMapping(value = "/batchUploadExcel")
    Result<Boolean, Object> batchUploadExcel(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId
            , HttpServletRequest request);

    @ApiOperation(value = "查询所有分组", notes = "查询所有分组", produces = "application/json")
    @GetMapping(value = "/getGroups")
    Result<List<GroupDTO>, Object> getAllGroup(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId);


    @ApiOperation(value = "查询业务名称是否重复", notes = "批量新增模型", produces = "application/json")
    @GetMapping(value = "/queryBusinessModelExists")
    Result<Boolean, Object> queryBusinessModelExists(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                                                     @ApiParam("业务模型名称") @RequestParam("businessModelName") String businessModelName,
                                                     @ApiParam("业务模型分组") @RequestParam("businessModelGroup") String businessModelGroup);

    @ApiOperation(value = "测试预览", notes = "测试预览", produces = "application/json")
    @PostMapping(value = "/testPreview")
    Result<List<Map<String, Object>>, Object> testPriview(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                                                          @RequestBody BusinessPreviewDataVO businessPreviewDataVO);
}

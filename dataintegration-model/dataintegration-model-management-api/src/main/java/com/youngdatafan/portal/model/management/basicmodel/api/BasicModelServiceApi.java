package com.youngdatafan.portal.model.management.basicmodel.api;

import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.portal.model.management.basicmodel.dto.AllColumnDTO;
import com.youngdatafan.portal.model.management.basicmodel.dto.BasicModelAndMetaDataDTO;
import com.youngdatafan.portal.model.management.basicmodel.dto.BasicModelDatasourceDTO;
import com.youngdatafan.portal.model.management.basicmodel.dto.CustomModelDTO;
import com.youngdatafan.portal.model.management.basicmodel.dto.GroupDTO;
import com.youngdatafan.portal.model.management.basicmodel.dto.TablesAndViewDTO;
import com.youngdatafan.portal.model.management.basicmodel.vo.AddBasicModelVO;
import com.youngdatafan.portal.model.management.basicmodel.vo.BatchInserModelVO;
import com.youngdatafan.portal.model.management.basicmodel.vo.GetColumnsVO;
import com.youngdatafan.portal.model.management.basicmodel.vo.TestPreviewVO;
import com.youngdatafan.portal.model.management.basicmodel.vo.UpdateBasicModelVO;
import com.youngdatafan.portal.model.management.common.entity.ModelDTO;
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
 * BasicModelServiceApi.
 */
public interface BasicModelServiceApi {

    /**
     * 基础模型.
     *
     * @param userId          userId
     * @param addBasicModelVO addBasicModelVO
     * @return DatasourceDTO
     */
    @PostMapping(value = "/add")
    Result<DatasourceDTO, Object> add(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                                      @RequestBody AddBasicModelVO addBasicModelVO);


    /**
     * 删除基础模型.
     *
     * @param userId         userId
     * @param basicModelName basicModelName
     * @return Boolean
     */
    @ApiOperation(value = "删除基础模型", notes = "根据模型名称删除基础模型", produces = "application/json")
    @DeleteMapping(value = "/delete/{basicModelName}")
    Result<Boolean, Object> delete(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                                   @PathVariable("basicModelName") @ApiParam("基础模型名称") String basicModelName);


    /**
     * 获取基础模型.
     *
     * @param userId         userId
     * @param curPage        curPage
     * @param pageSize       pageSize
     * @param modelName      modelName
     * @param group          group
     * @param datasourceName datasourceName
     * @param modelTyp       modelTyp
     * @return Result
     */
    @ApiOperation(value = "获取基础模型", notes = "根据用户id获取基础模型信息", produces = "application/json")
    @GetMapping(value = "/selectAll")
    Result selectAll(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                     @ApiParam("页码") @RequestParam(value = "curPage", defaultValue = "1") Integer curPage,
                     @ApiParam("行数") @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                     @ApiParam("模型名称") @RequestParam(value = "modelName", required = false) String modelName,
                     @ApiParam("所属分组") @RequestParam(value = "group", required = false) String group,
                     @ApiParam("数据源名称") @RequestParam(value = "datasourceName", required = false) String datasourceName, @ApiParam("模型类型") @RequestParam("modelType") String modelTyp);


    /**
     * 更新基础模型.
     *
     * @param userId             userId
     * @param updateBasicModelVO updateBasicModelVO
     * @return Result
     */
    @PutMapping(value = "/update")
    Result update(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId, @ApiParam("更新基础模型对象") @RequestBody UpdateBasicModelVO updateBasicModelVO);

    /**
     * 查询所有数据源.
     *
     * @param userId userId
     * @return List
     */
    @ApiOperation(value = "查询所有数据源", notes = "查询所有数据源", produces = "application/json")
    @GetMapping(value = "/getDatasources")
    Result<List<BasicModelDatasourceDTO>, Object> getAllDatasource(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId);

    /**
     * 查询所有表.
     *
     * @param userId         userId
     * @param datasourceName datasourceName
     * @param schema         schema
     * @return List
     */
    @ApiOperation(value = "查询所有表", notes = "查询所有数据源", produces = "application/json")
    @GetMapping(value = "/getTables")
    Result<List<String>, Object> getAllTables(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                                              @ApiParam("数据库名称") @RequestParam("datasourceName") String datasourceName,
                                              @ApiParam("schema") @RequestParam(value = "schema", required = false) String schema);

    /**
     * 查询表和视图.
     *
     * @param userId         userId
     * @param datasourceName datasourceName
     * @param schema         schema
     * @return TablesAndViewDTO
     */
    @ApiOperation(value = "查询表和视图", notes = "查询所有数据源", produces = "application/json")
    @GetMapping(value = "/getTablesViews")
    Result<TablesAndViewDTO, Object> getAllTablesAndViews(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                                                          @ApiParam("数据库名称") @RequestParam("datasourceName") String datasourceName,
                                                          @ApiParam("schema") @RequestParam(value = "schema", required = false) String schema);

    /**
     * 查询所有schema.
     *
     * @param userId         userId
     * @param datasourceName datasourceName
     * @return List
     */
    @ApiOperation(value = "查询所有schema", notes = "查询所有schema", produces = "application/json")
    @GetMapping(value = "/getSchema")
    Result<List<String>, Object> getAllSchema(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                                              @ApiParam("数据库名称") @RequestParam("datasourceName") String datasourceName);

    /**
     * 通过表查询所有字段.
     *
     * @param userId         userId
     * @param datasourceName datasourceName
     * @param table          table
     * @param schema         schema
     * @param columnName     columnName
     * @return List
     */
    @ApiOperation(value = "通过表查询所有字段", notes = "查询所有数据源", produces = "application/json")
    @GetMapping(value = "/getColumns")
    Result<List<AllColumnDTO>, Object> getAllColumns(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                                                     @ApiParam("数据库名称") @RequestParam("datasourceName") String datasourceName,
                                                     @ApiParam("表名") @RequestParam("tables") String table,
                                                     @ApiParam("schema") @RequestParam(value = "schema", required = false) String schema,
                                                     @ApiParam("字段名") @RequestParam(value = "columnName", required = false) String columnName);

    /**
     * 获取表的查询sql.
     *
     * @param userId         userId
     * @param datasourceName datasourceName
     * @param table          table
     * @param schema         schema
     * @return String
     */
    @ApiOperation(value = "获取表的查询sql", notes = "查询所有数据源", produces = "application/json")
    @GetMapping(value = "/getTableQuerySql")
    Result<String, Object> getTableQuerySql(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                                            @ApiParam("数据库名称") @RequestParam("datasourceName") String datasourceName,
                                            @ApiParam("表名") @RequestParam("tables") String table,
                                            @ApiParam("schema") @RequestParam(value = "schema", required = false) String schema);

    /**
     * 通过sql查询所有字段.
     *
     * @param userId       userId
     * @param getColumnsVO getColumnsVO
     * @return List
     */
    @ApiOperation(value = "通过sql查询所有字段", notes = "通过sql查询所有字段", produces = "application/json")
    @PostMapping(value = "/getColumnsBySql")
    Result<List<AllColumnDTO>, Object> getAllColumnsBySql(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                                                          @ApiParam("GetColumnsVO") @RequestBody GetColumnsVO getColumnsVO);

    /**
     * 查询所有分组.
     *
     * @param userId    userId
     * @param groupType groupType
     * @return List
     */
    @ApiOperation(value = "查询所有分组", notes = "查询所有数据源", produces = "application/json")
    @GetMapping(value = "/getGroups")
    Result<List<GroupDTO>, Object> getAllGroup(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId, @RequestParam("groupType") String groupType);

    /**
     * 批量上传模型.
     *
     * @param userId    userId
     * @param modelType modelType
     * @param request   request
     * @return Boolean
     */
    @ApiOperation(value = "批量上传模型", notes = "查询所有数据源", produces = "application/json")
    @PostMapping(value = "/batchUploadExcel")
    Result<Boolean, Object> batchUploadExcel(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                                             @RequestParam("modelType") String modelType, HttpServletRequest request);

    /**
     * 批量下载模型.
     *
     * @param userId     userId
     * @param modelNames modelNames
     * @return List
     */
    @ApiOperation(value = "批量下载模型", notes = "查询所有数据源", produces = "application/json")
    @PostMapping(value = "/batchDownload")
    Result<List<BasicModelAndMetaDataDTO>, Object> batchDownloadExcel(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                                                                      @ApiParam("模型名称") @RequestBody List<String> modelNames);

    /**
     * 批量新增模型.
     *
     * @param userId            userId
     * @param batchInserModelVO batchInserModelVO
     * @return List
     */
    @ApiOperation(value = "批量新增模型", notes = "批量新增模型", produces = "application/json")
    @PostMapping(value = "/batchInsertModel")
    Result<List<BasicModelAndMetaDataDTO>, Object> batchInsertModel(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                                                                    @ApiParam("模型对象") @RequestBody BatchInserModelVO batchInserModelVO);

    /**
     * 批量新增模型.
     *
     * @param userId          userId
     * @param basicModelName  basicModelName
     * @param basicModelGroup basicModelGroup
     * @return Boolean
     */
    @ApiOperation(value = "批量新增模型", notes = "批量新增模型", produces = "application/json")
    @GetMapping(value = "/queryBasiceModelExists")
    Result<Boolean, Object> queryBasiceModelExists(@ApiParam("用户id") @RequestHeader(value = "authorization-userId") String userId,
                                                   @ApiParam("基础模型名称") @RequestParam("basicModelName") String basicModelName,
                                                   @ApiParam("基础模型分组") @RequestParam("basicModelGroup") String basicModelGroup);

    /**
     * 查询自定义模型信息.
     *
     * @param userId    userId
     * @param modelName modelName
     * @return CustomModelDTO
     */
    @ApiOperation(value = "查询自定义模型信息", notes = "根据模型名称查询模型信息", produces = "application/json")
    @GetMapping(value = "/select/customModel")
    Result<CustomModelDTO, Object> selectCustomModel(@ApiParam("用户id") @RequestHeader(value = "authorization-userId") String userId,
                                                     @RequestParam(value = "modelName", required = false) String modelName);

    /**
     * 查询模型信息.
     *
     * @param userId    userId
     * @param modelName modelName
     * @param modelType modelType
     * @return ModelDTO
     */
    @ApiOperation(value = "查询模型信息", notes = "根据模型名称和模型类型查询模型信息", produces = "application/json")
    @GetMapping(value = "/select/modelsByModelName")
    Result<ModelDTO, Object> selectModelsByModelName(@ApiParam("用户id") @RequestHeader(value = "authorization-userId") String userId,
                                                     @RequestParam(value = "modelName") String modelName,
                                                     @RequestParam(value = "modelType") String modelType);

    /**
     * 查询模型信息.
     *
     * @param userId        userId
     * @param testPreviewVO testPreviewVO
     * @return List
     */
    @ApiOperation(value = "查询模型信息", notes = "根据模型名称和模型类型查询模型信息", produces = "application/json")
    @PostMapping(value = "/testPreview")
    Result<List<Map<String, Object>>, Object> testPreview(@ApiParam("用户id") @RequestHeader(value = "authorization-userId") String userId, @RequestBody TestPreviewVO testPreviewVO);

}

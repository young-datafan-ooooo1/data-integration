package com.youngdatafan.portal.model.management.basicmodel.controller;

import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.dataintegration.core.util.StatusCode;
import com.github.pagehelper.PageInfo;
import com.youngdatafan.portal.model.management.basicmodel.api.BasicModelServiceApi;
import com.youngdatafan.portal.model.management.basicmodel.dto.AllColumnDTO;
import com.youngdatafan.portal.model.management.basicmodel.dto.BasicModelAndMetaDataDTO;
import com.youngdatafan.portal.model.management.basicmodel.dto.BasicModelDTO;
import com.youngdatafan.portal.model.management.basicmodel.dto.CustomModelDTO;
import com.youngdatafan.portal.model.management.basicmodel.dto.GroupDTO;
import com.youngdatafan.portal.model.management.basicmodel.dto.TablesAndViewDTO;
import com.youngdatafan.portal.model.management.basicmodel.service.BasiceModelService;
import com.youngdatafan.portal.model.management.basicmodel.vo.AddBasicModelVO;
import com.youngdatafan.portal.model.management.basicmodel.vo.BatchInserModelVO;
import com.youngdatafan.portal.model.management.basicmodel.vo.GetColumnsVO;
import com.youngdatafan.portal.model.management.basicmodel.vo.TestPreviewVO;
import com.youngdatafan.portal.model.management.basicmodel.vo.UpdateBasicModelVO;
import com.youngdatafan.portal.model.management.common.entity.ModelDTO;
import com.youngdatafan.portal.model.management.datasource.dto.DatasourceDTO;
import com.youngdatafan.portal.model.management.util.excel.Importer;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 * <p>****************************************************************************</p>
 * <ul style="margin:15px;">
 * <li>Description : description</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2020/2/10 4:25 PM</li>
 * <li>Author      : ksice_xt</li>
 * </ul>
 * <p>****************************************************************************</p>
 */
@RestController
@RequestMapping("/basicModel")
public class BasicModelServiceApiController implements BasicModelServiceApi {

    @Resource
    BasiceModelService basiceModelService;

    @Resource
    Importer importer;

    @Override
    public Result<DatasourceDTO, Object> add(String userId, AddBasicModelVO addBasicModelVO) {
        try {
            return basiceModelService.insert(userId, addBasicModelVO);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(StatusCode.CODE_10010.getCode(), addBasicModelVO, "新增失败");
        }
    }

    @Override
    public Result delete(String userId, String basicModelName) {
        try {
            return basiceModelService.delete(userId, basicModelName);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.success(false);
        }
    }

    @Override
    public Result<PageInfo<BasicModelDTO>, Object> selectAll(String userId, Integer curPage, Integer pageSize, String modelName, String group, String datasourceName, String modelType) {
        return Result.success(basiceModelService.selectAll(userId, modelName, group, datasourceName, curPage, pageSize, modelType));
    }

    @Override
    public Result update(String userId, UpdateBasicModelVO updateBasicModelVO) {
        try {

            return basiceModelService.update(userId, updateBasicModelVO);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(StatusCode.CODE_10010.getCode(), updateBasicModelVO, "更新失败");
        }
    }

    @Override
    public Result getAllDatasource(String userId) {
        return Result.success(basiceModelService.getAllDatasources(userId));
    }

    @Override
    public Result getAllTables(String userId, String datasourceName, String schema) {
        return basiceModelService.getAllTables(datasourceName, userId, schema);
    }

    @Override
    public Result<TablesAndViewDTO, Object> getAllTablesAndViews(String userId, String datasourceName, String schema) {
        return basiceModelService.getAllTablesAndViews(userId, datasourceName, schema);
    }

    @Override
    public Result<List<String>, Object> getAllSchema(String userId, String datasourceName) {
        return basiceModelService.getAllSchema(datasourceName, userId);
    }

    @Override
    public Result<List<AllColumnDTO>, Object> getAllColumns(String userId, String datasourceName, String table, String schema, String columnName) {
        return basiceModelService.getAllColumns(userId, table, datasourceName, schema);
    }

    @Override
    public Result<String, Object> getTableQuerySql(String userId, String datasourceName, String table, String schema) {
        return basiceModelService.getTableQuerySql(userId, datasourceName, table, schema);
    }

    @Override
    public Result<List<AllColumnDTO>, Object> getAllColumnsBySql(String userId, GetColumnsVO getColumnsVO) {
        try {
            List<AllColumnDTO> allColumnsBySql = basiceModelService.getAllColumnsBySql(userId, getColumnsVO.getDataSourceName(), getColumnsVO.getSql());
            return Result.success(allColumnsBySql);
        } catch (Exception e) {
            return Result.fail(StatusCode.CODE_10010.getCode(), false, "获取字段失败");
        }
    }

    @Override
    public Result<List<GroupDTO>, Object> getAllGroup(String userId, String groupType) {

        return Result.success(basiceModelService.getAllGroup(userId, groupType));
    }

    @Override
    public Result<Boolean, Object> batchUploadExcel(String userId, String modelType, HttpServletRequest request) {
        try {
            System.out.println(userId);
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            // 获取上传文件
            MultipartFile file = multipartRequest.getFile("file");

            InputStream inputStream = file.getInputStream();

            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(inputStream);

            return importer.importBasicModel(userId, xssfWorkbook, modelType);

        } catch (IOException e) {
            e.printStackTrace();

            return Result.fail(StatusCode.CODE_10010.getCode(), Boolean.FALSE, "上传失败");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(StatusCode.CODE_10010.getCode(), Boolean.FALSE, e.getMessage());
        }
    }

    @Override
    public Result<List<BasicModelAndMetaDataDTO>, Object> batchDownloadExcel(String userId, List<String> modelName) {

        return Result.success(basiceModelService.getBasicModelAndMetaData(userId, modelName));
    }

    @Override
    public Result<List<BasicModelAndMetaDataDTO>, Object> batchInsertModel(String userId, BatchInserModelVO batchInserModelVO) {
        return basiceModelService.batchInsertModel(userId, batchInserModelVO);
    }

    @Override
    public Result<Boolean, Object> queryBasiceModelExists(String userId, String basicModelName, String basicModelGroup) {
        return basiceModelService.queryBasiceModelExists(userId, basicModelName, basicModelGroup);
    }

    @Override
    public Result<CustomModelDTO, Object> selectCustomModel(String userId, String modelName) {
        return basiceModelService.selectCustomModel(userId, modelName);
    }

    @Override
    public Result<ModelDTO, Object> selectModelsByModelName(String userId, String modelName, String modelType) {
        ModelDTO modelDTO = basiceModelService.selectModelsByModelName(userId, modelName, modelType);
        return Result.success(modelDTO);
    }

    @Override
    public Result<List<Map<String, Object>>, Object> testPreview(String userId, TestPreviewVO testPreviewVO) {
        return basiceModelService.testPreview(userId, testPreviewVO.getDatasourceId(),
            testPreviewVO.getMetaDatas(), testPreviewVO.getModelFilter(), testPreviewVO.getTableName());
    }
}

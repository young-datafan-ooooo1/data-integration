package com.youngdatafan.portal.model.management.businessmodel.controller;

import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.dataintegration.core.util.StatusCode;
import com.youngdatafan.portal.model.management.businessmodel.api.BusinessModelServiceApi;
import com.youngdatafan.portal.model.management.businessmodel.dto.*;
import com.youngdatafan.portal.model.management.businessmodel.service.BusinessModelService;
import com.youngdatafan.portal.model.management.businessmodel.vo.AddBusinessModelVO;
import com.youngdatafan.portal.model.management.businessmodel.vo.BusinessPreviewDataVO;
import com.youngdatafan.portal.model.management.businessmodel.vo.UpdateBusinessModelVO;
import com.youngdatafan.portal.model.management.datasource.dto.DatasourceDTO;
import com.youngdatafan.portal.model.management.util.excel.Importer;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

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
@RequestMapping("/businessModel")
public class BusinessModelServiceApiController implements BusinessModelServiceApi {
    @Resource
    BusinessModelService businessModelService;

    @Resource
    Importer importer;

    @Override
    public Result<List<ModelTypeAndGroupListDTO>, Object> selectModelAndGroups(String userId) {

        return Result.success(businessModelService.selectModelAndGroupList(userId));
    }

    @Override
    public Result<BusinessModelDTO, Object> selectBusinessModel(String userId, String modelName) {

        return Result.success(businessModelService.selectModelByModelName(userId, modelName));
    }

    @Override
    public Result<ModelDatasourceDTO, Object> selectDatasource(String userId, String businessModelName) {


        return businessModelService.selectDatasource(userId, businessModelName);
    }

    @Override
    public Result<ModelDatasourceDTO, Object> selectDatasourceByModelName(String userId, String businessModelName, String modeType) {

        return  businessModelService.selectDatasourceByModelName(userId, businessModelName,modeType);
    }

    @Override
    public Result<List<BusinessModelMetaDataDTO>, Object> selectBusinessModelMetaData(String userId, String businessModelName) {
        return businessModelService.selectBusinessModelMetaData(userId, businessModelName);
    }


    @Override
    public Result<DatasourceDTO, Object> add(String userId, AddBusinessModelVO addBusinessModelVO) {
        try {

            return businessModelService.insert(userId, addBusinessModelVO);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(StatusCode.CODE_10010.getCode(), Boolean.FALSE, "新增失败");
        }
    }

    @Override
    public Result<Boolean, Object> delete(String userId, String businessModelName) {
        try {


            return businessModelService.delete(userId, businessModelName);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(StatusCode.CODE_10010.getCode(), Boolean.FALSE, "删除失败");
        }
    }

    @Override
    public Result selectAll(String userId, Integer curPage, Integer pageSize, String modelName, String group, String basicModelName) {

        return Result.success(businessModelService.selectAll(userId, modelName, group, basicModelName, curPage, pageSize));
    }

    @Override
    public Result update(String userId, UpdateBusinessModelVO updateBusinessModelVO) {
        try {
            if (StringUtils.isEmpty(updateBusinessModelVO.getSortNum())) {
                updateBusinessModelVO.setSortNum("99");
            }

            return businessModelService.update(userId, updateBusinessModelVO);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(StatusCode.CODE_10010.getCode(), Boolean.FALSE, "更新失败");
        }
    }

    @Override
    public Result<List<BasicModelGroupDTO>, Object> getBasicModelGroup(String userId) {

        return Result.success(businessModelService.getBasicModelGroup(userId));

    }

    @Override
    public Result<List<BasicModelNameAndCnameDTO>, Object> getBasicModels(String userId, String basicModelGroup) {


        return Result.success(businessModelService.getBasicModel(userId, basicModelGroup));

    }

    @Override
    public Result<List<BasicModelAndMetaDataDTO>, Object> getBasicModelColumns(String userId, String basicModel) {
        return Result.success(businessModelService.getBasicModelColumns(userId, basicModel));
    }

    @Override
    public Result<List<GroupDTO>, Object> getAllGroup(String userId) {

        return Result.success(businessModelService.getAllGroup(userId));
    }


    @Override
    public Result<List<BatchDownloadBusinessModelDTO>, Object> batchDownloadExcel(String userId, List<String> modelNames) {


        return Result.success(businessModelService.batchDownloadExcel(userId, modelNames));
    }

    @Override
    public Result<Boolean, Object> batchUploadExcel(String userId, HttpServletRequest request) {
        try {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            // 获取上传文件
            MultipartFile file = multipartRequest.getFile("file");

            InputStream inputStream = file.getInputStream();
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(inputStream);

            return importer.importBusinessModel(userId, xssfWorkbook);
        } catch (RuntimeException e) {
            return Result.fail(StatusCode.CODE_10010.getCode(), Boolean.FALSE, e.getMessage());
        } catch (IOException e) {
            return Result.fail(StatusCode.CODE_10010.getCode(), Boolean.FALSE, e.getMessage());
        }
    }

    @Override
    public Result<List<Map<String, Object>>, Object> testPriview(String userId, BusinessPreviewDataVO businessPreviewDataVO) {
        return businessModelService.testPriview(userId, businessPreviewDataVO);
    }

    @Override
    public Result<Boolean, Object> queryBusinessModelExists(String userId, String businessModelName, String businessModelGroup) {

        return businessModelService.queryBusinessModelExists(userId, businessModelName, businessModelGroup);
    }
}

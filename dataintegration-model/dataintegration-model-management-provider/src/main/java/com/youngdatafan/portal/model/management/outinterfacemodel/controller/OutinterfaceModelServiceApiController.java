package com.youngdatafan.portal.model.management.outinterfacemodel.controller;

// import com.datafan.dataintegration.core.util.StatusCode;
import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.dataintegration.core.util.StatusCode;
import com.youngdatafan.portal.model.management.datasource.dto.DatasourceDTO;
import com.youngdatafan.portal.model.management.outinterfacemodel.api.OutinterfaceModelServiceApi;
import com.youngdatafan.portal.model.management.outinterfacemodel.dto.*;
import com.youngdatafan.portal.model.management.outinterfacemodel.service.OutinterfaceModelService;
import com.youngdatafan.portal.model.management.outinterfacemodel.vo.AddOutinterfaceModelVO;
import com.youngdatafan.portal.model.management.outinterfacemodel.vo.OutinterfacePreviewDataVO;
import com.youngdatafan.portal.model.management.outinterfacemodel.vo.UpdateOutinterfaceModelVO;
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
@RequestMapping("/outinterfaceModel")
public class OutinterfaceModelServiceApiController implements OutinterfaceModelServiceApi {
    @Resource
    OutinterfaceModelService outinterfaceModelService;

    @Resource
    Importer importer;

    @Override
    public Result<List<OutinterfaceModelTypeAndGroupListDTO>, Object> selectModelAndGroups(String userId) {

        return Result.success(outinterfaceModelService.selectModelAndGroupList(userId));
    }

    @Override
    public Result<OutinterfaceModelDTO, Object> selectOutinterfaceModel(String userId, String modelName) {

        return Result.success(outinterfaceModelService.selectModelByModelName(userId, modelName));
    }

    @Override
    public Result<OutinterfaceModelDatasourceDTO, Object> selectDatasource(String userId, String outinterfaceModelName) {


        return outinterfaceModelService.selectDatasource(userId, outinterfaceModelName);
    }

    @Override
    public Result<OutinterfaceModelDatasourceDTO, Object> selectDatasourceByModelName(String userId, String outinterfaceModelName, String modeType) {

        return  outinterfaceModelService.selectDatasourceByModelName(userId, outinterfaceModelName,modeType);
    }

    @Override
    public Result<List<OutinterfaceModelMetaDataDTO>, Object> selectOutinterfaceModelMetaData(String userId, String outinterfaceModelName) {
        return outinterfaceModelService.selectOutinterfaceModelMetaData(userId, outinterfaceModelName);
    }


    @Override
    public Result<DatasourceDTO, Object> add(String userId, AddOutinterfaceModelVO addOutinterfaceModelVO) {
        try {

            return outinterfaceModelService.insert(userId, addOutinterfaceModelVO);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(StatusCode.CODE_10010.getCode(), Boolean.FALSE, "新增失败");
        }
    }

    @Override
    public Result<Boolean, Object> delete(String userId, String outinterfaceModelName) {
        try {


            return outinterfaceModelService.delete(userId, outinterfaceModelName);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(StatusCode.CODE_10010.getCode(), Boolean.FALSE, "删除失败");
        }
    }

    @Override
    public Result selectAll(String userId, Integer curPage, Integer pageSize, String modelName, String group, String basicModelName) {

        return Result.success(outinterfaceModelService.selectAll(userId, modelName, group, basicModelName, curPage, pageSize));
    }

    @Override
    public Result update(String userId, UpdateOutinterfaceModelVO updateOutinterfaceModelVO) {
        try {
            if (StringUtils.isEmpty(updateOutinterfaceModelVO.getSortNum())) {
                updateOutinterfaceModelVO.setSortNum("99");
            }

            return outinterfaceModelService.update(userId, updateOutinterfaceModelVO);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(StatusCode.CODE_10010.getCode(), Boolean.FALSE, "更新失败");
        }
    }

    @Override
    public Result<List<OutinterfaceModelGroupDTO>, Object> getBasicModelGroup(String userId) {

        return Result.success(outinterfaceModelService.getBasicModelGroup(userId));

    }

    @Override
    public Result<List<OutinterfaceModelNameAndCnameDTO>, Object> getBasicModels(String userId, String basicModelGroup) {


        return Result.success(outinterfaceModelService.getBasicModel(userId, basicModelGroup));

    }

    @Override
    public Result<List<OutinterfaceBasicModelAndMetaDataDTO>, Object> getBasicModelColumns(String userId, String basicModel) {
        return Result.success(outinterfaceModelService.getBasicModelColumns(userId, basicModel));
    }

    @Override
    public Result<List<OutinterfaceGroupDTO>, Object> getAllGroup(String userId) {

        return Result.success(outinterfaceModelService.getAllGroup(userId));
    }


    @Override
    public Result<List<BatchDownloadOutinterfaceModelDTO>, Object> batchDownloadExcel(String userId, List<String> modelNames) {


        return Result.success(outinterfaceModelService.batchDownloadExcel(userId, modelNames));
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
    public Result<List<Map<String, Object>>, Object> testPriview(String userId, OutinterfacePreviewDataVO outinterfacePreviewDataVO) {
        return outinterfaceModelService.testPriview(userId, outinterfacePreviewDataVO);
    }

    @Override
    public Result<Boolean, Object> queryOutinterfaceModelExists(String userId, String outinterfaceModelName, String outinterfaceModelGroup) {

        return outinterfaceModelService.queryOutinterfaceModelExists(userId, outinterfaceModelName, outinterfaceModelGroup);
    }
}

package com.youngdatafan.portal.model.management.outmodel.controller;

import com.youngdatafan.dataintegration.core.util.StatusCode;
import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.portal.model.management.outmodel.api.OutModelServiceApi;
import com.youngdatafan.portal.model.management.outmodel.dto.OutModelAndMetaDataListDTO;
import com.youngdatafan.portal.model.management.outmodel.dto.OutModelAndMetaDataWithPageDTO;
import com.youngdatafan.portal.model.management.outmodel.dto.OutModelGroupDTO;
import com.youngdatafan.portal.model.management.outmodel.dto.OutModelProjectIdAndNamesDTO;
import com.youngdatafan.portal.model.management.outmodel.service.OutModelService;
import com.youngdatafan.portal.model.management.outmodel.vo.AddOutModelVO;
import com.youngdatafan.portal.model.management.outmodel.vo.UpdateOutModelVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * <p>****************************************************************************</p>
 * <ul style="margin:15px;">
 * <li>Description : description</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2020/2/12 11:37 PM</li>
 * <li>Author      : ksice_xt</li>
 * </ul>
 * <p>****************************************************************************</p>
 */
@RestController
@RequestMapping("/outModel")
public class OutModelServiceApiController implements OutModelServiceApi {

    @Autowired
    OutModelService outModelService;


    @Override
    public Result<String, Object> add(String userId, AddOutModelVO addOutModelVO) {
        try {
            return Result.success(outModelService.add(userId, addOutModelVO));
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(StatusCode.CODE_10010.getCode(), Boolean.FALSE, e.getMessage());
        }
    }

    @Override
    public Result<Boolean, Object> delete(String userId, String[] outModelId) {
        try {
            List<String> list = Arrays.asList(outModelId);

            if (outModelService.delete(userId, list)) {
                return Result.success(true);
            } else {
                return Result.fail(StatusCode.CODE_10010.getCode(), Boolean.FALSE, "删除失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(StatusCode.CODE_10010.getCode(), Boolean.FALSE, "删除失败");
        }
    }

    @Override
    public Result<List<OutModelGroupDTO>, Object> getAllGroup(String userId) {
        return Result.success(outModelService.getGroups(userId));
    }

    @Override
    public Result<OutModelAndMetaDataWithPageDTO, Object> selectAllOutModel(String userId, Integer curPage, Integer pageSize, String groupId,
                                                                            String projectId, String modelName, String enabled) {
        return Result.success(outModelService.selectAllOutModel(userId, curPage, pageSize, groupId, projectId, modelName, enabled));
    }

    @Override
    public Result<List<OutModelProjectIdAndNamesDTO>, Object> getAllProject(String userId) {
        return Result.success(outModelService.getAllProject(userId));
    }

    @Override
    public Result<OutModelAndMetaDataListDTO, Object> selectOutModelAndMetaData(String userId, String modelName) {
        return Result.success(outModelService.selectOutModelAndMetaData(userId, modelName));
    }

    @Override
    public Result<Boolean, Object> updateAll(String userId, @Valid UpdateOutModelVO updateOutModelVO) {
        try {
            Boolean aBoolean = outModelService.updateAll(userId, updateOutModelVO);
            if (aBoolean) {
                return Result.success(aBoolean, "修改成功");
            } else {
                return Result.fail(StatusCode.CODE_10010.getCode(), aBoolean, "修改失败");
            }
        } catch (Exception e) {
            return Result.fail(StatusCode.CODE_10010.getCode(), false, "修改失败");
        }
    }

    @Override
    public Result<Boolean, Object> updateSelective(String userId, @Valid UpdateOutModelVO updateOutModelVO) {
        Boolean aBoolean = outModelService.updateSelective(userId, updateOutModelVO);
        if (aBoolean) {
            return Result.success(aBoolean, "修改成功");
        } else {
            return Result.fail(StatusCode.CODE_10010.getCode(), aBoolean, "修改失败");
        }
    }

    @Override
    public Result<Boolean, Object> outModelIsExist(String userId, String modelId) {
        Boolean aBoolean = outModelService.outModelIsExist(userId, modelId);
        return Result.success(aBoolean);
    }
}

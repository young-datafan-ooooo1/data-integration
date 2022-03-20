package com.youngdatafan.portal.model.management.modelgrant.controller;

import com.youngdatafan.dataintegration.core.util.StatusCode;
import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.portal.model.management.modelgrant.api.ModelGroupServiceApi;
import com.youngdatafan.portal.model.management.modelgrant.dto.ModelGrantGroupDTO;
import com.youngdatafan.portal.model.management.modelgrant.dto.ModelNameAndGroupNameAndTypes;
import com.youngdatafan.portal.model.management.modelgrant.service.ModelGroupService;
import com.youngdatafan.portal.model.management.modelgrant.vo.AddModelGroupVO;
import com.youngdatafan.portal.model.management.modelgrant.vo.UpdateModelGroupVO;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/modelGrantGroup")
public class ModeGroupServiceApiController implements ModelGroupServiceApi {

    @Autowired
    ModelGroupService modelGroupService;

    @Override
    public Result add(String userId, AddModelGroupVO addModelGroupVO) {


        if (modelGroupService.insert(userId, addModelGroupVO)) {
            return Result.success(addModelGroupVO);
        } else {
            return Result.fail(StatusCode.CODE_10010.getCode(), addModelGroupVO, "新增授权组失败");
        }
    }

    @Override
    public Result delete(String userId, String modelGrantGroupName) {
        try {
            return Result.success(modelGroupService.delete(userId, modelGrantGroupName));
        } catch (Exception e) {
            return Result.success(false);
        }
    }

    @Override
    public Result update(String userId, UpdateModelGroupVO updateModelGroupVO) {

        try {
            return modelGroupService.update(userId, updateModelGroupVO);
        } catch (Exception e) {
            return Result.success(false);
        }
    }

    @Override
    public Result selectAll(String userId, Integer curPage, Integer pageSize, String modelGrantGroupName) {
        PageInfo<ModelGrantGroupDTO> pageInfo = modelGroupService.selectAll(userId, curPage, pageSize, modelGrantGroupName);
        return Result.success(pageInfo);
    }

    @Override
    public Result<ModelNameAndGroupNameAndTypes, Object> selectAllBusinessModel(String userId, String modelName, String modelGroupName,
                                                                                String modelGroupType, Integer curPage,
                                                                                Integer pageSize) {
        ModelNameAndGroupNameAndTypes modelNameAndGroupNameAndTypes = modelGroupService.selectAllBusinessodel(userId, modelName, modelGroupName, modelGroupType,curPage,pageSize);
        return Result.success(modelNameAndGroupNameAndTypes);
    }

    @Override
    public Result<Boolean, Object> queryModelGroupNameIsExits(String userId, String modelGroupName) {
        Boolean b = modelGroupService.queryModelGroupNameIsExits(userId, modelGroupName);
        return Result.success(b);
    }
}

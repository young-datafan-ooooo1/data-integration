package com.youngdatafan.portal.model.management.modelgrant.controller;

import com.youngdatafan.dataintegration.core.util.StatusCode;
import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.portal.model.management.modelgrant.api.ModelGroupGrantUserServiceApi;
import com.youngdatafan.portal.model.management.modelgrant.dto.AddUserGrantGroupListDTO;
import com.youngdatafan.portal.model.management.modelgrant.dto.ModelGroupGrantUserDTO;
import com.youngdatafan.portal.model.management.modelgrant.service.ModelGroupGrantUserService;
import com.youngdatafan.portal.model.management.modelgrant.vo.AddModelGroupGrantUserVO;
import com.youngdatafan.portal.model.management.modelgrant.vo.UpdateModelGroupGrantUserVO;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>****************************************************************************</p>
 * <ul style="margin:15px;">
 * <li>Description : description</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2020/2/12 10:52 PM</li>
 * <li>Author      : ksice_xt</li>
 * </ul>
 * <p>****************************************************************************</p>
 */
@RestController
@RequestMapping("/modelGroupGrantUser")
public class ModelGroupGrantUserUserServiceApiController implements ModelGroupGrantUserServiceApi {

    @Autowired
    ModelGroupGrantUserService modelGroupGrantUserService;

    @Override
    public Result add(String userId, AddModelGroupGrantUserVO addModelGroupGrantUserVO) {
        try {
            return modelGroupGrantUserService.insert(userId, addModelGroupGrantUserVO);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(StatusCode.CODE_10010.getCode(), addModelGroupGrantUserVO, "新增失败");
        }
    }

    @Override
    public Result update(String userId, UpdateModelGroupGrantUserVO updateModelGroupGrantUserVO) {
        try {
            return modelGroupGrantUserService.update(userId, updateModelGroupGrantUserVO);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(StatusCode.CODE_10010.getCode(), updateModelGroupGrantUserVO, "更新失败");
        }
    }

    @Override
    public Result selectAll(String userId, String userName, Integer curPage, Integer pageSize, String modelGrantGroupName) {
        PageInfo<ModelGroupGrantUserDTO> pageInfo = modelGroupGrantUserService.selectAll(userId, userName, curPage, pageSize, modelGrantGroupName);
        return Result.success(pageInfo);
    }

    @Override
    public Result delete(String userId, String userGrantId) {
        try {
            return modelGroupGrantUserService.delete(userId, userGrantId);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(StatusCode.CODE_10010.getCode(), Boolean.FALSE, "删除失败");
        }
    }

    @Override
    public Result getUserIdAndName(String userId) {

        return Result.success(modelGroupGrantUserService.selectUserIdAndNames(userId));
    }

    @Override
    public Result<List<AddUserGrantGroupListDTO>, Object> getModelGrantGroup(String userId, String modelGroupName) {
        return Result.success(modelGroupGrantUserService.selectGroupModelNameAndModelList(userId, StringUtils.isEmpty(modelGroupName) ? null : modelGroupName));
    }
}

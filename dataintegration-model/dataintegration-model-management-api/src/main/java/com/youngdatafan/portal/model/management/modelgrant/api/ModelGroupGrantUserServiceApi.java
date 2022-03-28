package com.youngdatafan.portal.model.management.modelgrant.api;

import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.portal.model.management.modelgrant.dto.AddUserGrantGroupListDTO;
import com.youngdatafan.portal.model.management.modelgrant.vo.AddModelGroupGrantUserVO;
import com.youngdatafan.portal.model.management.modelgrant.vo.UpdateModelGroupGrantUserVO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * ModelGroupGrantUserServiceApi.
 */
public interface ModelGroupGrantUserServiceApi {

    /**
     * 添加用户授权.
     *
     * @param userId                   userId
     * @param addModelGroupGrantUserVO addModelGroupGrantUserVO
     * @return Result
     */
    @PostMapping(value = "/add")
    Result add(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
               @RequestBody AddModelGroupGrantUserVO addModelGroupGrantUserVO);

    /**
     * 修改用户授权.
     *
     * @param userId                      userId
     * @param updateModelGroupGrantUserVO updateModelGroupGrantUserVO
     * @return Result
     */

    @PutMapping(value = "/update")
    Result update(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                  @RequestBody UpdateModelGroupGrantUserVO updateModelGroupGrantUserVO);

    /**
     * 获取用户授权信息.
     *
     * @param userId              userId
     * @param userName            userName
     * @param curPage             curPage
     * @param pageSize            pageSize
     * @param modelGrantGroupName modelGrantGroupName
     * @return Result
     */
    @ApiOperation(value = "获取用户授权信息", notes = "获取用户授权信息", produces = "application/json")
    @GetMapping(value = "/selectAll")
    Result selectAll(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                     @ApiParam("用户名") @RequestParam(value = "userName", required = false) String userName,
                     @ApiParam("页码") @RequestParam(value = "curPage", defaultValue = "1") Integer curPage,
                     @ApiParam("行数") @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                     @ApiParam("授权组名称") @RequestParam(value = "modelGrantGroupName", required = false) String modelGrantGroupName);

    /**
     * 删除用户授权信息.
     *
     * @param userId      userId
     * @param userGrantId userGrantId
     * @return Result
     */
    @ApiOperation(value = "删除用户授权信息", notes = "删除用户授权信息", produces = "application/json")
    @DeleteMapping(value = "/delete/{userGrantId}")
    Result delete(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                  @ApiParam("用户授权id") @PathVariable("userGrantId") String userGrantId);

    /**
     * 查询用户信息.
     *
     * @param userId userId
     * @return Result
     */
    @ApiOperation(value = "查询用户信息", notes = "查询用户姓名和id", produces = "application/json")
    @GetMapping(value = "/selectAll/users")
    Result getUserIdAndName(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId);

    /**
     * 查询授权组信息.
     *
     * @param userId         userId
     * @param modelGroupName modelGroupName
     * @return Result
     */
    @ApiOperation(value = "查询授权组信息", notes = "查询用户姓名和id", produces = "application/json")
    @GetMapping(value = "/selectAll/modelGrantGroups")
    Result<List<AddUserGrantGroupListDTO>, Object> getModelGrantGroup(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                                                                      @ApiParam("模型授权组") @RequestParam(value = "modelGroupName", required = false) String modelGroupName);
}


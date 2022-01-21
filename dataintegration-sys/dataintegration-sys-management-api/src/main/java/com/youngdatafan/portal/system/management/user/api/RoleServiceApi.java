package com.youngdatafan.portal.system.management.user.api;

import com.datafan.dataintegration.core.model.Result;
import com.youngdatafan.portal.system.management.user.dto.RoleDTO;
import com.youngdatafan.portal.system.management.user.vo.RoleAddVo;
import com.youngdatafan.portal.system.management.user.vo.RoleUpdateVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: jeremychen
 * @Descripition:
 * @Date:2020/2/11 3:50 下午
 */
@Api(tags = "角色管理API接口")
public interface RoleServiceApi {

    @ApiOperation(value = "添加角色", notes = "添加一个角色", produces = "application/json")
    @PostMapping(value = "/add")
    Result<RoleDTO, Object> add(@Validated @RequestBody RoleAddVo roleAddVo, @ApiParam("操作用户Id") @RequestHeader("authorization-userId") String optUserId);


    @ApiOperation(value = "修改角色信息", notes = "修改角色信息", produces = "application/json")
    @PutMapping(value = "/update")
    Result<RoleDTO, Object> update(@Validated @RequestBody RoleUpdateVo roleUpdateVo, @ApiParam("操作用户Id") @RequestHeader("authorization-userId") String optUserId);

    @ApiOperation(value = "查询所有角色", notes = "查询所有角色", produces = "application/json")
    @GetMapping(value = "/selectAllRoles")
    Result<List<RoleDTO>, Object> selectAllRoles(@ApiParam("操作用户Id") @RequestHeader("authorization-userId") String optUserId);

    @ApiOperation(value = "查询所有角色(分页)", notes = "查询所有角色(分页)", produces = "application/json")
    @GetMapping(value = "/selectAllRolesPage")
    Result<PageInfo<RoleDTO>, Object> selectAllRolesPage(@RequestParam("pageSize") @ApiParam("每页条数") String pageSize, @RequestParam("curPage") @ApiParam("当前页") String curPage, @ApiParam("角色名称") @RequestParam("keyWord") String keyWord, @ApiParam("操作用户Id") @RequestHeader("authorization-userId") String optUserId);

    @ApiOperation(value = "授权菜单", notes = "授权菜单", produces = "application/json")
    @PostMapping(value = "/grantResouces")
    Result<Boolean, Object> grantResouces(@RequestParam("roleId") @ApiParam("角色id") String roleId, @RequestParam("resources") @ApiParam("资源id清单（以逗号分隔）") String resources);

    @ApiOperation(value = "删除角色", notes = "删除角色", produces = "application/json")
    @DeleteMapping(value = "/deleteRoles")
    Result<Boolean, Object> deleteRoles(@RequestParam("roleIds") @ApiParam("角色id（以逗号分隔）") String roleIds, @ApiParam("操作用户Id") @RequestHeader("authorization-userId") String optUserId);

    @ApiOperation(value = "查询用户已经授权到的角色", notes = "查询用户已经授权到的角色", produces = "application/json")
    @GetMapping(value = "/selectRoleIdsByUserId")
    Result<List<String>, Object> selectRoleIdsByUserId(@RequestParam("userId") String userId);
}

package com.youngdatafan.portal.system.management.user.api;

import com.github.pagehelper.PageInfo;
import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.portal.system.management.user.dto.ResourceDTO;
import com.youngdatafan.portal.system.management.user.vo.ResouceUpdateOrderVO;
import com.youngdatafan.portal.system.management.user.vo.ResourceAddVO;
import com.youngdatafan.portal.system.management.user.vo.ResourceUpdateVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 资源管理API接口.
 */
@Api(tags = "资源管理API接口")
public interface ResourceServiceApi {

    /**
     * 获取所有资源.
     *
     * @param optUserId optUserId
     * @return List
     */
    @ApiOperation(value = "获取所有资源", notes = "获取所有资源", produces = "application/json")
    @GetMapping(value = "/selectAllResource")
    Result<List<ResourceDTO>, Object> selectAllResource(@ApiParam("操作用户Id") @RequestHeader("authorization-userId") String optUserId);

    /**
     * 获取已授权资源.
     *
     * @param optUserId optUserId
     * @return List
     */
    @ApiOperation(value = "获取所有有效资源", notes = "获取所有有效资源", produces = "application/json")
    @GetMapping(value = "/selectAllValidResource")
    Result<List<ResourceDTO>, Object> selectAllValidResource(@ApiParam("操作用户Id") @RequestHeader("authorization-userId") String optUserId);

    /**
     * 获取已授权资源.
     *
     * @param userId optUserId
     * @return List
     */
    @ApiOperation(value = "获取已授权资源", notes = "获取已授权资源", produces = "application/json")
    @GetMapping(value = "/selectAllGrantedResource")
    Result<List<ResourceDTO>, Object> selectAllGrantedResource(@ApiParam("用户id") @RequestHeader("authorization-userId") String userId);

    /**
     * 添加资源.
     *
     * @param resourceAddVO resourceAddVO
     * @param optUserId     optUserId
     * @return List
     */
    @ApiOperation(value = "添加资源", notes = "添加资源", produces = "application/json")
    @PostMapping(value = "/add")
    Result<ResourceDTO, Object> add(@RequestBody ResourceAddVO resourceAddVO, @ApiParam("操作用户Id") @RequestHeader("authorization-userId") String optUserId);

    /**
     * 修改资源.
     *
     * @param resourceUpdateDTO resourceUpdateDTO
     * @param optUserId         optUserId
     * @return List
     */
    @ApiOperation(value = "修改资源", notes = "修改资源", produces = "application/json")
    @PutMapping(value = "/update")
    Result<ResourceDTO, Object> update(@RequestBody ResourceUpdateVO resourceUpdateDTO, @ApiParam("操作用户Id") @RequestHeader("authorization-userId") String optUserId);

    /**
     * 删除资源.
     *
     * @param optUserId optUserId
     * @param resId     resId
     * @return List
     */
    @ApiOperation(value = "删除资源", notes = "删除资源", produces = "application/json")
    @PutMapping(value = "/deleteResource")
    Result<Boolean, Object> deleteResource(@ApiParam("资源id") @RequestParam("resId") String resId, @ApiParam("操作用户Id") @RequestHeader("authorization-userId") String optUserId);

    /**
     * 根据角色id获取已经授权资源.
     *
     * @param roleId roleId
     * @return List
     */
    @ApiOperation(value = "根据角色id获取已经授权资源", notes = "根据角色id获取已经授权资源", produces = "application/json")
    @GetMapping(value = "/selectGrantedResources")
    Result<List<ResourceDTO>, Object> selectGrantedResources(@ApiParam("角色Id") @RequestParam("roleId") String roleId);

    /**
     * 检查资源名称是否存在.
     *
     * @param resName resName
     * @return List
     */
    @ApiOperation(value = "检查资源名称是否存在", notes = "检查资源名称是否存在", produces = "application/json")
    @GetMapping(value = "/checkResourceName")
    Result<Boolean, Object> checkResourceName(@ApiParam("资源名称") @RequestParam("resName") String resName);

    /**
     * 资源分页查询.
     *
     * @param pageSize pageSize
     * @param curPage  curPage
     * @param resName  resName
     * @return PageInfo
     */
    @ApiOperation(value = "资源分页查询", notes = "资源分页查询", produces = "application/json")
    @GetMapping(value = "/selectResourceByResNamePage")
    Result<PageInfo<ResourceDTO>, Object> selectResourceByResNamePage(@RequestParam("pageSize") @ApiParam("每页条数") String pageSize, @RequestParam("curPage") @ApiParam("当前页") String curPage,
                                                                      @RequestParam("resName") @ApiParam("资源名称") String resName);

    /**
     * 查询所有资源树.
     *
     * @param userId optUserId
     * @return List
     */
    @ApiOperation(value = "查询所有资源树", notes = "查询所有资源树", produces = "application/json")
    @GetMapping(value = "/selectResourceTree")
    Result<List<ResourceDTO>, Object> selectResourceTree(@ApiParam("操作用户Id") @RequestHeader("authorization-userId") String userId);

    /**
     * 查询门户的所有资源树.
     *
     * @param userId optUserId
     * @return List
     */
    @ApiOperation(value = "查询门户的所有资源树", notes = "查询门户的所有资源树", produces = "application/json")
    @GetMapping(value = "/selectResourceTreeForPortal")
    Result<List<ResourceDTO>, Object> selectResourceTreeForPortal(@ApiParam("操作用户Id") @RequestHeader("authorization-userId") String userId);

    /**
     * 查询子平台的所有资源树.
     *
     * @param parentResId parentResId
     * @param userId      optUserId
     * @return List
     */
    @ApiOperation(value = "查询子平台的所有资源树", notes = "查询子平台的所有资源树", produces = "application/json")
    @GetMapping(value = "/selectResourceTreeForSubPlatform")
    Result<List<ResourceDTO>, Object> selectResourceTreeForSubPlatform(@ApiParam("操作用户Id") @RequestHeader("authorization-userId") String userId,
                                                                       @RequestParam("parentResId") @ApiParam("父菜单id") String parentResId);

    /**
     * 更新菜单顺序.
     *
     * @param roleId roleId
     * @return List
     */
    @ApiOperation(value = "根据角色id获取已经授权资源树", notes = "根据角色id获取已经授权资源树", produces = "application/json")
    @GetMapping(value = "/selectGrantedResourcesTree")
    Result<List<ResourceDTO>, Object> selectGrantedResourcesTree(@ApiParam("角色Id") @RequestParam("roleId") String roleId);


    /**
     * 更新菜单顺序.
     *
     * @param resouceUpdateOrderVOS resouceUpdateOrderVOS
     * @return List
     */
    @ApiOperation(value = "更新菜单顺序", notes = "更新菜单顺序", produces = "application/json")
    @PutMapping(value = "/updateResourceOrder")
    Result<Boolean, Object> updateResourceOrder(@RequestBody @Validated List<ResouceUpdateOrderVO> resouceUpdateOrderVOS);

}



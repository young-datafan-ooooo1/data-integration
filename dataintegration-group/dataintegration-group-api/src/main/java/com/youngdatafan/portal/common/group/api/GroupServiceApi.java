package com.youngdatafan.portal.common.group.api;

import com.github.pagehelper.PageInfo;
import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.portal.common.group.dto.DpPortalGroupDTO;
import com.youngdatafan.portal.common.group.vo.GroupAddVO;
import com.youngdatafan.portal.common.group.vo.GroupUkVO;
import com.youngdatafan.portal.common.group.vo.GroupUpdateVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 分组服务api声明.
 *
 * @author gavin
 * @since 2020/2/10 12:41 下午
 */
@Api(tags = "分组API接口")
public interface GroupServiceApi {

    /**
     * 查询分组是否重复.
     *
     * @param groupUkVO ModelGroupUkVO
     * @param userId    用户id
     * @return 接口应答
     */
    @ApiOperation(value = "查询分组是否重复", notes = "返回true or false", produces = "application/json")
    @PostMapping(value = "/queryGroupExists")
    Result queryGroupExists(@Validated @RequestBody GroupUkVO groupUkVO,
                            @RequestHeader(value = "authorization-userId") String userId);

    /**
     * 新增分组.
     *
     * @param groupAddVO modelGroupAddVO
     * @param userId     用户id
     * @return 接口应答
     */
    @ApiOperation(value = "新增分组", produces = "application/json")
    @PostMapping(value = "/add")
    Result<DpPortalGroupDTO, Object> add(@Validated @RequestBody GroupAddVO groupAddVO, @RequestHeader(value = "authorization-userId") String userId);

    /**
     * 修改分组.
     *
     * @param groupUpdateVO modelGroupUpdateVO
     * @return 接口应答
     */
    @ApiOperation(value = "修改分组", notes = "参数对象中如果有null值，数据库会被更新成null", produces = "application/json")
    @PutMapping(value = "/update")
    Result<Boolean, Object> update(@Validated @RequestBody GroupUpdateVO groupUpdateVO);

    /**
     * 修改分组.
     *
     * @param groupUpdateVO modelGroupUpdateVO
     * @return 接口应答
     */
    @ApiOperation(value = "修改分组，选择性的", notes = "参数对象中如果有null值，数据库将不会被更新", produces = "application/json")
    @PutMapping(value = "/updateSelective")
    Result<Boolean, Object> updateSelective(@Validated @RequestBody GroupUpdateVO groupUpdateVO);

    /**
     * 批量删除分组.
     *
     * @param groupIds 分组id
     * @return 接口应答
     */
    @ApiOperation(value = "批量删除分组", notes = "根据id删除分组", produces = "application/json")
    @DeleteMapping(value = "/deleteBatch")
    Result<Boolean, Object> deleteBatch(@RequestParam("groupIds") String[] groupIds);

    /**
     * 删除分组.
     *
     * @param groupId 分组id
     * @return 接口应答
     */
    @ApiOperation(value = "删除分组", notes = "根据id删除分组", produces = "application/json")
    @DeleteMapping(value = "/delete/{groupId}")
    Result<Boolean, Object> delete(@PathVariable("groupId") String groupId);

    /**
     * 查询所有分组.
     *
     * @param userId   用户id
     * @param pageNum  页数
     * @param pageSize 每页记录数
     * @return 接口应答
     */
    @ApiOperation(value = "根据分组类型查询", produces = "application/json")
    @GetMapping(value = "/selectAll")
    Result<PageInfo<DpPortalGroupDTO>, Object> selectAll(
        @RequestHeader(value = "authorization-userId") String userId,
        @ApiParam("页数") @RequestParam int pageNum, @ApiParam("每页记录数") @RequestParam int pageSize);

    /**
     * 根据分组类型查询.
     *
     * @param userId    用户id
     * @param groupType 分组类型
     * @param pageNum   页数
     * @param pageSize  每页记录数
     * @return 接口应答
     */
    @ApiOperation(value = "根据分组类型查询", produces = "application/json")
    @GetMapping(value = "/selectBy/{groupType}")
    Result<PageInfo<DpPortalGroupDTO>, Object> selectByGroupType(
        @RequestHeader(value = "authorization-userId") String userId,
        @ApiParam("分组类型，多个逗号分隔") @PathVariable("groupType") String groupType,
        @ApiParam("页数") @RequestParam int pageNum, @ApiParam("每页记录数") @RequestParam int pageSize);

    /**
     * 根据分组名称或备注模糊查询指定分组类型下的所有分组.
     *
     * @param userId    用户id
     * @param groupName 分组名称
     * @param groupType 分组类型
     * @param pageNum   页数
     * @param pageSize  每页记录数
     * @return 接口应答
     */
    @ApiOperation(value = "根据分组名称或备注模糊查询指定分组类型下的所有分组", produces = "application/json")
    @GetMapping(value = "/selectLikeBy/{groupType}")
    Result<PageInfo<DpPortalGroupDTO>, Object> selectLikeByGroupType(
        @RequestHeader(value = "authorization-userId") String userId,
        @ApiParam("分组类型，多个逗号分隔") @PathVariable("groupType") String groupType,
        @ApiParam("分组名称，模糊查询") @RequestParam(value = "groupName", required = false) String groupName,
        @ApiParam("页数") @RequestParam int pageNum, @ApiParam("每页记录数") @RequestParam int pageSize);

}


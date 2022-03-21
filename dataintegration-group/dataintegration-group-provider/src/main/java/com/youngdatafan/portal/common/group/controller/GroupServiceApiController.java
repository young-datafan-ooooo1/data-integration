package com.youngdatafan.portal.common.group.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.portal.common.group.api.GroupServiceApi;
import com.youngdatafan.portal.common.group.dto.DpPortalGroupDTO;
import com.youngdatafan.portal.common.group.entity.DpPortalGroup;
import com.youngdatafan.portal.common.group.service.GroupService;
import com.youngdatafan.portal.common.group.vo.GroupAddVO;
import com.youngdatafan.portal.common.group.vo.GroupUkVO;
import com.youngdatafan.portal.common.group.vo.GroupUpdateVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 分组接口控制器.
 *
 * @author gavin
 * @since 2020/2/10 1:13 下午
 */
@RestController
@RequestMapping("/group")
public class GroupServiceApiController implements GroupServiceApi {

    private final GroupService groupService;

    @Autowired
    public GroupServiceApiController(GroupService groupService) {
        this.groupService = groupService;
    }

    @Override
    public Result<Boolean, Object> queryGroupExists(@Validated @RequestBody GroupUkVO groupUkVO,
                                                    @RequestHeader(value = "authorization-userId") String userId) {
        DpPortalGroup dpPortalGroup = new DpPortalGroup();
        // 创建数据库实体对象
        BeanUtils.copyProperties(groupUkVO, dpPortalGroup);
        // 设置当前用户id
        dpPortalGroup.setCreateUserId(userId);
        // 查询数据库
        return Result.success(groupService.queryGroupExists(dpPortalGroup));
    }

    @Override
    public Result<DpPortalGroupDTO, Object> add(@Validated @RequestBody GroupAddVO groupAddVO,
                                                @RequestHeader(value = "authorization-userId") String userId) {
        // 添加组
        return Result.success(groupService.addModelGroup(groupAddVO, userId));
    }

    @Override
    public Result<Boolean, Object> update(@Validated @RequestBody GroupUpdateVO groupUpdateVO) {
        // 更新组
        return Result.success(groupService.updateModelGroup(groupUpdateVO) > 0);
    }

    @Override
    public Result<Boolean, Object> updateSelective(@Validated @RequestBody GroupUpdateVO groupUpdateVO) {
        // 更新组
        return Result.success(groupService.updateModelGroupSelective(groupUpdateVO) > 0);
    }

    @Override
    public Result<Boolean, Object> delete(@PathVariable("groupId") String groupId) {
        // 删除组
        return Result.success(groupService.delete(groupId) > 0);
    }

    @Override
    public Result<PageInfo<DpPortalGroupDTO>, Object> selectAll(String userId, int pageNum, int pageSize) {
        // 设置分页规则
        PageHelper.startPage(pageNum, pageSize);
        // 返回所有分页信息参数为查询所有记录的信息
        PageInfo<DpPortalGroupDTO> pageInfo = new PageInfo<>(groupService.selectAll(userId));

        return Result.success(pageInfo);
    }

    @Override
    public Result<Boolean, Object> deleteBatch(@RequestParam("groupIds") String[] groupIds) {
        groupService.deleteBath(groupIds);
        // 删除组
        return Result.success(true);
    }

    @Override
    public Result<PageInfo<DpPortalGroupDTO>, Object> selectByGroupType(@RequestHeader(value = "authorization-userId") String userId,
                                                                        @PathVariable("groupType") String groupType,
                                                                        @RequestParam int pageNum, @RequestParam int pageSize) {
        // 设置分页规则
        PageHelper.startPage(pageNum, pageSize);
        // 返回所有分页信息参数为查询所有记录的信息
        PageInfo<DpPortalGroupDTO> pageInfo = new PageInfo<>(groupService.selectByGroupType(userId, groupType));

        return Result.success(pageInfo);
    }

    @Override
    public Result<PageInfo<DpPortalGroupDTO>, Object> selectLikeByGroupType(@RequestHeader(value = "authorization-userId") String userId,
                                                                            @PathVariable("groupType") String groupType, @RequestParam(value = "groupName", required = false) String groupName,
                                                                            @RequestParam int pageNum, @RequestParam int pageSize) {
        // 设置分页规则
        PageHelper.startPage(pageNum, pageSize);
        // 返回所有分页信息参数为查询所有记录的信息
        PageInfo<DpPortalGroupDTO> pageInfo = new PageInfo<>(groupService.selectLikeByGroupType(userId, groupType, groupName));

        return Result.success(pageInfo);
    }
}

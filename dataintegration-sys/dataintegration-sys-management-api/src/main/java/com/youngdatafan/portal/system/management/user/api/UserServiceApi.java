package com.youngdatafan.portal.system.management.user.api;

import com.github.pagehelper.PageInfo;
import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.portal.system.management.user.dto.RoleDTO;
import com.youngdatafan.portal.system.management.user.dto.UserDTO;
import com.youngdatafan.portal.system.management.user.dto.UserRoleDTO;
import com.youngdatafan.portal.system.management.user.vo.LoginVo;
import com.youngdatafan.portal.system.management.user.vo.UpdatePasswdVo;
import com.youngdatafan.portal.system.management.user.vo.UserAddVO;
import com.youngdatafan.portal.system.management.user.vo.UserUpdateVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 用户操作相关接口.
 *
 * @author gavin
 * @since 2020-01-09 16:39
 */
@Api(tags = "用户管理API接口")
public interface UserServiceApi {

    /**
     * 登录.
     *
     * @param loginVo loginVo
     * @return String
     */
    @ApiOperation(value = "登录", notes = "通过用户名密码登录", produces = "application/json")
    @PostMapping(value = "/login")
    Result<String, Object> login(@Validated @RequestBody LoginVo loginVo);

    /**
     * 添加用户.
     *
     * @param optUserId optUserId
     * @param userAddVO 用户属性
     * @return 接口应答
     */
    @ApiOperation(value = "添加用户", notes = "新增一个用户", produces = "application/json")
    @PostMapping(value = "/add")
    Result<UserDTO, Object> add(@Validated @RequestBody UserAddVO userAddVO, @ApiParam("操作用户id") @RequestHeader(value = "authorization-userId", required = true) String optUserId);

    /**
     * 修改用户.
     *
     * @param optUserId    *
     * @param userUpdateVO 用户属性
     * @return 接口应答
     */
    @ApiOperation(value = "修改用户", notes = "修改用户的信息】", produces = "application/json")
    @PutMapping(value = "/updateUser")
    Result<UserDTO, Object> updateUser(@Validated @RequestBody UserUpdateVO userUpdateVO, @ApiParam("操作用户id") @RequestHeader(value = "authorization-userId", required = true) String optUserId);

    /**
     * 删除用户(批量）.
     *
     * @param userIds   userIds
     * @param optUserId optUserId
     * @return 接口应答
     */
    @ApiOperation(value = "删除用户", notes = "根据用id删除用户", produces = "application/json")
    @DeleteMapping(value = "/deleteByUserIds")
    Result<Boolean, Object> deleteByUserIds(@RequestParam("userIds") @ApiParam("用户Id(以逗号分隔)") String userIds,
                                            @ApiParam("操作用户id") @RequestHeader(value = "authorization-userId", required = true) String optUserId);

    /**
     * 查询所有用户.
     *
     * @param pageSize  pageSize
     * @param curPage   curPage
     * @param keyWord   keyWord
     * @param optUserId optUserId
     * @return PageInfo
     */
    @ApiOperation(value = "查询用户", notes = "获取所有用户信息(分页)", produces = "application/json")
    @GetMapping(value = "/selectUsersPage")
    Result<PageInfo<UserDTO>, Object> selectUsersPage(@RequestParam("pageSize") @ApiParam("每页条数") String pageSize, @RequestParam("curPage") @ApiParam("当前页") String curPage,
                                                      @RequestParam("keyWord") @ApiParam("用户中文名或者登录名") String keyWord,
                                                      @ApiParam("操作用户id") @RequestHeader(value = "authorization-userId", required = true) String optUserId);


    /**
     * 授权角色.
     *
     * @param userId  用户id
     * @param roleIds 角色id清单
     * @return boolean
     */
    @ApiOperation(value = "授权角色", notes = "授权角色", produces = "application/json")
    @PostMapping(value = "/grantRoleToUser")
    Result<Boolean, Object> grantRoleToUser(@ApiParam("用户id") @RequestParam("userId") String userId, @ApiParam("角色id清单") @RequestParam("roleIds") String roleIds);

    /**
     * 查询已授权角色ID.
     *
     * @param userId userId
     * @return List
     */
    @ApiOperation(value = "查询已授权角色ID", notes = "查询已授权角色ID", produces = "application/json")
    @GetMapping(value = "/selectGrantedRoleIds")
    Result<List<String>, Object> selectGrantedRoleIds(@ApiParam("用户id") @RequestParam("userId") String userId);

    /**
     * 查询已授权角色.
     *
     * @param userId userId
     * @return List
     */
    @ApiOperation(value = "查询已授权角色", notes = "查询已授权角色", produces = "application/json")
    @GetMapping(value = "/selectGrantedRoles")
    Result<List<RoleDTO>, Object> selectGrantedRoles(@ApiParam("用户id") @RequestParam("userId") String userId);


    /**
     * 重置密码.
     *
     * @param userId    userId
     * @param optUserId optUserId
     * @return Boolean
     */
    @ApiOperation(value = "重置密码", notes = "重置密码", produces = "application/json")
    @PutMapping(value = "/resetPassword")
    Result<Boolean, Object> resetPassword(@ApiParam("用户id") @RequestParam("userId") String userId,
                                          @ApiParam("操作用户id") @RequestHeader(value = "authorization-userId", required = true) String optUserId);

    /**
     * 修改密码.
     *
     * @param updatePasswdVo updatePasswdVo
     * @param optUserId      optUserId
     * @param optUserName    optUserName
     * @return Boolean
     */
    @ApiOperation(value = "修改密码", notes = "修改密码", produces = "application/json")
    @PutMapping(value = "/updatePassword")
    Result<Boolean, Object> updatePassword(@RequestBody UpdatePasswdVo updatePasswdVo, @ApiParam("操作用户id") @RequestHeader(value = "authorization-userId", required = true) String optUserId,
                                           @ApiParam("操作用户名称") @RequestHeader(value = "authorization-userName", required = true) String optUserName);

    /**
     * 根据用户名查询用户 .
     *
     * @param userName userName
     * @return 用户信息
     */
    @ApiOperation(value = "根据用户名查询用户", notes = "根据用户名查询用户", produces = "application/json")
    @GetMapping(value = "/selectByUserName")
    Result<UserDTO, Object> selectByUserName(@RequestParam("userName") String userName);


    /**
     * 根据用户名查询用户.
     *
     * @param userName userName
     * @return 用户信息
     */
    @ApiOperation(value = "根据用户名查询用户登录信息", notes = "根据用户名查询用户", produces = "application/json")
    @GetMapping(value = "/selectLoginInfoByUserName")
    Result<UserDTO, Object> selectLoginInfoByUserName(@RequestParam("userName") String userName);

    /**
     * 锁定用户 .
     *
     * @param userId userId
     * @return boolean
     */
    @ApiOperation(value = "锁定用户", notes = "锁定用户", produces = "application/json")
    @PutMapping(value = "/lockUser")
    Result<Boolean, Object> lockUser(@RequestParam(value = "userId", required = true) String userId);


    /**
     * 解锁用户.
     *
     * @param userId userId
     * @return Boolean
     */
    @ApiOperation(value = "解锁用户", notes = "解锁用户", produces = "application/json")
    @PutMapping(value = "/unLockUser")
    Result<Boolean, Object> unLockUser(@RequestParam(value = "userId", required = true) String userId);

    /**
     * 更新上次登录时间.
     *
     * @param userId    userId
     * @param loginTime loginTime
     * @return Boolean
     */
    @ApiOperation(value = "更新上次登录时间", notes = "更新上次登录时间", produces = "application/json")
    @PutMapping(value = "/updateLastLoginTime")
    Result<Boolean, Object> updateLastLoginTime(@RequestParam(value = "userId", required = true) String userId, @RequestParam(value = "loginTime", required = true) String loginTime);

    /**
     * 根据用户名查询用户角色信息.
     *
     * @param userName userName
     * @return List
     */
    @ApiOperation(value = "根据用户名查询用户角色信息", notes = "更新上次登录时间", produces = "application/json")
    @GetMapping(value = "/selectUserRoleByUserName")
    Result<List<UserRoleDTO>, Object> selectUserRoleByUserName(@RequestParam("userName") String userName);

    /**
     * 查询审核人.
     *
     * @param optUserId optUserId
     * @return List
     */
    @ApiOperation(value = "查询审核人", notes = "查询审核人", produces = "application/json")
    @GetMapping(value = "/selectAuditingUser")
    Result<List<UserDTO>, Object> selectAuditingUser(@ApiParam("操作用户id") @RequestHeader(value = "authorization-userId", required = true) String optUserId);

    /**
     * 查询用户是否存在.
     *
     * @param userName userName
     * @return Boolean
     */
    @ApiOperation(value = "查询用户是否存在", notes = "查询用户是否存在", produces = "application/json")
    @GetMapping(value = "/checkUserName")
    Result<Boolean, Object> checkUserName(@ApiParam("用户名") @RequestParam("userName") String userName);
}

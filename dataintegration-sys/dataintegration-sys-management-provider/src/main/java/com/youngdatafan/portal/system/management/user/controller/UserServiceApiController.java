package com.youngdatafan.portal.system.management.user.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.dataintegration.core.util.StatusCode;
import com.youngdatafan.dataintegration.core.util.UUIDUtils;
import com.youngdatafan.portal.system.management.common.utils.BaseController;
import com.youngdatafan.portal.system.management.common.utils.JwtUtil;
import com.youngdatafan.portal.system.management.common.utils.SM3Utils;
import com.youngdatafan.portal.system.management.user.api.UserServiceApi;
import com.youngdatafan.portal.system.management.user.dto.RoleDTO;
import com.youngdatafan.portal.system.management.user.dto.UserDTO;
import com.youngdatafan.portal.system.management.user.dto.UserRoleDTO;
import com.youngdatafan.portal.system.management.user.entity.DpPortalRole;
import com.youngdatafan.portal.system.management.user.entity.DpPortalUser;
import com.youngdatafan.portal.system.management.user.entity.DpPortalUserRole;
import com.youngdatafan.portal.system.management.user.entity.UserRoleEntity;
import com.youngdatafan.portal.system.management.user.service.DpPortalRoleService;
import com.youngdatafan.portal.system.management.user.service.DpPortalUserRoleService;
import com.youngdatafan.portal.system.management.user.service.DpPortalUserService;
import com.youngdatafan.portal.system.management.user.vo.LoginVo;
import com.youngdatafan.portal.system.management.user.vo.UpdatePasswdVo;
import com.youngdatafan.portal.system.management.user.vo.UserAddVO;
import com.youngdatafan.portal.system.management.user.vo.UserUpdateVO;
import io.swagger.annotations.ApiParam;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户管理api接口实现.
 *
 * @author gavin
 * @since 2020-01-09 17:37
 */
@RestController
@RequestMapping("/user")
public class UserServiceApiController extends BaseController<UserDTO> implements UserServiceApi {

    @Value("${jwt.expireTime}")
    private Long expireTime;

    @Value("${jwt.tokenSecret}")
    private String tokenSecret;

    @Autowired
    private DpPortalUserService dpPortalUserService;

    @Autowired
    private DpPortalUserRoleService dpPortalUserRoleService;

    @Autowired
    private DpPortalRoleService dpPortalRoleService;

    @Override
    @Transactional
    public Result<String, Object> login(@Validated @RequestBody LoginVo loginVo) {

        DpPortalUser dpPortalUser = dpPortalUserService.selectByUserName(loginVo.getUserName());

        if (dpPortalUser.getStatus().equals("1")) {
            return Result.fail(StatusCode.CODE_10010.getCode(), null, "用户已锁定");
        }

        List<String> roles = dpPortalUserRoleService.selectRoleIdsByUserId(dpPortalUser.getUserId());

        if (dpPortalUser != null && loginVo.getPassword() != null && SM3Utils.encode(loginVo.getPassword()).equals(dpPortalUser.getUserPasswd())) {
            dpPortalUser.setLastLoginTime(new Date());
            dpPortalUserService.updateByPrimaryKey(dpPortalUser);
            UserDTO userDTO = new UserDTO();
            userDTO.setUserId(dpPortalUser.getUserId());
            userDTO.setUserName(dpPortalUser.getUserName());
            userDTO.setDescribe(dpPortalUser.getDescribe());
            String jwt = JwtUtil.sign(tokenSecret, expireTime, dpPortalUser.getUserName(), dpPortalUser.getUserId(), roles);
            Result result = Result.success(jwt);
            result.setAttachment(userDTO);
            dpPortalUserService.resetPasswordErrLongTime(loginVo.getUserName());
            return result;
        } else {
            if (dpPortalUser != null) {
                dpPortalUserService.addPasswordErrLongTime(loginVo.getUserName());

                int ret = dpPortalUserService.getPasswordErrLongTime(loginVo.getUserName());
                //如果大于五次锁定用户
                if (ret >= 5) {
                    dpPortalUser.setStatus("1");
                    dpPortalUser.setUpdateTime(new Date());
                    dpPortalUserService.updateByPrimaryKey(dpPortalUser);
                }
            }
            return Result.fail(StatusCode.CODE_10010.getCode(), null, "用户名或密码错误");

        }
    }

    @Override
    @Transactional
    public Result<UserDTO, Object> add(@Validated @RequestBody UserAddVO userVO, @RequestHeader(value = "authorization-userId", required = true) String optUserId) {

        Integer integer = dpPortalUserService.checkExistsByUserName(userVO.getUserName());
        if (integer != null && integer.equals(1)) {
            return Result.fail(StatusCode.CODE_10010.getCode(), null, "用户已存在");
        }
        DpPortalUser dpPortalUser = new DpPortalUser();
        dpPortalUser.setUserId(UUIDUtils.generateUUID());
        dpPortalUser.setUserName(userVO.getUserName());
        dpPortalUser.setUserPasswd(SM3Utils.encode("Prime@2020"));
        dpPortalUser.setUserMobile(userVO.getUserMobile());
        dpPortalUser.setUserEmail(userVO.getUserEmail());
        dpPortalUser.setDescribe(userVO.getDescribe());
        dpPortalUser.setStatus(userVO.getStatus());
        dpPortalUser.setCreateTime(new Date());
        dpPortalUser.setCreateUserId(optUserId);

        // 保存到数据库
        dpPortalUserService.insert(dpPortalUser);

        // 创建dto返回对象
        UserDTO userDTO = new UserDTO();
        // 属性复制
        BeanUtils.copyProperties(dpPortalUser, userDTO);

        return Result.success(userDTO);
    }

    @Override
    @Transactional
    public Result<UserDTO, Object> updateUser(@Validated @RequestBody UserUpdateVO userUpdateVO, @RequestHeader(value = "authorization-userId", required = true) String optUserId) {
        DpPortalUser dpPortalUser = new DpPortalUser();
        dpPortalUser.setUserName(userUpdateVO.getUserName());
        dpPortalUser.setUserMobile(userUpdateVO.getUserMobile());
        dpPortalUser.setUserEmail(userUpdateVO.getUserEmail());
        dpPortalUser.setDescribe(userUpdateVO.getDescribe());
        dpPortalUser.setStatus(userUpdateVO.getStatus());
        dpPortalUser.setUpdateTime(new Date());
        dpPortalUser.setCreateUserId(optUserId);

        // 更新
        int i = dpPortalUserService.updateByUserNameSelective(dpPortalUser);
        if (userUpdateVO.getStatus().equals("0") && i > 0) {
            dpPortalUserService.resetPasswordErrLongTime(userUpdateVO.getUserName());
        }
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(dpPortalUser, userDTO);
        return Result.success(userDTO);
    }

    @Override
    @Transactional
    public Result<Boolean, Object> deleteByUserIds(@RequestParam("userIds") String userIds, @RequestHeader(value = "authorization-userId", required = true) String optUserId) {
        // 删除用户
        List<String> strings = Arrays.asList(userIds.split(","));

        int i = dpPortalUserService.deleteByUserIds(strings, optUserId);
        if (i > 0) {
            //级联删除
            dpPortalUserRoleService.deleteByUserIds(strings);
        }
        // 所影响记录大于0说明删除成功
        return Result.success(i > 0);
    }

    @Override
    public Result<PageInfo<UserDTO>, Object> selectUsersPage(@RequestParam("pageSize") String pageSize, @RequestParam("curPage") String curPage, @RequestParam("keyWord") String keyWord,
                                                             @ApiParam("操作用户id") @RequestHeader(value = "authorization-userId", required = true) String optUserId) {
        Page page = this.initPage(pageSize, curPage);
        PageInfo<UserDTO> dpPortalUserPageInfo = dpPortalUserService.selectUsers(keyWord, page, optUserId);

        return Result.success(dpPortalUserPageInfo);
    }

    @Override
    @Transactional
    public Result<Boolean, Object> grantRoleToUser(@RequestParam("userId") String userId, @RequestParam("roleIds") String roleIds) {

        if ("00000000".equals(userId)) {
            return Result.fail(StatusCode.CODE_10010.getCode(), null, "管理员不能授权");

        }
        dpPortalUserRoleService.deleteByUserId(userId);
        List<DpPortalUserRole> dpPortalUserRoles = new ArrayList<>();
        String[] roles = roleIds.split(",");

        for (int i = 0; i < roles.length; i++) {
            DpPortalUserRole dpPortalUserRole = new DpPortalUserRole();
            dpPortalUserRole.setRoleId(roles[i]);
            dpPortalUserRole.setUserId(userId);
            dpPortalUserRoles.add(dpPortalUserRole);
        }
        int i = dpPortalUserRoleService.batchInsert(dpPortalUserRoles);
        return Result.success(i > 0);
    }

    /**
     * selectGrantedRoleIds.
     *
     * @param userId userId
     * @return Result
     */
    @Override
    public Result<List<String>, Object> selectGrantedRoleIds(String userId) {
        return Result.success(dpPortalUserRoleService.selectRoleIdsByUserId(userId));
    }

    @Override
    public Result<List<RoleDTO>, Object> selectGrantedRoles(@RequestParam("userId") String userId) {

        List<DpPortalRole> dpPortalRoles = dpPortalRoleService.selectGrantedRolesByUserId(userId);
        List<RoleDTO> roleDTOS = new ArrayList<>();
        dpPortalRoles.forEach(o -> {
            RoleDTO roleDTO = new RoleDTO();
            BeanUtils.copyProperties(o, roleDTO);
            roleDTOS.add(roleDTO);
        });

        return Result.success(roleDTOS);
    }

    @Override
    public Result<Boolean, Object> resetPassword(@RequestParam("userName") String userName, @RequestHeader(value = "authorization-userId", required = true) String optUserId) {
        DpPortalUser dpPortalUser = new DpPortalUser();
        dpPortalUser.setUserName(userName);
        dpPortalUser.setCreateUserId(optUserId);
        dpPortalUser.setUserPasswd(SM3Utils.encode("Prime@2020"));
        int i = dpPortalUserService.updateByUserNameSelective(dpPortalUser);
        return Result.success(i > 0);
    }

    @Override
    public Result<Boolean, Object> updatePassword(@RequestBody UpdatePasswdVo updatePasswdVo, @RequestHeader(value = "authorization-userId", required = true) String optUserId,
                                                  @RequestHeader(value = "authorization-userName", required = true) String optUserName) {
        DpPortalUser dpPortalUser1 = dpPortalUserService.selectByUserName(optUserName);
        if (dpPortalUser1.getUserPasswd().equals(SM3Utils.encode(updatePasswdVo.getOldPassword()))) {
            DpPortalUser dpPortalUser = new DpPortalUser();
            dpPortalUser.setUserName(optUserName);
            dpPortalUser.setUserPasswd(SM3Utils.encode(updatePasswdVo.getPassword()));
            dpPortalUser.setUserId(optUserId);
            int i = dpPortalUserService.updateByPrimaryKeySelective(dpPortalUser);
            return Result.success(i > 0);
        } else {
            return Result.success(false, "旧密码错误");
        }
    }

    /**
     * 根据用户名查询用户.
     *
     * @param userName userName
     * @return 用户信息
     */
    @Override
    public Result<UserDTO, Object> selectByUserName(String userName) {
        DpPortalUser dpPortalUser = dpPortalUserService.selectByUserName(userName);
        if (dpPortalUser == null) {
            return Result.success(null);
        }
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(dpPortalUser, userDTO);
        return Result.success(userDTO);
    }

    /**
     * 根据用户名查询用户.
     *
     * @param userName userName
     * @return 用户信息
     */
    @Override
    public Result<UserDTO, Object> selectLoginInfoByUserName(String userName) {
        DpPortalUser dpPortalUser = dpPortalUserService.selectLoginInfoByUserName(userName);
        if (dpPortalUser == null) {
            return Result.success(null);
        }
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(dpPortalUser, userDTO);
        return Result.success(userDTO);
    }

    /**
     * 锁定用户接口.
     *
     * @param userId userId
     * @return Result
     */
    @Override
    public Result<Boolean, Object> lockUser(String userId) {
        int i = dpPortalUserService.updateStatus(userId, 2);
        return Result.success(i > 0);
    }

    /**
     * 解锁用户.
     *
     * @param userId userId
     * @return Result
     */
    @Override
    public Result<Boolean, Object> unLockUser(String userId) {
        int i = dpPortalUserService.updateStatus(userId, 0);
        return Result.success(i > 0);
    }

    @Override
    public Result<Boolean, Object> updateLastLoginTime(String userId, String loginTime) {
        int i = 0;

        try {
            i = dpPortalUserService.updateLastLoginTime(userId, DateUtils.parseDate(loginTime, "yyyy-MM-dd HH:mm:ss"));
        } catch (ParseException e) {
            e.printStackTrace();
            return Result.fail(StatusCode.CODE_10010.getCode(), "", "parseDate  error");
        }
        return Result.success(i > 0);
    }

    @Override
    public Result<List<UserRoleDTO>, Object> selectUserRoleByUserName(String userName) {
        List<UserRoleEntity> userRoleEntities = dpPortalUserService.selectUserRoleByUserName(userName);
        List<UserRoleDTO> userRoleDTOS = new ArrayList<>();
        userRoleEntities.forEach(o -> {
            UserRoleDTO userRoleDTO = new UserRoleDTO();
            BeanUtils.copyProperties(o, userRoleDTO);
            userRoleDTOS.add(userRoleDTO);
        });
        return Result.success(userRoleDTOS);
    }

    @Override
    public Result<List<UserDTO>, Object> selectAuditingUser(String optUserId) {
        List<DpPortalUser> dpPortalUsers = dpPortalUserService.selectAuditingUser(optUserId);

        List<UserDTO> userDTOS = new ArrayList<>();
        dpPortalUsers.forEach(o -> {
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(o, userDTO);
            userDTOS.add(userDTO);
        });
        return Result.success(userDTOS);
    }

    @Override
    public Result<Boolean, Object> checkUserName(String userName) {
        Integer integer = dpPortalUserService.checkExistsByUserName(userName);
        if (integer != null && integer.equals(1)) {
            return Result.success(true);
        }
        return Result.success(false);
    }

}

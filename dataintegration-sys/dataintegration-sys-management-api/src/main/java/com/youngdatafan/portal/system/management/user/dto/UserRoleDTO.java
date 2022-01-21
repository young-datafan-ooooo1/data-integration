package com.youngdatafan.portal.system.management.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Author: jeremychen
 * @Descripition:
 * @Date:2020/3/17 11:08 上午
 */
@ApiModel(description = "用户角色返回对象")
public class UserRoleDTO {
    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("用户编号")
    private String userId;

    @ApiModelProperty("角色id")
    private String roleId;

    @ApiModelProperty("用户中文名")
    private String userNameCn;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getUserNameCn() {
        return userNameCn;
    }

    public void setUserNameCn(String userNameCn) {
        this.userNameCn = userNameCn;
    }
}

package com.youngdatafan.portal.system.management.user.entity;

/**
 * @Author: jeremychen
 * @Descripition:
 * @Date:2020/3/17 11:08 上午
 */
public class UserRoleEntity {
    private String userName;

    private String userId;

    private String roleId;

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

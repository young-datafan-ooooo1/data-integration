package com.youngdatafan.common.sso.entity;


import com.datafan.dataintegration.core.util.JsonUtils;

/**
 * @Author: jeremychen
 * @Descripition: 用户登录信息
 * @Date:2020/3/13 3:17 下午
 */
public class LoginInfo {
    private String userId;
    private String userName;
    private String loginIp;
    private String token;
    private String roles;

    public static LoginInfo toObject(String json) {
        try {
            return JsonUtils.parseObject(json, LoginInfo.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String toJSON() {
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setLoginIp(loginIp);
        loginInfo.setUserId(userId);
        loginInfo.setUserName(userName);
        loginInfo.setToken(token);
        loginInfo.setRoles(roles);
        return JsonUtils.toString(loginInfo);
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }
}

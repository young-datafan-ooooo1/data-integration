package com.youngdatafan.common.sso.entity;

import com.youngdatafan.dataintegration.core.util.JsonUtils;
import lombok.Data;

/**
 * 用户登录信息.
 */
@Data
public class LoginInfo {
    private String userId;

    private String userName;

    private String loginIp;

    private String token;

    private String roles;

    /**
     * toObject.
     *
     * @param json json
     * @return LoginInfo
     */
    public static LoginInfo toObject(String json) {
        try {
            return JsonUtils.parseObject(json, LoginInfo.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * toJSON.
     *
     * @return String
     */
    public String toJSON() {
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setLoginIp(loginIp);
        loginInfo.setUserId(userId);
        loginInfo.setUserName(userName);
        loginInfo.setToken(token);
        loginInfo.setRoles(roles);
        return JsonUtils.toString(loginInfo);
    }
}

package com.youngdatafan.portal.system.management.user.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author: jeremychen
 * @Descripition:
 * @Date:2020/2/10 5:31 下午
 */

@ApiModel(description = "登录用户对象")
public class LoginVo {
    @ApiModelProperty(value = "用户名", required = true)
    @NotBlank
    private String userName;


    @ApiModelProperty(value = "密码", required = true)
    @NotBlank
    private String password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

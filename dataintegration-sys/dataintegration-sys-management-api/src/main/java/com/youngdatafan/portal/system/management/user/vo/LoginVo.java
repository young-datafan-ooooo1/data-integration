package com.youngdatafan.portal.system.management.user.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 登录用户对象.
 */

@Data
@ApiModel(description = "登录用户对象")
public class LoginVo {

    @ApiModelProperty(value = "用户名", required = true)
    @NotBlank
    private String userName;

    @ApiModelProperty(value = "密码", required = true)
    @NotBlank
    private String password;
}

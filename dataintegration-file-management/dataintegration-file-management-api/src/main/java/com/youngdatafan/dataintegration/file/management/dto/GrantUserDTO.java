package com.youngdatafan.dataintegration.file.management.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 授权用户信息返回类.
 *
 * @author liuhao
 * @since 2022/1/19 16:22
 */

@Data
@ApiModel("授权用户信息返回类")
public class GrantUserDTO {

    /**
     * 用户id.
     */
    @ApiModelProperty("用户id")
    private Integer userId;

    /**
     * 用户账户信息.
     */
    @ApiModelProperty("用户账号信息")
    private String account;

    /**
     * 用户名称信息.
     */
    @ApiModelProperty("用户名称信息")
    private String userName;
}

package com.youngdatafan.portal.system.management.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户角色返回对象.
 */
@Data
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
}

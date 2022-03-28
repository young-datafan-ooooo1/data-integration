package com.youngdatafan.portal.system.management.user.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 用户返回对象.
 */
@Data
@ApiModel(description = "用户返回对象")
public class DpPortalUser {
    /**
     * 用户编号(PK).
     */
    @ApiModelProperty("用户编号")
    private String userId;

    /**
     * 用户名称(UK).
     */
    @ApiModelProperty("用户名称")
    private String userName;

    /**
     * 用户描述.
     */
    @ApiModelProperty("用户描述")
    private String describe;

    /**
     * 用户密码.
     */
    @ApiModelProperty("用户密码")
    private String userPasswd;

    /**
     * 用户手机号.
     */
    @ApiModelProperty("用户手机号")
    private String userMobile;

    /**
     * 用户邮箱地址.
     */
    @ApiModelProperty("用户邮箱地址")
    private String userEmail;

    /**
     * 最后登录时间.
     */
    @ApiModelProperty("最后登录时间")
    private Date lastLoginTime;

    /**
     * 创建时间.
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 修改时间.
     */
    @ApiModelProperty("修改时间")
    private Date updateTime;

    /**
     * 创建者.
     */
    @ApiModelProperty("创建者")
    private String createUserId;

    /**
     * 用户状态.
     */
    @ApiModelProperty("用户状态")
    private String status;

    @ApiModelProperty("锁定时间")
    private Date lockTime;

    @ApiModelProperty("角色id")
    private List<String> roleIds;

    @ApiModelProperty("租户id")
    private List<String> tenantIds;
}

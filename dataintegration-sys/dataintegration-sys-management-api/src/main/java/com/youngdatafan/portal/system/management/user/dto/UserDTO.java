package com.youngdatafan.portal.system.management.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 * 用户返回对象.
 *
 * @author gavin
 * @since 2020/2/4 5:04 下午
 */
@Data
@ApiModel(description = "用户返回对象")
public class UserDTO implements Serializable {

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
     * 创建者/.
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", userId=").append(userId);
        sb.append(", userName=").append(userName);
        sb.append(", describe=").append(describe);
        sb.append(", userPasswd=").append(userPasswd);
        sb.append(", userMobile=").append(userMobile);
        sb.append(", userEmail=").append(userEmail);
        sb.append(", lastLoginTime=").append(lastLoginTime);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", createUserId=").append(createUserId);
        sb.append(", status=").append(status);
        sb.append("]");
        return sb.toString();
    }
}

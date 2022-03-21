package com.youngdatafan.portal.system.management.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import lombok.Data;

/**
 * 角色返回对象.
 */
@Data
@ApiModel(description = "角色返回对象")
public class RoleDTO {

    /**
     * 角色编号(PK).
     */
    @ApiModelProperty("角色编号(PK)")
    private String roleId;

    /**
     * 角色名称(UK).
     */
    @ApiModelProperty("角色名称(UK)")
    private String roleName;

    /**
     * 用户描述.
     */
    @ApiModelProperty("用户描述")
    private String describe;

    /**
     * 角色状态.
     */
    @ApiModelProperty("角色状态")
    private String status;

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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", roleId=").append(roleId);
        sb.append(", roleName=").append(roleName);
        sb.append(", describe=").append(describe);
        sb.append(", status=").append(status);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", createUserId=").append(createUserId);
        sb.append("]");
        return sb.toString();
    }

}

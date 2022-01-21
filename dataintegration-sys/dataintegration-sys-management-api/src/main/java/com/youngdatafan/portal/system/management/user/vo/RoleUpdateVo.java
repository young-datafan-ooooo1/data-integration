package com.youngdatafan.portal.system.management.user.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

/**
 * @Author: jeremychen
 * @Descripition:
 * @Date:2020/2/11 3:53 下午
 */
@ApiModel(description = "角色对象")
public class RoleUpdateVo {

    /**
     * 角色编号(PK)
     */
    @ApiModelProperty("角色编号(PK)")
    @NotBlank
    private String roleId;
    /**
     * 角色名称(UK)
     */
    @ApiModelProperty(value = "角色名称(UK)", required = true)
    @NotBlank
    private String roleName;

    /**
     * 角色描述
     */
    @ApiModelProperty(value = "角色描述", required = true)
    private String describe;

    /**
     * 角色状态
     */
    @ApiModelProperty(value = "角色状态",required = true)
    @NotBlank
    private String status;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

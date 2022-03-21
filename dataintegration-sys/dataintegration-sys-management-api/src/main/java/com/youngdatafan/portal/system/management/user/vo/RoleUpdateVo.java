package com.youngdatafan.portal.system.management.user.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 角色对象.
 */
@Data
@ApiModel(description = "角色对象")
public class RoleUpdateVo {

    /**
     * 角色编号(PK).
     */
    @ApiModelProperty("角色编号(PK)")
    @NotBlank
    private String roleId;

    /**
     * 角色名称(UK).
     */
    @ApiModelProperty(value = "角色名称(UK)", required = true)
    @NotBlank
    private String roleName;

    /**
     * 角色描述.
     */
    @ApiModelProperty(value = "角色描述", required = true)
    private String describe;

    /**
     * 角色状态.
     */
    @ApiModelProperty(value = "角色状态", required = true)
    @NotBlank
    private String status;

}

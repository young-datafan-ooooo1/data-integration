package com.youngdatafan.dataintegration.file.management.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户权限查询信息接受类.
 */

@Data
@ApiModel("用户权限信息查询接受类")
public class UserGrantQueryVO {

    /**
     * 用户名称.
     */
    @ApiModelProperty("用户名称")
    private String userName;

    /**
     * 用户所属部门.
     */
    @ApiModelProperty("用户所属部门")
    private Integer deptId;
}

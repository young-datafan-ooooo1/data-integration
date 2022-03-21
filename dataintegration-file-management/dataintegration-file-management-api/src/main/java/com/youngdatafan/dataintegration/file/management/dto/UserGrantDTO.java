package com.youngdatafan.dataintegration.file.management.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户权限信息返回类.
 *
 * @author liuhao
 * @since 2022/1/19 16:24
 */

@Data
@ApiModel("用户权限信息返回类")
public class UserGrantDTO {

    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty("用户名称")
    private String userName;

    @ApiModelProperty("所属部门")
    private String deptName;

    @ApiModelProperty("账号")
    private String account;

    @ApiModelProperty("被授权文件数量")
    private Integer fileNum;

    @ApiModelProperty("被授权的文件夹数量")
    private Integer folderNum;
}

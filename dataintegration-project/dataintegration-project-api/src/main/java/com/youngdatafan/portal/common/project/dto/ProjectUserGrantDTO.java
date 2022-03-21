package com.youngdatafan.portal.common.project.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目用户授权.
 *
 * @author gavin
 * @since 2020/2/12 1:08 下午
 */
@Data
@NoArgsConstructor
@ApiModel(description = "项目用户授权")
public class ProjectUserGrantDTO {

    @ApiModelProperty(value = "项目编号")
    private String projectId;

    @ApiModelProperty(value = "用户编号")
    private String userId;

    @ApiModelProperty(value = "操作模式")
    private String opModel;

    @ApiModelProperty(value = "用户名")
    private String describe;

}

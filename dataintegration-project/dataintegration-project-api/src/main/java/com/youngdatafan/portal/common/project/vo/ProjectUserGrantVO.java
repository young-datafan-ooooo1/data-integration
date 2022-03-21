package com.youngdatafan.portal.common.project.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 用户授权.
 *
 * @author gavin
 * @since 2020/2/12 1:02 下午
 */
@Data
@NoArgsConstructor
@ApiModel(description = "用户授权对象")
public class ProjectUserGrantVO {

    @NotNull
    @ApiModelProperty(value = "用户编号", required = true)
    private String userId;

    @NotNull
    @ApiModelProperty(value = "操作模式（E、R、W、RW）", required = true)
    private String opModel;

}

package com.youngdatafan.di.run.management.server.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Author: jeremychen
 * @Descripition:
 * @Date:2020/3/31 11:19 上午
 */
@Data
@ApiModel("获取字段统计信息请求参数")
public class ExecuteFieldRangeSumVO {

    @ApiModelProperty(value = "转换json", required = true)
    @NotBlank
    private String transJson;

    @ApiModelProperty(value = "当前步骤名称", required = true)
    @NotBlank
    private String currentStepName;

    @ApiModelProperty(value = "字段名称", required = true)
    @NotBlank
    private String field;
    @ApiModelProperty(value = "组数", required = true)
    @NotBlank
    private int groupNum;

    @ApiModelProperty(value = "最大值", required = true)
    @NotBlank
    private Object max;

    @ApiModelProperty(value = "最小值", required = true)
    @NotBlank
    private Object min;
}

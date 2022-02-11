package com.youngdatafan.di.run.management.server.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: jeremychen
 * @Descripition:
 * @Date:2020/3/31 5:05 下午
 */
@Data
@ApiModel("字段统计信息")
public class FieldSumDTO {

    @ApiModelProperty("字段值")
    private Object FieldValue;

    @ApiModelProperty("统计个数")
    private int cnt;
}

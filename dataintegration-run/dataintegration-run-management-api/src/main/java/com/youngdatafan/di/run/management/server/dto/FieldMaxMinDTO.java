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
public class FieldMaxMinDTO {

    @ApiModelProperty("最大值")
    private Object max;

    @ApiModelProperty("最小值")
    private Object min;

}

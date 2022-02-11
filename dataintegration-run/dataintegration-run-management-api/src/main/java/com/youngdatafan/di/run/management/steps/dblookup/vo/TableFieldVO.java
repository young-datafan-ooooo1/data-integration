package com.youngdatafan.di.run.management.steps.dblookup.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: jeremychen
 * @Descripition:
 * @Date:2020/3/7 1:24 下午
 */
@Data
@ApiModel(description = "表字段 字段")
public class TableFieldVO {

    @ApiModelProperty(value = "字段名称")
    private String name;

    @ApiModelProperty(value = "类型")
    private String type;


}

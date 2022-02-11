package com.youngdatafan.di.run.management.server.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author gavin
 * @create 2020/2/21 12:10 下午
 */
@Data
@NoArgsConstructor
@ApiModel(description = "步骤字段对象")
public class SaveToModelFieldVO {

    @ApiModelProperty(value = "字段名", required = true)
    @NotNull
    String filedName;

    @ApiModelProperty(value = "字段中文名", required = true)
    @NotNull
    String filedCname;

    @ApiModelProperty(value = "字段备注")
    String fieldDesc;

}

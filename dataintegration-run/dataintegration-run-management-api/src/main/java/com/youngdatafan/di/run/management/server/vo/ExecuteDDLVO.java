package com.youngdatafan.di.run.management.server.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Author: jeremychen
 * @Descripition:
 * @Date:2020/3/9 2:00 下午
 */
@Data
@ApiModel(description = "执行建表语句实体类")
public class ExecuteDDLVO {


    @ApiModelProperty(value = "数据源id", required = true)
    @NotBlank
    private String dataSourceId;

    @ApiModelProperty(value = "ddl", required = true)
    @NotBlank
    private String ddl;
}

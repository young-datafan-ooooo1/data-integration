package com.youngdatafan.di.run.management.server.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: jeremychen
 * @Descripition:
 * @Date:2020/5/19 4:42 下午
 */

@Data
@ApiModel("前置步骤sql信息")
public class PreStepSqlInfoDTO {
    @ApiModelProperty("前置步骤sql")
    private String preStepSql;

    @ApiModelProperty("数据源")
    private String dataSourceId;
}

package com.youngdatafan.di.run.management.server.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: jeremychen
 * @Descripition:
 * @Date:2020/3/5 11:08 上午
 */
@Data
@ApiModel("公式信息")
public class FunctionDTO {
    @ApiModelProperty("公式名称")
    private String name;

    @ApiModelProperty("click house数据库公式名称")
    private String ckName;

    @ApiModelProperty("公式描述")
    private String describtion;

    @ApiModelProperty("公式说明html块")
    private String htmlString;
}

package com.youngdatafan.portal.system.management.summary.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: jeremychen
 * @Descripition:
 * @Date:2020/5/28 2:53 下午
 */
@ApiModel("最近六个")
@Data
public class LastSixItemDTO {

    @ApiModelProperty("id")
    private String itemId;

    @ApiModelProperty("名称")
    private String itemName;
}

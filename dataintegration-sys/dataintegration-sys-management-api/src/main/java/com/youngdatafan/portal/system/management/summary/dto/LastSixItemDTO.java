package com.youngdatafan.portal.system.management.summary.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 最近六个.
 */
@ApiModel("最近六个")
@Data
public class LastSixItemDTO {

    @ApiModelProperty("id")
    private String itemId;

    @ApiModelProperty("名称")
    private String itemName;
}

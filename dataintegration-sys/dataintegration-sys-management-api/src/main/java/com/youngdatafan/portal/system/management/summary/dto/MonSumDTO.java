package com.youngdatafan.portal.system.management.summary.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * 月均总数统计.
 */
@ApiModel("月均总数统计")
@Data
public class MonSumDTO {

    @ApiModelProperty("总数")
    private int total;

    @ApiModelProperty("月均")
    private int avgMon;

    @ApiModelProperty("月明细")
    private Map<String, Integer> details;
}

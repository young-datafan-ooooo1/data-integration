package com.youngdatafan.portal.system.management.summary.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 文件汇总.
 */
@Data
@ApiModel("文件汇总")
public class FileSummaryDTO {

    @ApiModelProperty("总数")
    private int total;

    @ApiModelProperty("月均")
    private int avg;

    @ApiModelProperty("明细")
    private List<FileSumDetailDTO> fileSumDetailDTOS;

}

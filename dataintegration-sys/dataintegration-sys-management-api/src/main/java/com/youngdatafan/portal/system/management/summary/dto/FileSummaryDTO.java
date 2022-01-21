package com.youngdatafan.portal.system.management.summary.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: jeremychen
 * @Descripition:
 * @Date:2020/4/18 5:26 下午
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

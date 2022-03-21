package com.youngdatafan.portal.system.management.summary.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 文件类型统计明细.
 */
@Data
@ApiModel("文件类型统计明细")
public class FileSumDetailDTO {

    @ApiModelProperty("文件类型")
    private String fileType;

    @ApiModelProperty("计数")
    private int cnt;
}

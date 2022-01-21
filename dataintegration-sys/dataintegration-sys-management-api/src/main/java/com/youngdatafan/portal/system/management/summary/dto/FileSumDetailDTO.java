package com.youngdatafan.portal.system.management.summary.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: jeremychen
 * @Descripition:
 * @Date:2020/5/28 10:33 上午
 */
@Data
@ApiModel("文件类型统计明细")
public class FileSumDetailDTO {
    @ApiModelProperty("文件类型")
    private String fileType;

    @ApiModelProperty("计数")
    private int cnt;
}

package com.youngdatafan.dataintegration.file.management.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 字段信息类.
 *
 * @Author jeremychen
 * @Date 2020/4/15 15:29
 */
@Data
@ApiModel("字段信息")
public class FieldInfoDTO {

    @ApiModelProperty("字段名称")
    private String fieldName;

    @ApiModelProperty("字段类型")
    private String fileType;
}

package com.youngdatafan.di.run.management.steps.connect.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class PreviewDataInfoDTO {
    @ApiModelProperty(value = "字段信息")
    private List<String> cloumns;

    @ApiModelProperty(value = "表字段数据")
    private List<Object[]> dataDTO;

}

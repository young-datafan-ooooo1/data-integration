package com.youngdatafan.di.run.management.steps.connect.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

@ApiModel(description = "预览数据信息")
@Data
public class PreviewDataDTO {

    private List<String> fieldValue;

}

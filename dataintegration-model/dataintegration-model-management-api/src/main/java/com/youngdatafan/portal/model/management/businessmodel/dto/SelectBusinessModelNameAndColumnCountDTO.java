package com.youngdatafan.portal.model.management.businessmodel.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Data;

/**
 * 查询模型名称和字段数对象.
 */
@Data
@ApiModel(description = "查询模型名称和字段数对象")
public class SelectBusinessModelNameAndColumnCountDTO {

    @ApiModelProperty(value = "模型名称")
    private String modelName;

    @ApiModelProperty(value = "字段数")
    private List<BusinessModelMetaDataDTO> businessModelMetaDataDTOS;

}

package com.youngdatafan.portal.model.management.businessmodel.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * <p>****************************************************************************</p>
 * <ul style="margin:15px;">
 * <li>Description : description</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2020/2/18 10:18 PM</li>
 * <li>Author      : ksice_xt</li>
 * </ul>
 * <p>****************************************************************************</p>
 */
@ApiModel(description = "查询模型名称和字段数对象")
public class SelectBusinessModelNameAndColumnCountDTO {

    @ApiModelProperty(value = "模型名称")
    private String modelName;

    @ApiModelProperty(value = "字段数")
    private List<BusinessModelMetaDataDTO> businessModelMetaDataDTOS;


    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public List<BusinessModelMetaDataDTO> getBusinessModelMetaDataDTOS() {
        return businessModelMetaDataDTOS;
    }

    public void setBusinessModelMetaDataDTOS(List<BusinessModelMetaDataDTO> businessModelMetaDataDTOS) {
        this.businessModelMetaDataDTOS = businessModelMetaDataDTOS;
    }

}

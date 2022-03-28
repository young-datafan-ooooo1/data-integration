package com.youngdatafan.portal.model.management.modelgrant.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import java.util.Set;
import lombok.Data;

/**
 * 模型名称分组名称和类型对象.
 */
@Data
@ApiModel(description = "模型名称分组名称和类型对象")
public class ModelNameAndGroupNameAndTypes {

    @ApiModelProperty(value = "业务对象集合")
    private List<BusinessModelListDTO> businessModelListDTOS;

    @ApiModelProperty(value = "模型名称集合")
    private List<String> modelNames;

    @ApiModelProperty(value = "模型分组名称集合")
    private Set<String> groupNames;

    @ApiModelProperty(value = "模型分组类型集合")
    private Set<String> groupTypes;

    private Long total;

}

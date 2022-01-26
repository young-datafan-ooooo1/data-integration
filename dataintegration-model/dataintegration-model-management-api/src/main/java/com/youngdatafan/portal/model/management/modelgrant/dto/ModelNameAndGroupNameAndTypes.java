package com.youngdatafan.portal.model.management.modelgrant.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Set;

/**
 * <p>****************************************************************************</p>
 * <ul style="margin:15px;">
 * <li>Description : description</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2020/2/16 8:49 PM</li>
 * <li>Author      : ksice_xt</li>
 * </ul>
 * <p>****************************************************************************</p>
 */
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

    public List<BusinessModelListDTO> getBusinessModelListDTOS() {
        return businessModelListDTOS;
    }

    public void setBusinessModelListDTOS(List<BusinessModelListDTO> businessModelListDTOS) {
        this.businessModelListDTOS = businessModelListDTOS;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<String> getModelNames() {
        return modelNames;
    }

    public void setModelNames(List<String> modelNames) {
        this.modelNames = modelNames;
    }

    public Set<String> getGroupNames() {
        return groupNames;
    }

    public void setGroupNames(Set<String> groupNames) {
        this.groupNames = groupNames;
    }

    public Set<String> getGroupTypes() {
        return groupTypes;
    }

    public void setGroupTypes(Set<String> groupTypes) {
        this.groupTypes = groupTypes;
    }
}

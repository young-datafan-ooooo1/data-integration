package com.youngdatafan.portal.model.management.modelgrant.dto;

import com.youngdatafan.portal.model.management.common.enums.GroupTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>****************************************************************************</p>
 * <ul style="margin:15px;">
 * <li>Description : description</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2020/2/15 3:33 PM</li>
 * <li>Author      : ksice_xt</li>
 * </ul>
 * <p>****************************************************************************</p>
 */
@ApiModel(description = "添加模型返回对象")
public class BusinessModelListDTO {


    @ApiModelProperty(value = "模型id")
    private String modelName;

    @ApiModelProperty(value = "模型中文名称")
    private String modelChineseName;

    @ApiModelProperty(value = "模型分组名称")
    private String modelGroupName;

    @ApiModelProperty(value = "模型分组类型")
    private String modelGroupType;

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getModelGroupName() {
        return modelGroupName;
    }

    public void setModelGroupName(String modelGroupName) {


        this.modelGroupName = modelGroupName;
    }

    public String getModelGroupType() {

        return modelGroupType;
    }

    public void setModelGroupType(String modelGroupType) {

        if (GroupTypeEnum.YWMX.code().equals(modelGroupType)) {
            modelGroupType = GroupTypeEnum.YWMX.getDesc();
        }
        if (GroupTypeEnum.ZDYMX.code().equals(modelGroupType)) {
            modelGroupType = GroupTypeEnum.ZDYMX.getDesc();
        }
        this.modelGroupType = modelGroupType;
    }

    public String getModelChineseName() {
        return modelChineseName;
    }

    public void setModelChineseName(String modelChineseName) {
        this.modelChineseName = modelChineseName;
    }

    @Override
    public String toString() {
        return "BusinessModelListDTO{" +
                "modelName='" + modelName + '\'' +
                ", modelChineseName='" + modelChineseName + '\'' +
                ", modelGroupName='" + modelGroupName + '\'' +
                ", modelGroupType='" + modelGroupType + '\'' +
                '}';
    }
}

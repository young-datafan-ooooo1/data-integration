package com.youngdatafan.portal.model.management.outinterfacemodel.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>****************************************************************************</p>
 * <ul style="margin:15px;">
 * <li>Description : description</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2020/2/25 10:39 AM</li>
 * <li>Author      : ksice_xt</li>
 * </ul>
 * <p>****************************************************************************</p>
 */
@ApiModel("批量下载模板对象")
public class BatchDownloadOutinterfaceModelDTO {

    @ApiModelProperty(value = "基础模型名称")
    private String basicModelName;

    @ApiModelProperty(value = "基础模型分组")
    private String basicModelGroup;

    @ApiModelProperty(value = "基础模型字段")
    private String basicModelColumnName;

    @ApiModelProperty(value = "基础模型显示名称")
    private String basicModelChineseColumnName;

    @ApiModelProperty(value = "业务模型名称")
    private String outinterfaceModelName;

    @ApiModelProperty(value = "模型排序")
    private String modelSort;

    @ApiModelProperty(value = "业务模型字段显示")
    private String outinterfaceModelColumnName;

    @ApiModelProperty(value = "业务模型字段排序")
    private String modelColumnSort;

    @ApiModelProperty(value = "业务模型是否有效")
    private String outinterfaceModelEnable;

    @ApiModelProperty(value = "业务模型备注")
    private String description;

    @ApiModelProperty(value = "业务模型分组")
    private String outinterfaceModelGroupName;


    public String getBasicModelName() {
        return basicModelName;
    }

    public void setBasicModelName(String basicModelName) {
        this.basicModelName = basicModelName;
    }

    public String getBasicModelGroup() {
        return basicModelGroup;
    }

    public void setBasicModelGroup(String basicModelGroup) {
        this.basicModelGroup = basicModelGroup;
    }

    public String getBasicModelColumnName() {
        return basicModelColumnName;
    }

    public void setBasicModelColumnName(String basicModelColumnName) {
        this.basicModelColumnName = basicModelColumnName;
    }

    public String getBasicModelChineseColumnName() {
        return basicModelChineseColumnName;
    }

    public void setBasicModelChineseColumnName(String basicModelChineseColumnName) {
        this.basicModelChineseColumnName = basicModelChineseColumnName;
    }


    public String getModelSort() {
        return modelSort;
    }

    public void setModelSort(String modelSort) {
        this.modelSort = modelSort;
    }

    public String getModelColumnSort() {
        return modelColumnSort;
    }

    public void setModelColumnSort(String modelColumnSort) {
        this.modelColumnSort = modelColumnSort;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOutinterfaceModelName() {
        return outinterfaceModelName;
    }

    public void setOutinterfaceModelName(String outinterfaceModelName) {
        this.outinterfaceModelName = outinterfaceModelName;
    }

    public String getOutinterfaceModelColumnName() {
        return outinterfaceModelColumnName;
    }

    public void setOutinterfaceModelColumnName(String outinterfaceModelColumnName) {
        this.outinterfaceModelColumnName = outinterfaceModelColumnName;
    }

    public String getOutinterfaceModelEnable() {
        return outinterfaceModelEnable;
    }

    public void setOutinterfaceModelEnable(String outinterfaceModelEnable) {
        this.outinterfaceModelEnable = outinterfaceModelEnable;
    }

    public String getOutinterfaceModelGroupName() {
        return outinterfaceModelGroupName;
    }

    public void setOutinterfaceModelGroupName(String outinterfaceModelGroupName) {
        this.outinterfaceModelGroupName = outinterfaceModelGroupName;
    }

    @Override
    public String toString() {
        return "BatchDownloadOutinterfaceModelDTO{" +
                "basicModelName='" + basicModelName + '\'' +
                ", basicModelGroup='" + basicModelGroup + '\'' +
                ", basicModelColumnName='" + basicModelColumnName + '\'' +
                ", basicModelChineseColumnName='" + basicModelChineseColumnName + '\'' +
                ", outinterfaceModelName='" + outinterfaceModelName + '\'' +
                ", modelSort='" + modelSort + '\'' +
                ", outinterfaceModelColumnName='" + outinterfaceModelColumnName + '\'' +
                ", modelColumnSort='" + modelColumnSort + '\'' +
                ", outinterfaceModelEnable='" + outinterfaceModelEnable + '\'' +
                ", description='" + description + '\'' +
                ", outinterfaceModelGroupName='" + outinterfaceModelGroupName + '\'' +
                '}';
    }
}

package com.youngdatafan.portal.model.management.businessmodel.dto;

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
public class BatchDownloadBusinessModelDTO {

    @ApiModelProperty(value = "基础模型名称")
    private String basicModelName;

    @ApiModelProperty(value = "基础模型分组")
    private String basicModelGroup;

    @ApiModelProperty(value = "基础模型字段")
    private String basicModelColumnName;

    @ApiModelProperty(value = "基础模型显示名称")
    private String basicModelChineseColumnName;

    @ApiModelProperty(value = "业务模型名称")
    private String businessModelName;

    @ApiModelProperty(value = "模型排序")
    private String modelSort;

    @ApiModelProperty(value = "业务模型字段显示")
    private String businessModelColumnName;

    @ApiModelProperty(value = "业务模型字段排序")
    private String modelColumnSort;

    @ApiModelProperty(value = "业务模型是否有效")
    private String businessModelEnable;

    @ApiModelProperty(value = "业务模型备注")
    private String description;

    @ApiModelProperty(value = "业务模型分组")
    private String businessModelGroupName;

    public String getBusinessModelGroupName() {
        return businessModelGroupName;
    }

    public void setBusinessModelGroupName(String businessModelGroupName) {
        this.businessModelGroupName = businessModelGroupName;
    }

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

    public String getBusinessModelName() {
        return businessModelName;
    }

    public void setBusinessModelName(String businessModelName) {
        this.businessModelName = businessModelName;
    }

    public String getModelSort() {
        return modelSort;
    }

    public void setModelSort(String modelSort) {
        this.modelSort = modelSort;
    }

    public String getBusinessModelColumnName() {
        return businessModelColumnName;
    }

    public void setBusinessModelColumnName(String businessModelColumnName) {
        this.businessModelColumnName = businessModelColumnName;
    }

    public String getModelColumnSort() {
        return modelColumnSort;
    }

    public void setModelColumnSort(String modelColumnSort) {
        this.modelColumnSort = modelColumnSort;
    }

    public String getBusinessModelEnable() {
        return businessModelEnable;
    }

    public void setBusinessModelEnable(String businessModelEnable) {
        this.businessModelEnable = businessModelEnable;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "BatchDownloadBusinessModelDTO{" +
                "basicModelName='" + basicModelName + '\'' +
                ", basicModelGroup='" + basicModelGroup + '\'' +
                ", basicModelColumnName='" + basicModelColumnName + '\'' +
                ", basicModelChineseColumnName='" + basicModelChineseColumnName + '\'' +
                ", businessModelName='" + businessModelName + '\'' +
                ", modelSort='" + modelSort + '\'' +
                ", businessModelColumnName='" + businessModelColumnName + '\'' +
                ", modelColumnSort='" + modelColumnSort + '\'' +
                ", businessModelEnable='" + businessModelEnable + '\'' +
                ", description='" + description + '\'' +
                ", businessModelGroupName='" + businessModelGroupName + '\'' +
                '}';
    }
}

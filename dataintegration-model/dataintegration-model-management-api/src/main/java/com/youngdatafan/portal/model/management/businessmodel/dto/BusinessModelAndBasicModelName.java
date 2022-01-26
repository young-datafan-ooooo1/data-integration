package com.youngdatafan.portal.model.management.businessmodel.dto;

import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * <p>****************************************************************************</p>
 * <ul style="margin:15px;">
 * <li>Description : description</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2020/2/25 1:26 PM</li>
 * <li>Author      : ksice_xt</li>
 * </ul>
 * <p>****************************************************************************</p>
 */
public class BusinessModelAndBasicModelName {


    @ApiModelProperty(value = "基础模型名称")
    private String basicModelName;

    @ApiModelProperty(value = "基础模型分组")
    private String basicModelGroup;


    @ApiModelProperty(value = "业务模型名称")
    private String businessModelName;

    @ApiModelProperty(value = "模型排序")
    private String modelSort;

    @ApiModelProperty(value = "业务模型是否有效")
    private String businessModelEnable;

    @ApiModelProperty(value = "业务模型备注")
    private String description;

    @ApiModelProperty(value = "业务模型分组")
    private String businessModelGroupName;

    @ApiModelProperty(value = "业务模型id")
    private String businessModelId;

    private Date createTime;

    private Date updateTime;

    private String createUserId;

    private String querySql;

    public String getQuerySql() {
        return querySql;
    }

    public void setQuerySql(String querySql) {
        this.querySql = querySql;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getBusinessModelId() {
        return businessModelId;
    }

    public void setBusinessModelId(String businessModelId) {
        this.businessModelId = businessModelId;
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

    public String getBusinessModelGroupName() {
        return businessModelGroupName;
    }

    public void setBusinessModelGroupName(String businessModelGroupName) {
        this.businessModelGroupName = businessModelGroupName;
    }

    @Override
    public String toString() {
        return "BusinessModelAndBasicModelName{" +
                "basicModelName='" + basicModelName + '\'' +
                ", basicModelGroup='" + basicModelGroup + '\'' +
                ", businessModelName='" + businessModelName + '\'' +
                ", modelSort='" + modelSort + '\'' +
                ", businessModelEnable='" + businessModelEnable + '\'' +
                ", description='" + description + '\'' +
                ", businessModelGroupName='" + businessModelGroupName + '\'' +
                ", businessModelId='" + businessModelId + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", createUserId='" + createUserId + '\'' +
                '}';
    }
}

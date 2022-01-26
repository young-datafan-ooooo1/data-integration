package com.youngdatafan.portal.model.management.outmodel.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * <p>****************************************************************************</p>
 * <ul style="margin:15px;">
 * <li>Description : description</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2020/2/18 3:35 PM</li>
 * <li>Author      : ksice_xt</li>
 * </ul>
 * <p>****************************************************************************</p>
 */
@ApiModel(description = "输出类模型返回对象")
public class OutModelDTO {

    @ApiModelProperty("模型id")
    private String modelId;

    @ApiModelProperty("模型id")
    private String modelName;

    @ApiModelProperty("项目名称")
    private String projectName;
    @ApiModelProperty("项目类型")

    private String projectType;
    @ApiModelProperty("")

    private Integer columnSize;
    @ApiModelProperty("模型id")

    private Integer modelOrder;
    @ApiModelProperty("模型id")
    private String description;

    private String updateFrequency;

    private String dataUpdateStrategy;

    private String dataSaveStrategy;
    @ApiModelProperty("模型id")
    private Date createTime;
    @ApiModelProperty("模型id")
    private String createUserId;

    @ApiModelProperty("模型id")
    private String dataCount;

    private String datasourceId;

    private String tableName;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    private static final long serialVersionUID = 1L;

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId == null ? null : modelId.trim();
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName == null ? null : modelName.trim();
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName == null ? null : projectName.trim();
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType == null ? null : projectType.trim();
    }

    public Integer getColumnSize() {
        return columnSize;
    }

    public void setColumnSize(Integer columnSize) {
        this.columnSize = columnSize;
    }

    public Integer getModelOrder() {
        return modelOrder;
    }

    public void setModelOrder(Integer modelOrder) {
        this.modelOrder = modelOrder;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getUpdateFrequency() {
        return updateFrequency;
    }

    public void setUpdateFrequency(String updateFrequency) {
        this.updateFrequency = updateFrequency == null ? null : updateFrequency.trim();
    }

    public String getDataUpdateStrategy() {
        return dataUpdateStrategy;
    }

    public void setDataUpdateStrategy(String dataUpdateStrategy) {
        this.dataUpdateStrategy = dataUpdateStrategy == null ? null : dataUpdateStrategy.trim();
    }

    public String getDataSaveStrategy() {
        return dataSaveStrategy;
    }

    public void setDataSaveStrategy(String dataSaveStrategy) {
        this.dataSaveStrategy = dataSaveStrategy == null ? null : dataSaveStrategy.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId == null ? null : createUserId.trim();
    }

    public String getDataCount() {
        return dataCount;
    }

    public void setDataCount(String dataCount) {
        this.dataCount = dataCount == null ? null : dataCount.trim();
    }

    public String getDatasourceId() {
        return datasourceId;
    }

    public void setDatasourceId(String datasourceId) {
        this.datasourceId = datasourceId == null ? null : datasourceId.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", modelId=").append(modelId);
        sb.append(", modelName=").append(modelName);
        sb.append(", projectName=").append(projectName);
        sb.append(", projectType=").append(projectType);
        sb.append(", columnSize=").append(columnSize);
        sb.append(", modelOrder=").append(modelOrder);
        sb.append(", description=").append(description);
        sb.append(", updateFrequency=").append(updateFrequency);
        sb.append(", dataUpdateStrategy=").append(dataUpdateStrategy);
        sb.append(", dataSaveStrategy=").append(dataSaveStrategy);
        sb.append(", createTime=").append(createTime);
        sb.append(", createUserId=").append(createUserId);
        sb.append(", dataCount=").append(dataCount);
        sb.append(", datasourceId=").append(datasourceId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}

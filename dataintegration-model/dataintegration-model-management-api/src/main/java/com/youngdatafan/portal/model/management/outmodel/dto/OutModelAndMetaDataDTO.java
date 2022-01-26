package com.youngdatafan.portal.model.management.outmodel.dto;

import java.util.Date;
import java.util.List;

/**
 * <p>****************************************************************************</p>
 * <ul style="margin:15px;">
 * <li>Description : description</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2020/2/28 11:56 AM</li>
 * <li>Author      : ksice_xt</li>
 * </ul>
 * <p>****************************************************************************</p>
 */
public class OutModelAndMetaDataDTO {

    private String modelId;

    private String groupName;

    private String modelName;

    private String projectName;

    private String projectType;

    private Integer columnSize = 0;

    private Integer modelOrder;

    private String description;

    private String updateFrequency;

    private String dataUpdateStrategy;

    private String dataSaveStrategy;

    private Date createTime;

    private Date updateTime;

    private String createUserId;

    private String dataCount;

    private String datasourceId;

    private String tableName;

    private String stepName;

    private String querySql;

    private String projectId;

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getQuerySql() {
        return querySql;
    }

    public void setQuerySql(String querySql) {
        this.querySql = querySql;
    }

    private List<OutModelMetaDataDTO> outModelMetaDataDTOS;

    public List<OutModelMetaDataDTO> getOutModelMetaDataDTOS() {
        return outModelMetaDataDTOS;
    }

    public void setOutModelMetaDataDTOS(List<OutModelMetaDataDTO> outModelMetaDataDTOS) {
        this.outModelMetaDataDTOS = outModelMetaDataDTOS;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
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

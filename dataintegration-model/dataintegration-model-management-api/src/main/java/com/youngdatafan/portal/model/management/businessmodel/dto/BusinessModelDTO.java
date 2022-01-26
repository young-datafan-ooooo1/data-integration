package com.youngdatafan.portal.model.management.businessmodel.dto;

import com.youngdatafan.portal.model.management.common.entity.ModelFilterVO;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class BusinessModelDTO implements Serializable {
    private String modelName;

    private String chineseName;

    private String basicModelName;

    private String description;

    private String filterRule;

    private String enabled;

    private Date createTime;

    private Date updateTime;

    private String createUserId;

    private String querySql;

    private String sortNum;

    private String columnCount = "0";

    private String createUserName;

    private String groupName;

    private String cName;

    private String basicmodelGroupName;

    private String basicmodelGroupId;

    private String modelType;

    public String getModelType() {
        return modelType;
    }

    public void setModelType(String modelType) {
        this.modelType = modelType;
    }

    private List<BusinessModelMetaDataDTO> businessModelMetaDataDTOS;

    private List<ModelFilterVO> modelFilterVOS;

    public List<ModelFilterVO> getModelFilterVOS() {
        return modelFilterVOS;
    }

    public void setModelFilterVOS(List<ModelFilterVO> modelFilterVOS) {
        this.modelFilterVOS = modelFilterVOS;
    }

    public String getBasicmodelGroupId() {
        return basicmodelGroupId;
    }

    public void setBasicmodelGroupId(String basicmodelGroupId) {
        this.basicmodelGroupId = basicmodelGroupId;
    }

    public String getBasicmodelGroupName() {
        return basicmodelGroupName;
    }

    public void setBasicmodelGroupName(String basicmodelGroupName) {
        this.basicmodelGroupName = basicmodelGroupName;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }


    public List<BusinessModelMetaDataDTO> getBusinessModelMetaDataDTOS() {
        return businessModelMetaDataDTOS;
    }

    public void setBusinessModelMetaDataDTOS(List<BusinessModelMetaDataDTO> businessModelMetaDataDTOS) {
        this.businessModelMetaDataDTOS = businessModelMetaDataDTOS;
    }

    public String getColumnCount() {
        return columnCount;
    }

    public void setColumnCount(String columnCount) {
        this.columnCount = columnCount;
    }

    public String getSortNum() {
        return sortNum;
    }

    public void setSortNum(String sortNum) {
        this.sortNum = sortNum;
    }

    private static final long serialVersionUID = 1L;

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName == null ? null : modelName.trim();
    }

    public String getChineseName() {
        return chineseName;
    }

    public void setChineseName(String chineseName) {
        this.chineseName = chineseName == null ? null : chineseName.trim();
    }

    public String getBasicModelName() {
        return basicModelName;
    }

    public void setBasicModelName(String basicModelName) {
        this.basicModelName = basicModelName == null ? null : basicModelName.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getFilterRule() {
        return filterRule;
    }

    public void setFilterRule(String filterRule) {
        this.filterRule = filterRule == null ? null : filterRule.trim();
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled == null ? null : enabled.trim();
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
        this.createUserId = createUserId == null ? null : createUserId.trim();
    }

    public String getQuerySql() {
        return querySql;
    }

    public void setQuerySql(String querySql) {
        this.querySql = querySql == null ? null : querySql.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", modelName=").append(modelName);
        sb.append(", chineseName=").append(chineseName);
        sb.append(", basicModelName=").append(basicModelName);
        sb.append(", description=").append(description);
        sb.append(", filterRule=").append(filterRule);
        sb.append(", enabled=").append(enabled);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", createUserId=").append(createUserId);
        sb.append(", querySql=").append(querySql);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}

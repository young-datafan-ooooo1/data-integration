package com.youngdatafan.di.run.management.server.vo;

import com.youngdatafan.di.run.management.util.SqlEncoderUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author gavin
 * @since 2020/3/6 11:45 上午
 */
@ApiModel(description = "保存为模型")
public class SaveToModelVO {

    @ApiModelProperty(value = "数据源id", required = true)
    @NotBlank
    private String datasourceId;

    @ApiModelProperty(value = "数据查询sql", required = true)
    @NotBlank
    private String dataQuerySql;

    @ApiModelProperty(value = "模型名称", required = true)
    @NotBlank
    private String modelName;

    @ApiModelProperty(value = "所属分组id", required = true)
    @NotBlank
    private String groupId;

    @ApiModelProperty(value = "模型描述")
    private String modelDesc;

    @ApiModelProperty(value = "项目ID", required = true)
    @NotBlank
    private String projectId;

    @ApiModelProperty(value = "项目名称", required = true)
    @NotBlank
    private String projectName;

    @ApiModelProperty(value = "项目类型", required = true)
    @NotBlank
    private String projectType;

    @ApiModelProperty(value = "步骤名称", required = true)
    @NotBlank
    private String stepName;

    @ApiModelProperty(value = "字段集合", required = true)
    @NotNull
    private List<SaveToModelFieldVO> fields;

    public String getDatasourceId() {
        return datasourceId;
    }

    public void setDatasourceId(String datasourceId) {
        this.datasourceId = datasourceId;
    }

    public String getDataQuerySql() {
        return dataQuerySql;
    }

    public void setDataQuerySql(String dataQuerySql) {
        this.dataQuerySql = SqlEncoderUtil.decode(dataQuerySql);
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getModelDesc() {
        return modelDesc;
    }

    public void setModelDesc(String modelDesc) {
        this.modelDesc = modelDesc;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public List<SaveToModelFieldVO> getFields() {
        return fields;
    }

    public void setFields(List<SaveToModelFieldVO> fields) {
        this.fields = fields;
    }
}

package com.youngdatafan.di.run.management.server.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;

import java.util.List;

/**
 * 项目预览执行步骤数据
 *
 * @author gavin
 * @since 2020/2/13 1:45 下午
 */
@Builder
public class ProjectPreviewExecutorDTO {

    @ApiModelProperty("预览数据 字段名集合")
    private List<String> previewFieldNames;

    @ApiModelProperty("预览数据 字段名类型集合")
    private List<String> previewFieldTypes;

    @ApiModelProperty("预览数据")
    private List<String[]> previewRows;

    @ApiModelProperty("运行时日志")
    private String log;

    @ApiModelProperty("错误记录数，大于0则为失败")
    private long errors;

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public long getErrors() {
        return errors;
    }

    public void setErrors(long errors) {
        this.errors = errors;
    }

    public List<String> getPreviewFieldNames() {
        return previewFieldNames;
    }

    public void setPreviewFieldNames(List<String> previewFieldNames) {
        this.previewFieldNames = previewFieldNames;
    }

    public List<String[]> getPreviewRows() {
        return previewRows;
    }

    public void setPreviewRows(List<String[]> previewRows) {
        this.previewRows = previewRows;
    }

    public List<String> getPreviewFieldTypes() {
        return previewFieldTypes;
    }

    public void setPreviewFieldTypes(List<String> previewFieldTypes) {
        this.previewFieldTypes = previewFieldTypes;
    }
}

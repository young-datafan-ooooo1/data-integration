package com.youngdatafan.di.run.management.server.vo;

import com.youngdatafan.di.run.management.util.SqlEncoderUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author gavin
 * @since 2020/2/26 11:35 上午
 */
@ApiModel(description = "预览数据下载")
public class PreviewDownloadVO {

    @ApiModelProperty(value = "数据查询sql")
    @NotBlank
    String dataQuerySql;
    @ApiModelProperty(value = "下载的excel名称", required = true)
    @NotBlank
    private String fileName;
    @ApiModelProperty(value = "数据源Id", required = true)
    @NotBlank
    private String datasourceId;

    @ApiModelProperty(value = "步骤字段数组", required = true)
    @NotNull
    private StepFieldVO[] stepFields;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDataQuerySql() {
        return dataQuerySql;
    }

    public void setDataQuerySql(String dataQuerySql) {
        this.dataQuerySql = SqlEncoderUtil.decode(dataQuerySql);
    }

    public String getDatasourceId() {
        return datasourceId;
    }

    public void setDatasourceId(String datasourceId) {
        this.datasourceId = datasourceId;
    }

    public StepFieldVO[] getStepFields() {
        return stepFields;
    }

    public void setStepFields(StepFieldVO[] stepFields) {
        this.stepFields = stepFields;
    }
}

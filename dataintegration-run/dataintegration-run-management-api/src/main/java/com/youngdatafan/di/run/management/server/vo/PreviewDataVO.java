package com.youngdatafan.di.run.management.server.vo;

import com.youngdatafan.di.run.management.util.SqlEncoderUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author gavin
 * @since 2020/2/21 12:10 下午
 */
@ApiModel(description = "数据预览对象")
public class PreviewDataVO {

    @ApiModelProperty(value = "数据查询sql")
    @NotBlank
    String dataQuerySql;
    @ApiModelProperty(value = "查询多少条记录", required = true)
    @NotNull
    Integer size;
    @ApiModelProperty(value = "数据源Id", required = true)
    @NotNull
    private String dataSourceId;

    public String getDataSourceId() {
        return dataSourceId;
    }

    public void setDataSourceId(String dataSourceId) {
        this.dataSourceId = dataSourceId;
    }

    public String getDataQuerySql() {
        return dataQuerySql;
    }

    public void setDataQuerySql(String dataQuerySql) {
        this.dataQuerySql = SqlEncoderUtil.decode(dataQuerySql);
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}

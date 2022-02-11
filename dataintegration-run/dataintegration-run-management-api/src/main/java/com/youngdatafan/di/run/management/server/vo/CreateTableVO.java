package com.youngdatafan.di.run.management.server.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @Author: jeremychen
 * @Descripition:
 * @Date:2020/3/7 1:24 下午
 */
@Data
@ApiModel(description = "建表实体类")
public class CreateTableVO {

    @ApiModelProperty(value = "数据源id", required = true)
    @NotBlank
    private String dataSourceId;

    @ApiModelProperty(value = "表的schema", required = false)
    private String schema;

    @ApiModelProperty(value = "表名", required = true)
    @NotBlank
    private String tableName;

    @ApiModelProperty(value = "主/技术键字段的名称", required = false)
    private String pk;

    @ApiModelProperty(value = "技术键字段的名称", required = false)
    private String tk;

    @ApiModelProperty(value = "如果需要对主键使用自动递增字段，则为true", required = true)
    @NotBlank
    private boolean use_autoinc;

    @ApiModelProperty(value = "在语句中附加分号", required = true)
    @NotBlank
    private boolean semicolon;

    @ApiModelProperty(value = "字段信息", required = true)
    @NotBlank
    List<FieldsInfoVO> fieldsInfos;

}

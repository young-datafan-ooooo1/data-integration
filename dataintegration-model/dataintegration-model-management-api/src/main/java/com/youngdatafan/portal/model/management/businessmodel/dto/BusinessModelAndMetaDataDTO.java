package com.youngdatafan.portal.model.management.businessmodel.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "业务模型数据源")
public class BusinessModelAndMetaDataDTO {
    @ApiModelProperty(value = "中文名称")
    private String cName;

    @ApiModelProperty(value = "名称即id")
    private String name;

    @ApiModelProperty(value = "数据源id")
    private String datasourceName;

    @ApiModelProperty(value = "表名称")
    private String tableName;

    @ApiModelProperty(value = "排序")
    private String sortNum;

    @ApiModelProperty(value = "schema名称")
    private String schemaName;

    @ApiModelProperty(value = "备注")
    private String description;

    @ApiModelProperty(value = "是否有效")
    private String enabled;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "创建时间")
    private String querySql;

}

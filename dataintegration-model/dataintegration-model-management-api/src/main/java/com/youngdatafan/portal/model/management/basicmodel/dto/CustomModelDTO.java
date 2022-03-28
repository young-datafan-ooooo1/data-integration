package com.youngdatafan.portal.model.management.basicmodel.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Data;

/**
 * 自定义模型返回对象.
 */
@Data
@ApiModel(description = "自定义模型返回对象")
public class CustomModelDTO {

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

    @ApiModelProperty(value = "关联元数据集合")
    private List<BasicModelMetaDataDTO> basicModelMetaDataDTOS;
}

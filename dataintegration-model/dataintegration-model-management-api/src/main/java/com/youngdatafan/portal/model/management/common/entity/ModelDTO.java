package com.youngdatafan.portal.model.management.common.entity;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Data;

/**
 * ModelDTO.
 */
@Data
public class ModelDTO {

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
    private List<ModelMetaDataDTO> modelMetaDataDTOS;

    @ApiModelProperty(value = "基础模型sql")
    private String basicModelSql;

    @ApiModelProperty(value = "业务模型sql")
    private String businessModelSql;

}

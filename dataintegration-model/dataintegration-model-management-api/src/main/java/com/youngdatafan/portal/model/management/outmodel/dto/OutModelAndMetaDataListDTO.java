package com.youngdatafan.portal.model.management.outmodel.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 * 供探索端拖拽模型查询模型信息.
 */
@Data
@ApiModel(value = "供探索端拖拽模型查询模型信息")
public class OutModelAndMetaDataListDTO {

    @ApiModelProperty(value = "中文名称")
    private String cName;

    @ApiModelProperty(value = "名称")
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

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "查询sql")
    private String querySql;

    @ApiModelProperty(value = "关联元数据集合")
    private List<OutModelMetaDataDTO> outModelMetaDataDTOS;
}

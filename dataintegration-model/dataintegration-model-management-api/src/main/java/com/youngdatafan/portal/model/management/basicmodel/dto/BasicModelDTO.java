package com.youngdatafan.portal.model.management.basicmodel.dto;

import com.youngdatafan.portal.model.management.common.entity.ModelFilterVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Data;

/**
 * 基础模型返回对象.
 */
@Data
@ApiModel(description = "基础模型返回对象")
public class BasicModelDTO {

    @ApiModelProperty(value = "中文名称")
    private String cName;

    @ApiModelProperty(value = "名称即id")
    private String name;

    @ApiModelProperty(value = "分组id")
    private String groupId;

    @ApiModelProperty(value = "所属分组")
    private String group;

    @ApiModelProperty(value = "数据源名称")
    private String datasourceName;

    @ApiModelProperty(value = "表名称")
    private String tableName;

    @ApiModelProperty(value = "字段数量")
    private String columnCount = "0";

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

    @ApiModelProperty(value = "创建人")
    private String createUserName;

    @ApiModelProperty(value = "关联元数据集合")
    private List<BasicModelMetaDataDTO> basicModelMetaDataDTOS;

    private List<ModelFilterVO> modelFilterVOS;

}

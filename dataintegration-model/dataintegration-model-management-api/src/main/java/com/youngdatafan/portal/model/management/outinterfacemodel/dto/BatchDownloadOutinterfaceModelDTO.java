package com.youngdatafan.portal.model.management.outinterfacemodel.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 批量下载模板对象.
 */
@Data
@ApiModel("批量下载模板对象")
public class BatchDownloadOutinterfaceModelDTO {

    @ApiModelProperty(value = "基础模型名称")
    private String basicModelName;

    @ApiModelProperty(value = "基础模型分组")
    private String basicModelGroup;

    @ApiModelProperty(value = "基础模型字段")
    private String basicModelColumnName;

    @ApiModelProperty(value = "基础模型显示名称")
    private String basicModelChineseColumnName;

    @ApiModelProperty(value = "业务模型名称")
    private String outinterfaceModelName;

    @ApiModelProperty(value = "模型排序")
    private String modelSort;

    @ApiModelProperty(value = "业务模型字段显示")
    private String outinterfaceModelColumnName;

    @ApiModelProperty(value = "业务模型字段排序")
    private String modelColumnSort;

    @ApiModelProperty(value = "业务模型是否有效")
    private String outinterfaceModelEnable;

    @ApiModelProperty(value = "业务模型备注")
    private String description;

    @ApiModelProperty(value = "业务模型分组")
    private String outinterfaceModelGroupName;

}

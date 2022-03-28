package com.youngdatafan.portal.model.management.outinterfacemodel.dto;

import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.Data;

/**
 * 基础模型.
 */
@Data
public class OutinterfaceModelAndBasicModelName {

    @ApiModelProperty(value = "基础模型名称")
    private String basicModelName;

    @ApiModelProperty(value = "基础模型分组")
    private String basicModelGroup;

    @ApiModelProperty(value = "业务模型名称")
    private String businessModelName;

    @ApiModelProperty(value = "模型排序")
    private String modelSort;

    @ApiModelProperty(value = "业务模型是否有效")
    private String businessModelEnable;

    @ApiModelProperty(value = "业务模型备注")
    private String description;

    @ApiModelProperty(value = "业务模型分组")
    private String businessModelGroupName;

    @ApiModelProperty(value = "业务模型id")
    private String businessModelId;

    private Date createTime;

    private Date updateTime;

    private String createUserId;

    private String querySql;

}

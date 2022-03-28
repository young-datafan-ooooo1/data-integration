package com.youngdatafan.portal.model.management.outmodel.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.Data;

/**
 * OutModelDTO.
 */
@Data
@ApiModel(description = "输出类模型返回对象")
public class OutModelDTO {

    @ApiModelProperty("模型id")
    private String modelId;

    @ApiModelProperty("模型id")
    private String modelName;

    @ApiModelProperty("项目名称")
    private String projectName;

    @ApiModelProperty("项目类型")

    private String projectType;

    @ApiModelProperty("")

    private Integer columnSize;

    @ApiModelProperty("模型id")

    private Integer modelOrder;

    @ApiModelProperty("模型id")
    private String description;

    private String updateFrequency;

    private String dataUpdateStrategy;

    private String dataSaveStrategy;

    @ApiModelProperty("模型id")
    private Date createTime;

    @ApiModelProperty("模型id")
    private String createUserId;

    @ApiModelProperty("模型id")
    private String dataCount;

    private String datasourceId;

    private String tableName;

}

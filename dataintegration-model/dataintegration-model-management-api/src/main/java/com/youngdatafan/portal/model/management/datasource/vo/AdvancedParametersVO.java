package com.youngdatafan.portal.model.management.datasource.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Data;

/**
 * 数据源输入高级参数.
 */
@Data
@ApiModel(description = "数据源输入高级参数")
public class AdvancedParametersVO {

    @ApiModelProperty(value = "高级参数")
    private List<ParameterVo> advancedParameterVo;

    @ApiModelProperty(value = "选项参数")
    private List<ParameterVo> optionsParameterVo;

    @ApiModelProperty(value = "连接池参数")
    private List<ParameterVo> poolParameterVo;

    @ApiModelProperty(value = "集群参数")
    private List<ParameterVo> clusterParameterVo;

}

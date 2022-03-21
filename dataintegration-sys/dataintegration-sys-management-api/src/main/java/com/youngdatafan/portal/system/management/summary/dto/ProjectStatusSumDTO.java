package com.youngdatafan.portal.system.management.summary.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 项目状态汇总.
 */
@Data
@ApiModel("项目状态汇总")
public class ProjectStatusSumDTO {

    @ApiModelProperty("项目类型")
    private String projecType;

    @ApiModelProperty("上线")
    private int onLine;

    @ApiModelProperty("下线")
    private int offline;

    @ApiModelProperty("审核中")
    private int checking;

    @ApiModelProperty("驳回")
    private int refuse;

    @ApiModelProperty("已撤销")
    private int cancel;

    @ApiModelProperty("撤销申请中")
    private int canceling;

    @ApiModelProperty("未发布")
    private int wfb;

}

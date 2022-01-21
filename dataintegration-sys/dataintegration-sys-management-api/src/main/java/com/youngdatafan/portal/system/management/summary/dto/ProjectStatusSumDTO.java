package com.youngdatafan.portal.system.management.summary.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: jeremychen
 * @Descripition:
 * @Date:2020/4/18 6:17 下午
 */
@Data
@ApiModel("项目状态汇总")
public class ProjectStatusSumDTO {

    @ApiModelProperty("项目类型")
    private String projecType;

    @ApiModelProperty("上线")
    private int ONLINE;

    @ApiModelProperty("下线")
    private int OFFLINE;

    @ApiModelProperty("审核中")
    private int CHECKING;

    @ApiModelProperty("驳回")
    private int REFUSE;

    @ApiModelProperty("已撤销")
    private int CANCEL;

    @ApiModelProperty("撤销申请中")
    private int CANCELING;

    @ApiModelProperty("未发布")
    private int wfb;


}

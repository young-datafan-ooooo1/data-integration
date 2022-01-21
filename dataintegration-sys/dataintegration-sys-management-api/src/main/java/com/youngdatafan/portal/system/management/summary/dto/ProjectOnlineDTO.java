package com.youngdatafan.portal.system.management.summary.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: jeremychen
 * @Descripition:
 * @Date:2020/4/18 4:41 下午
 */
@Data
@ApiModel("代办审核")
public class ProjectOnlineDTO {

    @ApiModelProperty("状态")
    private String status;

    @ApiModelProperty("统计")
    private int cnt;

}

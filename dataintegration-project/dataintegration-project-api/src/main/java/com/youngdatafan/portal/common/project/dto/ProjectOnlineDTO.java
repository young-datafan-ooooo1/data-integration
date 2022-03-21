package com.youngdatafan.portal.common.project.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 上线审核信息.
 *
 * @author gavin
 * @since 2020/2/11 12:06 下午
 */
@Data
@NoArgsConstructor
@ApiModel(description = "项目附件对象")
public class ProjectOnlineDTO {

    @ApiModelProperty(value = "审核状态")
    private String status;

    @ApiModelProperty(value = "提交时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "创建者")
    private String createUserId;

    @ApiModelProperty(value = "创建者名称")
    private String createUserName;

    @ApiModelProperty(value = "审核人")
    private String reviewUserId;

    @ApiModelProperty(value = "审核人名称")
    private String reviewUserName;

    @ApiModelProperty(value = "审核备注")
    private String reviewNotes;

}

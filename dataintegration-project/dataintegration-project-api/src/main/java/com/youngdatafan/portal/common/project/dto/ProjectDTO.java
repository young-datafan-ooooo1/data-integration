package com.youngdatafan.portal.common.project.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目信息.
 *
 * @author gavin
 * @since 2020/2/11 12:06 下午
 */
@Data
@NoArgsConstructor
@ApiModel(description = "项目信息")
public class ProjectDTO {

    @ApiModelProperty(value = "项目编号")
    private String projectId;

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "项目描述")
    private String description;

    @ApiModelProperty(value = "项目类型")
    private String projectType;

    @ApiModelProperty(value = "项目状态")
    private String projectStatus;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "创建者")
    private String createUserId;

    @ApiModelProperty(value = "创建者名称")
    private String createUserName;

    @ApiModelProperty(value = "最后修改者")
    private String updateUserId;

    @ApiModelProperty(value = "最后修改者名称")
    private String updateUserName;

    @ApiModelProperty(value = "分组编号")
    private String groupId;

    @ApiModelProperty(value = "分组名称")
    private String groupName;

    @ApiModelProperty(value = "分组描述")
    private String groupDescribe;

    @ApiModelProperty(value = "上线审核信息")
    private ProjectOnlineDTO projectOnlineDTO;

    @ApiModelProperty(value = "操作模式")
    private String opModel;
}

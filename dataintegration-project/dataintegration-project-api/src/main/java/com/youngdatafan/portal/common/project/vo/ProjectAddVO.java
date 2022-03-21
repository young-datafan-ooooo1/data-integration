package com.youngdatafan.portal.common.project.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * 项目新增对象.
 *
 * @author gavin
 * @since 2020/2/11 10:32 上午
 */
@Data
@NoArgsConstructor
@ApiModel(description = "项目管理新增对象")
public class ProjectAddVO {

    @ApiModelProperty(value = "项目id")
    @Length(min = 1, max = 50)
    private String projectId;

    @ApiModelProperty(value = "项目名称", required = true)
    @NotNull
    @Length(min = 1, max = 30)
    private String projectName;

    @ApiModelProperty(value = "项目描述")
    private String description;

    @ApiModelProperty(value = "项目类型，（使用拼音简写）：探索脚本(TSJB)、集成脚本(JSJB)、报表(BB)", required = true)
    @NotNull
    @Length(min = 1, max = 10)
    private String projectType;

    @ApiModelProperty(value = "项目状态，0-正常 1-不可用 2-锁定", required = true)
    @NotNull
    @Length(min = 1, max = 1)
    private String projectStatus;

    @ApiModelProperty(value = "分组编号(FK)", required = true)
    @NotNull
    @Length(min = 1, max = 50)
    private String groupId;

    @ApiModelProperty(value = "分组名称", required = true)
    @NotNull
    @Length(min = 1, max = 50)
    private String groupName;

    @ApiModelProperty(value = "项目附件对象")
    private ProjectFileVO projectFileVO;

}


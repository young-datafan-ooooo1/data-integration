package com.youngdatafan.portal.common.project.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gavin
 * @since 2020/2/17 1:37 下午
 */
@Data
@NoArgsConstructor
@ApiModel(description = "项目分组信息")
public class GroupDTO {

    @ApiModelProperty(value = "分组编号")
    private String groupId;

    @ApiModelProperty(value = "分组名称")
    private String groupName;

    @ApiModelProperty(value = "分组描述")
    private String describe;

    @ApiModelProperty(value = "我的项目")
    private List<ProjectDTO> projects = new ArrayList<>();

    public void add(ProjectDTO projectDTO) {
        projects.add(projectDTO);
    }

}

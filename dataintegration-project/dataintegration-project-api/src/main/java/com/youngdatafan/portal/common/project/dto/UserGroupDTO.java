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
public class UserGroupDTO {

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "用户名称")
    private String describe;

    @ApiModelProperty(value = "我的项目")
    private List<GroupDTO> projects = new ArrayList<>();

    public void add(GroupDTO groupDTO) {
        projects.add(groupDTO);
    }

}

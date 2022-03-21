package com.youngdatafan.portal.common.project.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目分组信息.
 *
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

    /**
     * add .
     *
     * @param groupDTO groupDTO
     */
    public void add(GroupDTO groupDTO) {
        projects.add(groupDTO);
    }

}

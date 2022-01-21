package com.youngdatafan.portal.common.project.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: jeremychen
 * @Descripition:
 * @Date:2020/2/19 12:01 下午
 */
@Data
@ApiModel(description = "用户信息")
public class UserInfoDTO {
    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "用户名称")
    private String describe;

    @ApiModelProperty(value = "项目信息")
    private List<ProjectDTO> projectDTOList;
}

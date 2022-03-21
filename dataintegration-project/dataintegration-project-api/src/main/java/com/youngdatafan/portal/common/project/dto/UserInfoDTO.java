package com.youngdatafan.portal.common.project.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Data;

/**
 * 用户信息.
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

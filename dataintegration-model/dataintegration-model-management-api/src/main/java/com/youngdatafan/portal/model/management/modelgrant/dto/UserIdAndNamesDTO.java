package com.youngdatafan.portal.model.management.modelgrant.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * UserIdAndNamesDTO.
 */
@Data
@ApiModel(description = "返回用户名和用户id对象")
public class UserIdAndNamesDTO {

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "用户id")
    private String userId;

}

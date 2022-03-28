package com.youngdatafan.portal.model.management.modelgrant.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * GroupIdAndGroupNamesDTO.
 */
@Data
public class GroupIdAndGroupNamesDTO {

    @ApiModelProperty(value = "授权组id")
    private String grantId;

    @ApiModelProperty(value = "授权组名称")
    private String grantGroupName;

}

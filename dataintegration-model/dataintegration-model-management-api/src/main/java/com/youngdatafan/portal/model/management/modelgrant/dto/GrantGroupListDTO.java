package com.youngdatafan.portal.model.management.modelgrant.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Data;

/**
 * 返回授权组集合对象.
 */
@Data
@ApiModel(description = "返回授权组集合对象")
public class GrantGroupListDTO {

    @ApiModelProperty(value = "授权组名称集合")
    private List<GroupIdAndGroupNamesDTO> groupIdAndGroupNamesDTOS;

    @ApiModelProperty(value = "授权对象集合")
    private List<AddUserGrantGroupListDTO> addUserGrantGroupListDTOS;

}

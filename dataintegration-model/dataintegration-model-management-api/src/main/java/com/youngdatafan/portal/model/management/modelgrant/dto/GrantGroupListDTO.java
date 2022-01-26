package com.youngdatafan.portal.model.management.modelgrant.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * <p>****************************************************************************</p>
 * <ul style="margin:15px;">
 * <li>Description : description</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2020/2/15 8:59 PM</li>
 * <li>Author      : ksice_xt</li>
 * </ul>
 * <p>****************************************************************************</p>
 */
@ApiModel(description = "返回授权组集合对象")
public class GrantGroupListDTO {

    @ApiModelProperty(value = "授权组名称集合")
    private List<GroupIdAndGroupNamesDTO> groupIdAndGroupNamesDTOS;

    @ApiModelProperty(value = "授权对象集合")
    private List<AddUserGrantGroupListDTO> addUserGrantGroupListDTOS;

    public List<GroupIdAndGroupNamesDTO> getGroupIdAndGroupNamesDTOS() {
        return groupIdAndGroupNamesDTOS;
    }

    public void setGroupIdAndGroupNamesDTOS(List<GroupIdAndGroupNamesDTO> groupIdAndGroupNamesDTOS) {
        this.groupIdAndGroupNamesDTOS = groupIdAndGroupNamesDTOS;
    }

    public List<AddUserGrantGroupListDTO> getAddUserGrantGroupListDTOS() {
        return addUserGrantGroupListDTOS;
    }

    public void setAddUserGrantGroupListDTOS(List<AddUserGrantGroupListDTO> addUserGrantGroupListDTOS) {
        this.addUserGrantGroupListDTOS = addUserGrantGroupListDTOS;
    }
}

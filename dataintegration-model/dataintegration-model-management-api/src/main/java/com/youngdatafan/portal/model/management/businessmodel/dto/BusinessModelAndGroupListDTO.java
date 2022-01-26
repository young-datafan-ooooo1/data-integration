package com.youngdatafan.portal.model.management.businessmodel.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * <p>****************************************************************************</p>
 * <ul style="margin:15px;">
 * <li>Description : description</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2020/2/18 6:24 PM</li>
 * <li>Author      : ksice_xt</li>
 * </ul>
 * <p>****************************************************************************</p>
 */
@ApiModel(description = "分组对象业务对象集合")
public class BusinessModelAndGroupListDTO {

    private String groupId;

    /**
     * 分组对象
     */
    @ApiModelProperty(value = "分组对象")
    private GroupDTO groupDTO;

    /**
     * 业务模型对象
     */
    @ApiModelProperty(value = "业务对象信息集合")
    private List<BusinessModelDTO> businessModels;

    public GroupDTO getGroupDTO() {
        return groupDTO;
    }

    public void setGroupDTO(GroupDTO groupDTO) {
        this.groupDTO = groupDTO;
    }


    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public List<BusinessModelDTO> getBusinessModels() {
        return businessModels;
    }

    public void setBusinessModels(List<BusinessModelDTO> businessModels) {
        this.businessModels = businessModels;
    }

    @Override
    public String toString() {
        return "BusinessModelAndGroupListDTO{" +
                "groupId='" + groupId + '\'' +
                ", groupDTO=" + groupDTO +
                ", businessModels=" + businessModels +
                '}';
    }
}

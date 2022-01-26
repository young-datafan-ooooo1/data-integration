package com.youngdatafan.portal.model.management.modelgrant.dto;

import io.swagger.annotations.ApiModelProperty;

/**
 * <p>****************************************************************************</p>
 * <ul style="margin:15px;">
 * <li>Description : description</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2020/2/21 5:08 PM</li>
 * <li>Author      : ksice_xt</li>
 * </ul>
 * <p>****************************************************************************</p>
 */
public class GroupIdAndGroupNamesDTO {

    @ApiModelProperty(value = "授权组id")
    private String grantId;

    @ApiModelProperty(value = "授权组名称")
    private String grantGroupName;

    public String getGrantId() {
        return grantId;
    }

    public void setGrantId(String grantId) {
        this.grantId = grantId;
    }

    public String getGrantGroupName() {
        return grantGroupName;
    }

    public void setGrantGroupName(String grantGroupName) {
        this.grantGroupName = grantGroupName;
    }
}

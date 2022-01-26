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
@ApiModel(description = "授权组对象")
public class AddUserGrantGroupListDTO {

    @ApiModelProperty(value = "授权组id")
    private String grantId;

    @ApiModelProperty(value = "授权组名称")
    private String grantGroupName;

    @ApiModelProperty(value = "关联模型名称")
    private List<String> businessModelName;

    @ApiModelProperty(value = "模型数量")
    private Integer modelCount;


    public List<String> getBusinessModelName() {
        return businessModelName;
    }

    public void setBusinessModelName(List<String> businessModelName) {
        this.businessModelName = businessModelName;
    }

    public Integer getModelCount() {
        return modelCount;
    }

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

    public void setModelCount(Integer modelCount) {
        this.modelCount = modelCount;
    }
}

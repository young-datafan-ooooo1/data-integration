package com.youngdatafan.portal.model.management.datasource.dto;

import io.swagger.annotations.ApiModelProperty;

/**
 * <p>****************************************************************************</p>
 * <ul style="margin:15px;">
 * <li>Description : description</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2020/12/1 6:28 下午</li>
 * <li>Author      : ksice_xt</li>
 * </ul>
 * <p>****************************************************************************</p>
 */
public class DataSourceListDTO extends DatasourceDTO {

    @ApiModelProperty(value = "是否属于授权数据源")
    private Boolean grantFLag;

    @ApiModelProperty(value = "是否属于授权数据源")
    private String realyUserId ;

    public String getRealyUserId() {
        return realyUserId;
    }

    public void setRealyUserId(String realyUserId) {
        this.realyUserId = realyUserId;
    }

    public Boolean getGrantFLag() {
        return grantFLag;
    }

    public void setGrantFLag(Boolean grantFLag) {
        this.grantFLag = grantFLag;
    }

}

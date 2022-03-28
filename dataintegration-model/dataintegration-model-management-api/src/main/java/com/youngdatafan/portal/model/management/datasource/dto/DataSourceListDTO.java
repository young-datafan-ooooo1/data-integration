package com.youngdatafan.portal.model.management.datasource.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 授权数据源.
 */
@Data
public class DataSourceListDTO extends DatasourceDTO {

    @ApiModelProperty(value = "是否属于授权数据源")
    private Boolean grantFLag;

    @ApiModelProperty(value = "是否属于授权数据源")
    private String realyUserId;

}

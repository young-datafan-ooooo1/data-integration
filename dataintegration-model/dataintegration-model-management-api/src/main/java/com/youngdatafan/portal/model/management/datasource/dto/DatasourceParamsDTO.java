package com.youngdatafan.portal.model.management.datasource.dto;

import io.swagger.annotations.ApiOperation;
import lombok.Data;


/**
 * 解析数据库地址参数.
 */
@Data
@ApiOperation(value = "解析数据库地址参数")
public class DatasourceParamsDTO {
    private String key;

    private String value;

}

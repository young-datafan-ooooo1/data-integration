package com.youngdatafan.portal.model.management.businessmodel.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 业务模型元数据.
 */
@ApiModel("业务模型元数据")
@Data
public class BusinessModelMetaDataVO {

    private static final long serialVersionUID = 1L;

    private String businessModelName;

    private String columnName;

    private String columnSerial;

    private String customColumnName;

    private String columnEtlSql;

}


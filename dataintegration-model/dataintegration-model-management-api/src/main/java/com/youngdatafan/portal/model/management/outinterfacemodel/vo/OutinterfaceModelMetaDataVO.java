package com.youngdatafan.portal.model.management.outinterfacemodel.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 业务模型元数据.
 */
@Data
@ApiModel("业务模型元数据")
public class OutinterfaceModelMetaDataVO {

    private static final long serialVersionUID = 1L;

    private String businessModelName;

    private String columnName;

    private String columnSerial;

    private String customColumnName;

    private String columnEtlSql;

}


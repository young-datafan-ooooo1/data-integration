package com.youngdatafan.portal.model.management.outinterfacemodel.dto;

import lombok.Data;

/**
 * OutinterfaceModelMetaDataCopyDTO.
 */
@Data
public class OutinterfaceModelMetaDataCopyDTO {

    private static final long serialVersionUID = 1L;

    private String businessModelName;

    private String columnName;

    private String columnSerial;

    private String customColumnName;

    private String columnEtlSql;

}

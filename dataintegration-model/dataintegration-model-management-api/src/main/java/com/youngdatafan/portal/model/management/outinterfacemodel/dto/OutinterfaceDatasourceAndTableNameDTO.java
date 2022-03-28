package com.youngdatafan.portal.model.management.outinterfacemodel.dto;

import java.util.Date;
import lombok.Data;

/**
 * OutinterfaceDatasourceAndTableNameDTO.
 */
@Data
public class OutinterfaceDatasourceAndTableNameDTO {

    private String dsName;

    private String describe;

    private String dsType;

    private String dsUrl;

    private String driverClassName;

    private String dsUsername;

    private String dsPassword;

    private String enabled;

    private Date createTime;

    private Date updateTime;

    private String createUserId;

    private String datasourceId;

    private String dsConectorSetting;

    private String tableName;

    private String tableSchema;

}

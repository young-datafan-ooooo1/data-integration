package com.youngdatafan.portal.model.management.basicmodel.dto;

import java.util.Date;
import lombok.Data;

/**
 * BasicModelCopyDTO.
 */
@Data
public class BasicModelCopyDTO {

    private String modelName;

    private String description;

    private String modelType;

    private String dsName;

    private String tableSchema;

    private String tableName;

    private String tableChineseName;

    private String tableDescription;

    private Date statisticsTime;

    private Integer rowCnt;

    private String enabled;

    private Date createTime;

    private Date updateTime;

    private String createUserId;

    private String modelSort;

    private String cName;

    private String groupType;

}

package com.youngdatafan.portal.model.management.outmodel.dto;

import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 * OutModelAndMetaDataDTO.
 */
@Data
public class OutModelAndMetaDataDTO {

    private String modelId;

    private String groupName;

    private String modelName;

    private String projectName;

    private String projectType;

    private Integer columnSize = 0;

    private Integer modelOrder;

    private String description;

    private String updateFrequency;

    private String dataUpdateStrategy;

    private String dataSaveStrategy;

    private Date createTime;

    private Date updateTime;

    private String createUserId;

    private String dataCount;

    private String datasourceId;

    private String tableName;

    private String stepName;

    private String querySql;

    private String projectId;

    private List<OutModelMetaDataDTO> outModelMetaDataDTOS;
}

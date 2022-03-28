package com.youngdatafan.portal.model.management.businessmodel.dto;

import com.youngdatafan.portal.model.management.common.entity.ModelFilterVO;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 * BusinessModelDTO.
 */
@Data
public class BusinessModelDTO implements Serializable {
    private String modelName;

    private String chineseName;

    private String basicModelName;

    private String description;

    private String filterRule;

    private String enabled;

    private Date createTime;

    private Date updateTime;

    private String createUserId;

    private String querySql;

    private String sortNum;

    private String columnCount = "0";

    private String createUserName;

    private String groupName;

    private String cName;

    private String basicmodelGroupName;

    private String basicmodelGroupId;

    private String modelType;

    private List<BusinessModelMetaDataDTO> businessModelMetaDataDTOS;

    private List<ModelFilterVO> modelFilterVOS;

}

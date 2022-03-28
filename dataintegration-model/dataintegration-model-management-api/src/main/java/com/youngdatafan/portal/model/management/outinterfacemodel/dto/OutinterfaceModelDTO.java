package com.youngdatafan.portal.model.management.outinterfacemodel.dto;

import com.youngdatafan.portal.model.management.common.entity.ModelFilterVO;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 * OutinterfaceModelDTO.
 */
@Data
public class OutinterfaceModelDTO implements Serializable {
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

    private List<OutinterfaceModelMetaDataDTO> outinterfaceModelMetaDataDTOS;

    private List<ModelFilterVO> modelFilterVOS;

}

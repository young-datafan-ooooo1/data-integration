package com.youngdatafan.portal.model.management.basicmodel.dto;

import com.youngdatafan.portal.model.management.common.entity.BasicModelMetaDataVO;
import lombok.Data;

/**
 * BasicModelMetaDataDTO.
 */
@Data
public class BasicModelMetaDataDTO extends BasicModelMetaDataVO {

    private String columnEtlSql;

}

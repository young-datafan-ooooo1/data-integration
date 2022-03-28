package com.youngdatafan.portal.model.management.basicmodel.dto;

import java.util.List;
import lombok.Data;

/**
 * BasicModelAndMetaDataListDTO.
 */
@Data
public class BasicModelAndMetaDataListDTO {
    private BasicModelCopyDTO basicModelDTO;

    private List<BasicModelMetaDataDTO> basicModelMetaDataDTO;

}

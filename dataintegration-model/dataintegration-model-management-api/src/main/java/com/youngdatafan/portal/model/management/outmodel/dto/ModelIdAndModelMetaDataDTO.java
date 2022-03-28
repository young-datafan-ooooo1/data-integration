package com.youngdatafan.portal.model.management.outmodel.dto;

import java.util.List;
import lombok.Data;

/**
 * ModelIdAndModelMetaDataDTO.
 */
@Data
public class ModelIdAndModelMetaDataDTO {

    private String modelId;

    private List<OutModelMetaDataDTO> list;

}

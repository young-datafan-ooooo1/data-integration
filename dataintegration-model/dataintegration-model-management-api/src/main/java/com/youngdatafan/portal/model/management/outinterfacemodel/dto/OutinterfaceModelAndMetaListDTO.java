package com.youngdatafan.portal.model.management.outinterfacemodel.dto;

import java.util.List;
import lombok.Data;


/**
 * OutinterfaceModelAndMetaListDTO.
 */
@Data
public class OutinterfaceModelAndMetaListDTO {

    private OutinterfaceModelAndBasicModelName businessModelAndBasicModelName;

    private List<OutinterfaceModelMetaDataCopyDTO> list;

}

package com.youngdatafan.portal.model.management.businessmodel.dto;

import java.util.List;
import lombok.Data;

/**
 * ModelTypeAndGroupListDTO.
 */
@Data
public class ModelTypeAndGroupListDTO {

    private String groupTypeName;

    private List<BusinessModelAndGroupListDTO> businessModelAndGroupListDTOS;

}

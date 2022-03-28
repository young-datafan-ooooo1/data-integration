package com.youngdatafan.portal.model.management.outinterfacemodel.dto;

import java.util.List;
import lombok.Data;

/**
 * OutinterfaceModelTypeAndGroupListDTO.
 */
@Data
public class OutinterfaceModelTypeAndGroupListDTO {

    private String groupTypeName;

    private List<OutinterfaceModelAndGroupListDTO> outinterfaceModelAndGroupListDTOS;

}

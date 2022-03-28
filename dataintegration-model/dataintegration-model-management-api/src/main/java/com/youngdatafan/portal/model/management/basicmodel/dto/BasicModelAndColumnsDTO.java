package com.youngdatafan.portal.model.management.basicmodel.dto;

import java.util.List;
import lombok.Data;

/**
 * BasicModelAndColumnsDTO.
 */
@Data
public class BasicModelAndColumnsDTO {

    private BasicModelCopyDTO basicModelCopyDTO;

    private List<AllColumnDTO> list;

}

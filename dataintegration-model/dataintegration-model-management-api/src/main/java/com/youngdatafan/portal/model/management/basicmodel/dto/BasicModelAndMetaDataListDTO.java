package com.youngdatafan.portal.model.management.basicmodel.dto;

import java.util.List;

/**
 * <p>****************************************************************************</p>
 * <ul style="margin:15px;">
 * <li>Description : description</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2020/2/23 8:40 PM</li>
 * <li>Author      : ksice_xt</li>
 * </ul>
 * <p>****************************************************************************</p>
 */
public class BasicModelAndMetaDataListDTO {
    BasicModelCopyDTO basicModelDTO;


    List<BasicModelMetaDataDTO> basicModelMetaDataDTO;

    public BasicModelCopyDTO getBasicModelDTO() {
        return basicModelDTO;
    }

    public void setBasicModelDTO(BasicModelCopyDTO basicModelDTO) {
        this.basicModelDTO = basicModelDTO;
    }

    public List<BasicModelMetaDataDTO> getBasicModelMetaDataDTO() {
        return basicModelMetaDataDTO;
    }

    public void setBasicModelMetaDataDTO(List<BasicModelMetaDataDTO> basicModelMetaDataDTO) {
        this.basicModelMetaDataDTO = basicModelMetaDataDTO;
    }
}

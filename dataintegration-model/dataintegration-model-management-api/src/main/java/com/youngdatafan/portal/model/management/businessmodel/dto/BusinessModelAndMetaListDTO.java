package com.youngdatafan.portal.model.management.businessmodel.dto;

import java.util.List;

/**
 * <p>****************************************************************************</p>
 * <ul style="margin:15px;">
 * <li>Description : description</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2020/2/25 1:49 PM</li>
 * <li>Author      : ksice_xt</li>
 * </ul>
 * <p>****************************************************************************</p>
 */
public class BusinessModelAndMetaListDTO {

    private BusinessModelAndBasicModelName businessModelAndBasicModelName;


    private List<BusinessModelMetaDataCopyDTO> list;

    public BusinessModelAndBasicModelName getBusinessModelAndBasicModelName() {
        return businessModelAndBasicModelName;
    }

    public void setBusinessModelAndBasicModelName(BusinessModelAndBasicModelName businessModelAndBasicModelName) {
        this.businessModelAndBasicModelName = businessModelAndBasicModelName;
    }

    public List<BusinessModelMetaDataCopyDTO> getList() {
        return list;
    }

    public void setList(List<BusinessModelMetaDataCopyDTO> list) {
        this.list = list;
    }
}

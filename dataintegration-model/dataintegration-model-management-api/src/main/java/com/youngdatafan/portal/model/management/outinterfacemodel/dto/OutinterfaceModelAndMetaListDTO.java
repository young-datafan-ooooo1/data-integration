package com.youngdatafan.portal.model.management.outinterfacemodel.dto;

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
public class OutinterfaceModelAndMetaListDTO {

    private OutinterfaceModelAndBasicModelName businessModelAndBasicModelName;


    private List<OutinterfaceModelMetaDataCopyDTO> list;

    public OutinterfaceModelAndBasicModelName getBusinessModelAndBasicModelName() {
        return businessModelAndBasicModelName;
    }

    public void setBusinessModelAndBasicModelName(OutinterfaceModelAndBasicModelName businessModelAndBasicModelName) {
        this.businessModelAndBasicModelName = businessModelAndBasicModelName;
    }

    public List<OutinterfaceModelMetaDataCopyDTO> getList() {
        return list;
    }

    public void setList(List<OutinterfaceModelMetaDataCopyDTO> list) {
        this.list = list;
    }
}

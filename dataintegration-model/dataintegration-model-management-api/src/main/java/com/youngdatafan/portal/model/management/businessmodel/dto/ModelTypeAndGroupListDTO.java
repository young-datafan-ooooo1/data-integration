package com.youngdatafan.portal.model.management.businessmodel.dto;

import java.util.List;

/**
 * <p>****************************************************************************</p>
 * <ul style="margin:15px;">
 * <li>Description : description</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2020/2/22 6:57 PM</li>
 * <li>Author      : ksice_xt</li>
 * </ul>
 * <p>****************************************************************************</p>
 */
public class ModelTypeAndGroupListDTO {

    private String groupTypeName;

    private List<BusinessModelAndGroupListDTO> businessModelAndGroupListDTOS;

    public String getGroupTypeName() {
        return groupTypeName;
    }

    public void setGroupTypeName(String groupTypeName) {
        this.groupTypeName = groupTypeName;
    }

    public List<BusinessModelAndGroupListDTO> getBusinessModelAndGroupListDTOS() {
        return businessModelAndGroupListDTOS;
    }

    public void setBusinessModelAndGroupListDTOS(List<BusinessModelAndGroupListDTO> businessModelAndGroupListDTOS) {
        this.businessModelAndGroupListDTOS = businessModelAndGroupListDTOS;
    }
}

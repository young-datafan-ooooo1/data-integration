package com.youngdatafan.portal.model.management.modelgrant.dto;

import java.util.List;

/**
 * <p>****************************************************************************</p>
 * <ul style="margin:15px;">
 * <li>Description : description</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2020/3/6 12:40 PM</li>
 * <li>Author      : ksice_xt</li>
 * </ul>
 * <p>****************************************************************************</p>
 */
public class GroupIdBusinessModelListDTO {
    private String groupid;

    private List<BusinessModelListDTO> businessModelListDTOS;

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public List<BusinessModelListDTO> getBusinessModelListDTOS() {
        return businessModelListDTOS;
    }

    public void setBusinessModelListDTOS(List<BusinessModelListDTO> businessModelListDTOS) {
        this.businessModelListDTOS = businessModelListDTOS;
    }
}

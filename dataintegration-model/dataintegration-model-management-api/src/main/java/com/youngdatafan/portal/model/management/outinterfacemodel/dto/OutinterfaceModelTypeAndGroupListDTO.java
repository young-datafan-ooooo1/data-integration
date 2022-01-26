package com.youngdatafan.portal.model.management.outinterfacemodel.dto;

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
public class OutinterfaceModelTypeAndGroupListDTO {

    private String groupTypeName;

    private List<OutinterfaceModelAndGroupListDTO> outinterfaceModelAndGroupListDTOS;

    public String getGroupTypeName() {
        return groupTypeName;
    }

    public void setGroupTypeName(String groupTypeName) {
        this.groupTypeName = groupTypeName;
    }

    public List<OutinterfaceModelAndGroupListDTO> getOutinterfaceModelAndGroupListDTOS() {
        return outinterfaceModelAndGroupListDTOS;
    }

    public void setOutinterfaceModelAndGroupListDTOS(List<OutinterfaceModelAndGroupListDTO> outinterfaceModelAndGroupListDTOS) {
        this.outinterfaceModelAndGroupListDTOS = outinterfaceModelAndGroupListDTOS;
    }
}

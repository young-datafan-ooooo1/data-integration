package com.youngdatafan.portal.model.management.basicmodel.dto;

import java.util.List;

/**
 * <p>****************************************************************************</p>
 * <ul style="margin:15px;">
 * <li>Description : description</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2020/2/24 10:11 AM</li>
 * <li>Author      : ksice_xt</li>
 * </ul>
 * <p>****************************************************************************</p>
 */
public class BasicModelAndColumnsDTO {

    private BasicModelCopyDTO basicModelCopyDTO;


    private List<AllColumnDTO> list;

    public BasicModelCopyDTO getBasicModelCopyDTO() {
        return basicModelCopyDTO;
    }

    public void setBasicModelCopyDTO(BasicModelCopyDTO basicModelCopyDTO) {
        this.basicModelCopyDTO = basicModelCopyDTO;
    }

    public List<AllColumnDTO> getList() {
        return list;
    }

    public void setList(List<AllColumnDTO> list) {
        this.list = list;
    }
}

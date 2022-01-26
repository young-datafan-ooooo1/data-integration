package com.youngdatafan.portal.model.management.outmodel.dto;

import java.util.List;

/**
 * <p>****************************************************************************</p>
 * <ul style="margin:15px;">
 * <li>Description : description</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2020/2/28 1:17 PM</li>
 * <li>Author      : ksice_xt</li>
 * </ul>
 * <p>****************************************************************************</p>
 */
public class OutModelAndMetaDataWithPageDTO {
    private Long total;

    private List<OutModelAndMetaDataDTO> outModelAndMetaData;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<OutModelAndMetaDataDTO> getOutModelAndMetaData() {
        return outModelAndMetaData;
    }

    public void setOutModelAndMetaData(List<OutModelAndMetaDataDTO> outModelAndMetaData) {
        this.outModelAndMetaData = outModelAndMetaData;
    }
}

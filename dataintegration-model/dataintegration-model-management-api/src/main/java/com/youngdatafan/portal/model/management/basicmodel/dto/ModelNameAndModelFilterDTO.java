package com.youngdatafan.portal.model.management.basicmodel.dto;

import com.youngdatafan.portal.model.management.common.entity.ModelFilterVO;

import java.util.List;

/**
 * <p>****************************************************************************</p>
 * <ul style="margin:15px;">
 * <li>Description : description</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2020/5/8 11:05 AM</li>
 * <li>Author      : ksice_xt</li>
 * </ul>
 * <p>****************************************************************************</p>
 */
public class ModelNameAndModelFilterDTO {

    private String modelName;

    private List<ModelFilterVO> modelFilterVOS;


    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public List<ModelFilterVO> getModelFilterVOS() {
        return modelFilterVOS;
    }

    public void setModelFilterVOS(List<ModelFilterVO> modelFilterVOS) {
        this.modelFilterVOS = modelFilterVOS;
    }

}

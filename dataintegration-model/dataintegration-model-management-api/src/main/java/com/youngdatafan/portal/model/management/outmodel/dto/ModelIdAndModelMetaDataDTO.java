package com.youngdatafan.portal.model.management.outmodel.dto;

import java.util.List;

/**
 * <p>****************************************************************************</p>
 * <ul style="margin:15px;">
 * <li>Description : description</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2020/2/28 12:46 PM</li>
 * <li>Author      : ksice_xt</li>
 * </ul>
 * <p>****************************************************************************</p>
 */
public class ModelIdAndModelMetaDataDTO {

    private String modelId;

    private List<OutModelMetaDataDTO> list;

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public List<OutModelMetaDataDTO> getList() {
        return list;
    }

    public void setList(List<OutModelMetaDataDTO> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "ModelIdAndModelMetaDataDTO{" +
                "modelId='" + modelId + '\'' +
                ", list=" + list +
                '}';
    }
}

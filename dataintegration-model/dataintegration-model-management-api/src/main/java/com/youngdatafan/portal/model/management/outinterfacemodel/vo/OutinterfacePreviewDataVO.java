package com.youngdatafan.portal.model.management.outinterfacemodel.vo;

import com.youngdatafan.portal.model.management.common.entity.BasicModelMetaDataVO;
import com.youngdatafan.portal.model.management.common.entity.ModelFilterVO;

import java.util.List;

/**
 * <p>****************************************************************************</p>
 * <ul style="margin:15px;">
 * <li>Description : description</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2020/5/8 5:29 PM</li>
 * <li>Author      : ksice_xt</li>
 * </ul>
 * <p>****************************************************************************</p>
 */
public class OutinterfacePreviewDataVO {
    private String basicModelId;

    private List<ModelFilterVO> modelFilterVOS;

    private List<BasicModelMetaDataVO> modelMetaDataVOS;

    public List<BasicModelMetaDataVO> getModelMetaDataVOS() {
        return modelMetaDataVOS;
    }

    public void setModelMetaDataVOS(List<BasicModelMetaDataVO> modelMetaDataVOS) {
        this.modelMetaDataVOS = modelMetaDataVOS;
    }

    public String getBasicModelId() {
        return basicModelId;
    }

    public void setBasicModelId(String basicModelId) {
        this.basicModelId = basicModelId;
    }

    public List<ModelFilterVO> getModelFilterVOS() {
        return modelFilterVOS;
    }

    public void setModelFilterVOS(List<ModelFilterVO> modelFilterVOS) {
        this.modelFilterVOS = modelFilterVOS;
    }
}

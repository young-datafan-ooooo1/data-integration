package com.youngdatafan.portal.model.management.outinterfacemodel.vo;

import com.youngdatafan.portal.model.management.common.entity.BasicModelMetaDataVO;
import com.youngdatafan.portal.model.management.common.entity.ModelFilterVO;
import java.util.List;
import lombok.Data;

/**
 * OutinterfacePreviewDataVO.
 */
@Data
public class OutinterfacePreviewDataVO {
    private String basicModelId;

    private List<ModelFilterVO> modelFilterVOS;

    private List<BasicModelMetaDataVO> modelMetaDataVOS;

}

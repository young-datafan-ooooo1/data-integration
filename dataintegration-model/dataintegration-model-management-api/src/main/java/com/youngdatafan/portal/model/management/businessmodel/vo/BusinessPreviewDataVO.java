package com.youngdatafan.portal.model.management.businessmodel.vo;

import com.youngdatafan.portal.model.management.common.entity.BasicModelMetaDataVO;
import com.youngdatafan.portal.model.management.common.entity.ModelFilterVO;
import java.util.List;
import lombok.Data;

/**
 * BusinessPreviewDataVO.
 */
@Data
public class BusinessPreviewDataVO {
    private String basicModelId;

    private List<ModelFilterVO> modelFilterVOS;

    private List<BasicModelMetaDataVO> modelMetaDataVOS;

}

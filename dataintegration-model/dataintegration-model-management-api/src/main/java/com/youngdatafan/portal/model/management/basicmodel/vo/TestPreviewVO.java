package com.youngdatafan.portal.model.management.basicmodel.vo;

import com.youngdatafan.portal.model.management.common.entity.BasicModelMetaDataVO;
import com.youngdatafan.portal.model.management.common.entity.ModelFilterVO;
import java.util.List;
import lombok.Data;

/**
 * TestPreviewVO.
 */
@Data
public class TestPreviewVO {
    private String datasourceId;

    private List<BasicModelMetaDataVO> metaDatas;

    private List<ModelFilterVO> modelFilter;

    private String tableName;

}

package com.youngdatafan.portal.model.management.basicmodel.dto;

import com.youngdatafan.portal.model.management.common.entity.ModelFilterVO;
import java.util.List;
import lombok.Data;

/**
 * ModelNameAndModelFilterDTO.
 */
@Data
public class ModelNameAndModelFilterDTO {

    private String modelName;

    private List<ModelFilterVO> modelFilterVOS;

}

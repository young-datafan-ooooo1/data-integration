package com.youngdatafan.portal.model.management.outinterfacemodel.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Data;

/**
 * 分组对象业务对象集合.
 */
@Data
@ApiModel(description = "分组对象业务对象集合")
public class OutinterfaceModelAndGroupListDTO {

    private String groupId;

    /**
     * 分组对象.
     */
    @ApiModelProperty(value = "分组对象")
    private OutinterfaceGroupDTO groupDTO;

    /**
     * 业务模型对象.
     */
    @ApiModelProperty(value = "业务对象信息集合")
    private List<OutinterfaceModelDTO> outinterfaceModels;

}

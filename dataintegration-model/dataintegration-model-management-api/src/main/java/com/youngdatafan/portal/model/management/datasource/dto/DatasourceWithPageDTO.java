package com.youngdatafan.portal.model.management.datasource.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Data;

/**
 * 数据源分页对象.
 */
@Data
@ApiModel(description = "数据源分页对象")
public class DatasourceWithPageDTO {
    @ApiModelProperty(value = "数据源对象")
    private List<DatasourceDTO> datasourceDTO;

    @ApiModelProperty(value = "总条数")
    private Long total;

}


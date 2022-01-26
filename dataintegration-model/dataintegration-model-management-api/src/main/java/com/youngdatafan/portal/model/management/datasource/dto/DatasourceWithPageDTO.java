package com.youngdatafan.portal.model.management.datasource.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * <p>****************************************************************************</p>
 * <ul style="margin:15px;">
 * <li>Description : description</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2020/2/21 3:19 PM</li>
 * <li>Author      : ksice_xt</li>
 * </ul>
 * <p>****************************************************************************</p>
 */
@ApiModel(description = "数据源分页对象")
public class DatasourceWithPageDTO {
    @ApiModelProperty(value = "数据源对象")
    private List<DatasourceDTO> datasourceDTO;

    @ApiModelProperty(value = "总条数")
    private Long total;

    public void setDatasourceDTO(List<DatasourceDTO> datasourceDTO) {
        this.datasourceDTO = datasourceDTO;
    }

    public List<DatasourceDTO> getDatasourceDTO() {
        return datasourceDTO;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}


package com.youngdatafan.portal.model.management.basicmodel.dto;

import io.swagger.annotations.ApiModel;

import java.util.List;
import lombok.Data;

/**
 * 表和视图.
 */
@Data
@ApiModel(description = "表和视图")
public class TablesAndViewDTO {
    private List<String> tables;

    private List<String> views;

    public TablesAndViewDTO(List<String> tables, List<String> views) {
        this.tables = tables;
        this.views = views;
    }

}

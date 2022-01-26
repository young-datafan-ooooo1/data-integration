package com.youngdatafan.portal.model.management.datasource.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;



@ApiModel(description = "数据源 Explorer")
public class StatementDto {

    @ApiModelProperty(value = "Schemas")
    private List<String> Schemas;

    @ApiModelProperty(value = "所有表")
    private List<String> tables;

    @ApiModelProperty(value = "所有视图")

    private List<String> views;

    @ApiModelProperty(value = "所有同义词")
    private List<String> synonyms;


    public List<String> getSchemas() {
        return Schemas;
    }

    public void setSchemas(List<String> schemas) {
        Schemas = schemas;
    }

    public List<String> getTables() {
        return tables;
    }

    public void setTables(List<String> tables) {
        this.tables = tables;
    }

    public List<String> getViews() {
        return views;
    }

    public void setViews(List<String> views) {
        this.views = views;
    }

    public List<String> getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(List<String> synonyms) {
        this.synonyms = synonyms;
    }
}

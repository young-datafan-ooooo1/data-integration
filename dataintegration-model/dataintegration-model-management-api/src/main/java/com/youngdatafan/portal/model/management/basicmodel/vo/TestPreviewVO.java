package com.youngdatafan.portal.model.management.basicmodel.vo;

import com.youngdatafan.portal.model.management.common.entity.BasicModelMetaDataVO;
import com.youngdatafan.portal.model.management.common.entity.ModelFilterVO;

import java.util.List;

/**
 * <p>****************************************************************************</p>
 * <ul style="margin:15px;">
 * <li>Description : description</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2020/5/6 5:42 PM</li>
 * <li>Author      : ksice_xt</li>
 * </ul>
 * <p>****************************************************************************</p>
 */
public class TestPreviewVO {
    private String datasourceId;
    private List<BasicModelMetaDataVO> metaDatas;
    private List<ModelFilterVO> modelFilter;
    private String tableName;

    public String getDatasourceId() {
        return datasourceId;
    }

    public void setDatasourceId(String datasourceId) {
        this.datasourceId = datasourceId;
    }

    public List<BasicModelMetaDataVO> getMetaDatas() {
        return metaDatas;
    }

    public void setMetaDatas(List<BasicModelMetaDataVO> metaDatas) {
        this.metaDatas = metaDatas;
    }

    public List<ModelFilterVO> getModelFilter() {
        return modelFilter;
    }

    public void setModelFilter(List<ModelFilterVO> modelFilter) {
        this.modelFilter = modelFilter;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}

package com.youngdatafan.portal.model.management.datasource.vo;

import io.swagger.annotations.ApiModel;

import java.util.Arrays;

/**
 * @author Echo_Sxl on 2020/8/6 14:33
 * @version 1.0
 */
@ApiModel(description = "授权数据源参数")
public class GrantDatasourceVo {

    private String dsId;

    private String[] userId;

    public String getDsId() {
        return dsId;
    }

    public void setDsId(String dsId) {
        this.dsId = dsId;
    }

    public String[] getUserId() {
        return userId;
    }

    public void setUserId(String[] userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "GrantDatasourceVo{" +
                "dsId='" + dsId + '\'' +
                ", userId=" + Arrays.toString(userId) +
                '}';
    }
}

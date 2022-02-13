package com.youngdatafan.di.run.management.steps.connect.vo;

import io.swagger.annotations.ApiModel;

import java.util.StringJoiner;

@ApiModel(description = "获取字段")
public class QueryVO {

    /**
     * 数据库连接信息
     */
    private ConnectionDetailVO connectionDetailVO;

    /**
     * 查询sql
     */
    private String querySql;

    public ConnectionDetailVO getConnectionDetailVO() {
        return connectionDetailVO;
    }

    public void setConnectionDetailVO(ConnectionDetailVO connectionDetailVO) {
        this.connectionDetailVO = connectionDetailVO;
    }

    public String getQuerySql() {
        return querySql;
    }

    public void setQuerySql(String querySql) {
        this.querySql = querySql;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", QueryVO.class.getSimpleName() + "[", "]")
                .add("connectionDetailVO=" + connectionDetailVO)
                .add("getFieldsQuerySql='" + querySql + "'")
                .toString();
    }
}

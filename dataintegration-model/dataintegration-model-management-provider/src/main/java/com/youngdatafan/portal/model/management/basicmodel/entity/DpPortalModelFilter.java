package com.youngdatafan.portal.model.management.basicmodel.entity;

import java.io.Serializable;

/**
 * 模型过滤信息(DpPortalModelFilter)实体类
 *
 * @author makejava
 * @since 2020-05-06 16:06:16
 */
public class DpPortalModelFilter implements Serializable {
    private static final long serialVersionUID = 684292281115008754L;
    /**
     * 模型id
     */
    private String modelId;
    /**
     * 模型字段
     */
    private String modelColumn;
    /**
     * 操作符号
     */
    private String opertationSign;
    /**
     * 固定值
     */
    private String fixedValue;
    /**
     * 字段值
     */
    private String modelColumnVal;
    /**
     * 变量
     */
    private String variable;
    /**
     * 与上一个条件的关系
     */
    private String relation;


    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getModelColumn() {
        return modelColumn;
    }

    public void setModelColumn(String modelColumn) {
        this.modelColumn = modelColumn;
    }

    public String getOpertationSign() {
        return opertationSign;
    }

    public void setOpertationSign(String opertationSign) {
        this.opertationSign = opertationSign;
    }

    public String getFixedValue() {
        return fixedValue;
    }

    public void setFixedValue(String fixedValue) {
        this.fixedValue = fixedValue;
    }

    public String getModelColumnVal() {
        return modelColumnVal;
    }

    public void setModelColumnVal(String modelColumnVal) {
        this.modelColumnVal = modelColumnVal;
    }

    public String getVariable() {
        return variable;
    }

    public void setVariable(String variable) {
        this.variable = variable;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

}
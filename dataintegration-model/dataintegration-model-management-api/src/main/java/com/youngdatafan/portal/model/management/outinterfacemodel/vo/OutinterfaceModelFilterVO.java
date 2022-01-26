package com.youngdatafan.portal.model.management.outinterfacemodel.vo;

/**
 * <p>****************************************************************************</p>
 * <ul style="margin:15px;">
 * <li>Description : description</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2020/5/6 2:05 PM</li>
 * <li>Author      : ksice_xt</li>
 * </ul>
 * <p>****************************************************************************</p>
 */
public class OutinterfaceModelFilterVO {
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

    private String columnType;

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

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

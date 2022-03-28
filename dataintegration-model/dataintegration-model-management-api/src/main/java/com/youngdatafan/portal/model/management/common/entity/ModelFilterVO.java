package com.youngdatafan.portal.model.management.common.entity;

import lombok.Data;

/**
 * ModelFilterVO.
 */
@Data
public class ModelFilterVO {
    /**
     * 模型id.
     */
    private String modelId;

    /**
     * 模型字段.
     */
    private String modelColumn;

    /**
     * 操作符号.
     */
    private String opertationSign;

    /**
     * 固定值.
     */
    private String fixedValue;

    /**
     * 字段值.
     */
    private String modelColumnVal;

    /**
     * 变量.
     */
    private String variable;

    /**
     * 与上一个条件的关系.
     */
    private String relation;

    private String columnType;

}

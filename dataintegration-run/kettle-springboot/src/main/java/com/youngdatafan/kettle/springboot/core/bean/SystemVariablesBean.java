package com.youngdatafan.kettle.springboot.core.bean;

public class SystemVariablesBean {

    /**
     * 字段名
     */
    private String name;

    /**
     * 字段描述
     */
    private String desc;

    /**
     * aviator取值表达式
     */
    private String expression;


    /**
     * 格式化字符串
     */
    private String formatSting;

    /**
     * 表达式值
     */
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getFormatSting() {
        return formatSting;
    }

    public void setFormatSting(String formatSting) {
        this.formatSting = formatSting;
    }
}

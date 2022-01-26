package com.youngdatafan.portal.model.management.forward.superset;

import com.youngdatafan.portal.model.management.util.enums.StatisticsTypeEnum;

/**
 * <p>****************************************************************************</p>
 * <ul style="margin:15px;">
 * <li>Description : description</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2019/10/21 6:01 PM</li>
 * <li>Author      : ksice_xt</li>
 * </ul>
 * <p>****************************************************************************</p>
 */
public class XmlBean {

    //字段别名
    private String chineseName;

    //字段名称
    private String column;

    //字段类型
    private String columnType;

    //字段类型
    private String typeEnum;

    private String statisticsType;

    private StatisticsTypeEnum statisticsTypeEnum;

    public StatisticsTypeEnum getStatisticsTypeEnum() {
        return statisticsTypeEnum;
    }

    public void setStatisticsTypeEnum(StatisticsTypeEnum statisticsTypeEnum) {
        this.statisticsTypeEnum = statisticsTypeEnum;
    }

    public String getStatisticsType() {
        return statisticsType;
    }

    public void setStatisticsType(String statisticsType) {
        this.statisticsType = statisticsType;
    }

    public String getChineseName() {
        return chineseName;
    }

    public void setChineseName(String chineseName) {
        this.chineseName = chineseName;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getTypeEnum() {
        return typeEnum;
    }

    public void setTypeEnum(String typeEnum) {
        this.typeEnum = typeEnum;
    }

    @Override
    public String toString() {
        return "XmlBean{" +
                "chineseName='" + chineseName + '\'' +
                ", column='" + column + '\'' +
                ", columnType='" + columnType + '\'' +
                ", typeEnum='" + typeEnum + '\'' +
                ", statisticsTypeEnum=" + statisticsTypeEnum +
                '}';
    }
}

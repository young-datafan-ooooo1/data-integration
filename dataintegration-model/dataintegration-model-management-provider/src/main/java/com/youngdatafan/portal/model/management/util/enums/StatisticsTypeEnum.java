package com.youngdatafan.portal.model.management.util.enums;

public enum StatisticsTypeEnum {
  MIN("MIN", "最小值"),
  MAX("MAX", "最大值"),
  AVG("AVG", "平均值"),
  STDDEV("STDDEV", "标准差"),
  VAR("VAR", "方差"),
  GROUP("GROUP", "分组"),
  COUNT("COUNT", "计数");
  private final String code;
  private final String desc;

  StatisticsTypeEnum(String code, String desc) {
    this.code = code;
    this.desc = desc;
  }

  public String code() {
    return this.code;
  }

  public String getDesc() {
    return this.desc;
  }
}

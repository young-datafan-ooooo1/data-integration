package com.youngdatafan.portal.system.management.summary.dto;

/**
 * OperationStatusEnum.
 */
public enum OperationStatusEnum {
    E("error", "报错"),
    W("warn", "警告"),
    WS("waitingShift", "等待翻牌"),
    I("idle", "空闲"),
    P("pending", "待执行"),
    D("dependent", "依赖未满足"),
    R("running", "执行中"),
    RT("retrying", "失败重试中"),
    RR("rerunning", "重跑中"),
    RS("restarting", "重启中(处于中断中的状态)"),
    RE("runningWithError", "执行中(报错)"),
    RW("runningWithWarn", "执行中(警告)"),
    RC("runningWithCanceled", "执行中(中断)"),
    CL("canceling", "中断中"),
    C("canceled", "已中断"),
    S("success", "成功"),
    SI("successWithIgnore", "成功(忽略)");

    private final String code;

    private final String desc;

    OperationStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private String getDescByCode(String code) {
        String desc = "";
        for (OperationStatusEnum o : OperationStatusEnum.values()) {
            if (o.toString().equals(code)) {
                return o.getDesc();
            }
        }
        return null;
    }

    /**
     * getCode.
     *
     * @return code
     */
    public String getCode() {
        return code;
    }

    /**
     * getDesc.
     *
     * @return desc
     */
    public String getDesc() {
        return desc;
    }
}

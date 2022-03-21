package com.youngdatafan.dataintegration.core.util;

/**
 * 返回状态码枚举对象.
 *
 * @author gavin
 * @since 2020/5/21 10:15 上午
 */

public enum StatusCode {

    CODE_10000("10000", "接口调用成功，调用结果请参考具体的API文档所对应的业务返回参数"), CODE_10001("10001", "服务不可用"), CODE_10002("10002", "内部服务不可用（feign相互调用）"), CODE_10003("10003", "无效的请求报文"), CODE_10004("10004", "表单校验错误"),
    CODE_10005("10005", "token会话已超时"), CODE_10006("10006", "token权限认证失败，没有访问权限"), CODE_10007("10007", "用户在别的地方登陆"), CODE_10008("10008", "访问过快，请稍候再试"), CODE_10009("10009", "重复的请求"),
    CODE_10010("10010", "业务请求失败（通用的异常提示状态码）"),
    CODE_99999("99999", "其它业务异常");

    /**
     * 返回状态码.
     */
    private final String code;

    /**
     * 返回提示消息.
     */
    private final String msg;

    StatusCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * get statusCode.
     *
     * @return StatusCode
     */
    public String getCode() {
        return code;
    }

    /**
     * get msg.
     *
     * @return string msg
     */
    public String getMsg() {
        return msg;
    }
}

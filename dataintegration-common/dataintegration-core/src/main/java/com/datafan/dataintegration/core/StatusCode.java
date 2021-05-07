package com.datafan.dataintegration.core;

/**
 * 返回状态码
 * <p>
 * <p>
 * 已过期，请使用 {@link com.datafan.dataintegration.core.util.StatusCode}
 *
 * @author renhua.zhang
 * @create 2020-01-09 15:11:48
 */
@Deprecated
public interface StatusCode {

    /**
     * 接口调用成功，调用结果请参考具体的API文档所对应的业务返回参数
     */
    String CODE_10000 = "10000";

    /**
     * 服务不可用
     */
    String CODE_10001 = "10001";

    /**
     * 内部服务不可用（feign相互调用）
     */
    String CODE_10002 = "10002";

    /**
     * 无效的请求报文
     */
    String CODE_10003 = "10003";

    /**
     * 表单校验错误
     */
    String CODE_10004 = "10004";

    /**
     * token会话已超时
     */
    String CODE_10005 = "10005";

    /**
     * token权限认证失败，没有访问权限
     */
    String CODE_10006 = "10006";

    /**
     * 用户在别的地方登陆
     */
    String CODE_10007 = "10007";

    /**
     * 业务请求失败（通用的异常提示状态码）
     */
    String CODE_10010 = "10010";

    /**
     * 未知错误
     */
    String CODE_99999 = "99999";

}

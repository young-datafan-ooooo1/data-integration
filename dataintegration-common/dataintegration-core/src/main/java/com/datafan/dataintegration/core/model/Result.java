package com.datafan.dataintegration.core.model;

import com.datafan.dataintegration.core.StatusCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;

/**
 * 接口数据响应对象
 *
 * @author renhua.zhang
 * @create 2017-9-19 16:13
 */
@ApiModel("接口响应对象")
public class Result<T, A> implements Serializable {

    @ApiModelProperty("返回码")
    private String code;

    @ApiModelProperty("返回描述信息")
    private String msg;

    @ApiModelProperty("返回内容的签名数据")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String sign;

    @ApiModelProperty("返回内容")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T content;

    @ApiModelProperty("附件（满足不同场景需求返回数据）")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private A attachment;

    public Result() {
    }

    public Result(String code) {
        this.code = code;
    }

    public Result(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Result(String code, String msg, T content) {
        this.code = code;
        this.msg = msg;
        this.content = content;
    }

    /**
     * 成功
     *
     * @param content 返回内容对象
     * @return 返回PrimeResponse对象
     */
    public static <T> Result<T, Object> success(T content) {
        Result<T, Object> result = new Result<>();
        result.setContent(content);
        result.setCode(StatusCode.CODE_10000);

        return result;
    }

    /**
     * 成功
     *
     * @param content 返回内容对象
     * @param message 提示信息
     * @return 返回PrimeResponse对象
     */
    public static <T> Result<T, Object> success(T content, String message) {
        Result<T, Object> result = new Result<>();
        result.setContent(content);
        result.setMsg(message);
        result.setCode(StatusCode.CODE_10000);
        return result;
    }

    /**
     * 失败
     *
     * @param code    状态码
     * @param content 返回内容对象
     * @param message 提示信息
     * @return 返回PrimeResponse对象
     */
    public static <T> Result fail(String code, T content, String message) {
        Result<T, Object> result = new Result<>();
        result.setContent(content);
        result.setMsg(message);
        result.setCode(code);
        return result;
    }

    /**
     * 失败
     *
     * @param code    状态码
     * @param message 提示信息
     * @return 返回PrimeResponse对象
     */
    public static <T> Result fail(String code, String message) {
        Result<T, Object> result = new Result<>();
        result.setMsg(message);
        result.setCode(code);
        return result;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public A getAttachment() {
        return attachment;
    }

    public void setAttachment(A attachment) {
        this.attachment = attachment;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

}

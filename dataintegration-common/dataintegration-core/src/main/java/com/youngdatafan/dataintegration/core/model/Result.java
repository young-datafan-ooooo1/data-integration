package com.youngdatafan.dataintegration.core.model;

import com.youngdatafan.dataintegration.core.util.StatusCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * 接口数据响应对象.
 *
 * @author renhua.zhang
 * @since 2017-9-19 16:13
 */
@Setter
@Getter
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

    public Result(final String code) {
        this.code = code;
    }

    public Result(final String code, final String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Result(final String code, final String msg, final T content) {
        this.code = code;
        this.msg = msg;
        this.content = content;
    }

    /**
     * 成功.
     *
     * @param content 返回内容对象
     * @param <T>     content type
     * @return 返回PrimeResponse对象
     */
    public static <T> Result<T, Object> success(final T content) {
        Result<T, Object> result = new Result<>();
        result.setContent(content);
        result.setCode(StatusCode.CODE_10000.getCode());

        return result;
    }

    /**
     * 成功.
     *
     * @param content 返回内容对象
     * @param message 提示信息
     * @param <T>     content type
     * @return 返回PrimeResponse对象
     */
    public static <T> Result<T, Object> success(final T content, final String message) {
        Result<T, Object> result = new Result<>();
        result.setContent(content);
        result.setMsg(message);
        result.setCode(StatusCode.CODE_10000.getCode());
        return result;
    }

    /**
     * 失败.
     *
     * @param code    状态码
     * @param content 返回内容对象
     * @param message 提示信息
     * @param <T>     content type
     * @return 返回PrimeResponse对象
     */
    public static <T> Result fail(final String code, final T content, final String message) {
        Result<T, Object> result = new Result<>();
        result.setContent(content);
        result.setMsg(message);
        result.setCode(code);
        return result;
    }

    /**
     * 失败.
     *
     * @param code    状态码
     * @param content 返回内容对象
     * @param message 提示信息
     * @param <T>     content type
     * @return 返回PrimeResponse对象
     */
    public static <T> Result fail(final StatusCode code, final T content, final String message) {
        Result<T, Object> result = new Result<>();
        result.setContent(content);
        result.setMsg(message);
        result.setCode(code.getCode());
        return result;
    }

    /**
     * 失败.
     *
     * @param code    状态码
     * @param message 提示信息
     * @param <T>     content type
     * @return 返回PrimeResponse对象
     */
    public static <T> Result fail(final String code, final String message) {
        Result<T, Object> result = new Result<>();
        result.setMsg(message);
        result.setCode(code);
        return result;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

}

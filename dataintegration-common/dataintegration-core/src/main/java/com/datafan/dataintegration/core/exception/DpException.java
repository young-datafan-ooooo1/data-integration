package com.datafan.dataintegration.core.exception;

import org.springframework.http.HttpStatus;

/**
 * 系统异常
 *
 * @author renhua.zhang
 * @create 2017-10-27 16:13
 */
public class DpException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 状态代码
     */
    private String statusCode;

    /**
     * 提示信息
     */
    private String message;

    /**
     * 响应状态码
     */
    private HttpStatus httpStatus = HttpStatus.OK;

    public DpException(String statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public DpException(String statusCode, String message, HttpStatus httpStatus) {
        this.statusCode = statusCode;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public DpException(String statusCode, String message, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
        this.message = message;
    }


    public DpException() {

    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}

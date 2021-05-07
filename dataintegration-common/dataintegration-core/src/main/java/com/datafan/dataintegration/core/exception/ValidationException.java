package com.datafan.dataintegration.core.exception;

import org.springframework.http.HttpStatus;

/**
 * 参数以及数据格式校验异常
 *
 * @author renhua.zhang
 * @create 2017-10-27 16:13
 */
public class ValidationException extends DpException {

    public ValidationException(String statusCode, String message) {
        super(statusCode, message);
    }

    public ValidationException(String statusCode, String message, HttpStatus httpStatus) {
        super(statusCode, message, httpStatus);
    }

    public ValidationException(String statusCode, String message, Throwable cause) {
        super(statusCode, message, cause);
    }
}

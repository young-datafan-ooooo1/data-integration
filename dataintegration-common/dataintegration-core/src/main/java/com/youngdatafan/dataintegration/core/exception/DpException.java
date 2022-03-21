package com.youngdatafan.dataintegration.core.exception;

import com.youngdatafan.dataintegration.core.util.StatusCode;
import org.springframework.http.HttpStatus;

/**
 * 系统异常.
 *
 * @author renhua.zhang
 * @since 2017-10-27 16:13
 */
public class DpException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 状态代码.
     */
    private String statusCode;

    /**
     * 提示信息.
     */
    private String message;

    /**
     * 响应状态码.
     */
    private HttpStatus httpStatus = HttpStatus.OK;

    public DpException(final StatusCode statusCode, final String message) {
        this.statusCode = statusCode.getCode();
        this.message = message;
    }

    public DpException(final String statusCode, final String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public DpException(final String statusCode, final String message, final HttpStatus httpStatus) {
        this.statusCode = statusCode;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public DpException(final String statusCode, final String message, final Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
        this.message = message;
    }

    public DpException(final StatusCode statusCode, final String message, final Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode.getCode();
        this.message = message;
    }

    /**
     * get statusCode.
     *
     * @return statusCode
     */
    public String getStatusCode() {
        return statusCode;
    }

    /**
     * set statusCode.
     *
     * @param statusCode statusCode
     */
    public void setStatusCode(final String statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

    /**
     * set message.
     *
     * @param message string message
     */
    public void setMessage(final String message) {
        this.message = message;
    }

    /**
     * get httpStatus.
     *
     * @return httpStatus
     */
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    /**
     * set httpStatus.
     *
     * @param httpStatus httpStatus
     */
    public void setHttpStatus(final HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}

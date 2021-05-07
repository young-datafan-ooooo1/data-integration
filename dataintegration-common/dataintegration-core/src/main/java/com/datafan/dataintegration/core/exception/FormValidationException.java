package com.datafan.dataintegration.core.exception;

import java.util.Map;

/**
 * 表单参数以及数据格式校验异常
 *
 * @author renhua.zhang
 * @create 2017-10-27 16:13
 */
public class FormValidationException extends ValidationException {

    /**
     * 字段名
     */
    private String fieldName;

    /**
     * 附件，表单验证错误提示信息
     */
    private Map<String, Object> attachment;

    public FormValidationException(String statusCode, String fieldName, String message) {
        super(statusCode, message);
        this.fieldName = fieldName;
    }

    public FormValidationException(String statusCode, String message, Map<String, Object> attachment) {
        super(statusCode, message);
        this.attachment = attachment;
    }

    public FormValidationException(String statusCode, String fieldName, String message, Map<String, Object> attachment) {
        super(statusCode, message);
        this.fieldName = fieldName;
        this.attachment = attachment;
    }

    public FormValidationException(String statusCode, String fieldName, String message, Throwable cause, Map<String, Object> attachment) {
        super(statusCode, message, cause);
        this.fieldName = fieldName;
        this.attachment = attachment;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Map<String, Object> getAttachment() {
        return attachment;
    }

    public void setAttachment(Map<String, Object> attachment) {
        this.attachment = attachment;
    }
}

package com.youngdatafan.dataintegration.core.exception;

import java.util.Map;

/**
 * 表单参数以及数据格式校验异常.
 *
 * @author renhua.zhang
 * @since 2017-10-27 16:13
 */
public class FormValidationException extends ValidationException {

    /**
     * 字段名.
     */
    private String fieldName;

    /**
     * 附件，表单验证错误提示信息.
     */
    private Map<String, Object> attachment;

    public FormValidationException(final String statusCode, final String fieldName, final String message) {
        super(statusCode, message);
        this.fieldName = fieldName;
    }

    public FormValidationException(final String statusCode, final String message, final Map<String, Object> attachment) {
        super(statusCode, message);
        this.attachment = attachment;
    }

    public FormValidationException(final String statusCode, final String fieldName, final String message, final Map<String, Object> attachment) {
        super(statusCode, message);
        this.fieldName = fieldName;
        this.attachment = attachment;
    }

    public FormValidationException(final String statusCode, final String fieldName, final String message, final Throwable cause, final Map<String, Object> attachment) {
        super(statusCode, message, cause);
        this.fieldName = fieldName;
        this.attachment = attachment;
    }

    /**
     * get fieldName.
     *
     * @return fieldName
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * set fieldName.
     *
     * @param fieldName the fieldName
     */
    public void setFieldName(final String fieldName) {
        this.fieldName = fieldName;
    }

    /**
     * get attachment.
     *
     * @return attachment
     */
    public Map<String, Object> getAttachment() {
        return attachment;
    }

    /**
     * set attachment.
     *
     * @param attachment attachment
     */
    public void setAttachment(final Map<String, Object> attachment) {
        this.attachment = attachment;
    }
}

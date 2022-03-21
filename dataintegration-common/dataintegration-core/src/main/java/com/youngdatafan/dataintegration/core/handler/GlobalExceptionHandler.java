package com.youngdatafan.dataintegration.core.handler;

import com.youngdatafan.dataintegration.core.exception.DpException;
import com.youngdatafan.dataintegration.core.exception.FormValidationException;
import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.dataintegration.core.util.StatusCode;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 全局异常处理.
 *
 * @author gavin
 * @since 2020/2/7 4:18 下午
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 根据异常构造响应内容.
     *
     * @param t 异常对象
     * @return result
     * @throws Throwable 异常对象
     */
    public static Result<Object, Map<String, Object>> buildResult(final Throwable t) throws Throwable {
        Result<Object, Map<String, Object>> result = new Result<>();
        Throwable ex;
        if (t.getCause() != null) {
            ex = t.getCause();
        } else {
            ex = t;
        }

        if (t instanceof HttpMessageNotReadableException) {
            result.setCode(StatusCode.CODE_10003.getCode());
            result.setMsg(t.getMessage());

        } else if (t instanceof ServletException) {
            result.setCode(StatusCode.CODE_10003.getCode());
            result.setMsg(t.getMessage());

        } else if (t instanceof IllegalArgumentException) {
            result.setCode(StatusCode.CODE_10010.getCode());
            result.setMsg(t.getMessage());

        } else if (ex instanceof FormValidationException) {
            // 表单校验异常处理
            result.setCode(StatusCode.CODE_10004.getCode());
            FormValidationException e = (FormValidationException) ex;
            Map<String, Object> formAttachment = getFormAttachment(e);
            result.setAttachment(formAttachment);

        } else if (ex instanceof DpException) {
            // 平台异常标准处理
            DpException e = (DpException) ex;
            result.setCode(e.getStatusCode());
            result.setMsg(e.getMessage());

        } else if (ex instanceof MethodArgumentNotValidException) {
            // spring 表单校验异常处理
            Map<String, Object> attachment = getValidExceptionAttachment((MethodArgumentNotValidException) ex);
            result.setCode(StatusCode.CODE_10004.getCode());
            result.setAttachment(attachment);

        } else {
            // 其它异常抛出去交给网关处理
            throw t;
        }
        return result;
    }

    /**
     * 获取表单校验异常附件信息.
     *
     * @param ex MethodArgumentNotValidException
     * @return java.util.HashMap
     */
    private static Map<String, Object> getValidExceptionAttachment(final MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        List<ObjectError> errors = bindingResult.getAllErrors();
        if (CollectionUtils.isEmpty(errors)) {
            return null;
        }

        // 封装返回附件内容
        Map<String, Object> attachment = new HashMap<>(errors.size());
        for (ObjectError objectError : errors) {
            FieldError fieldError = (FieldError) objectError;
            attachment.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return attachment;
    }

    /**
     * 获取表单附件.
     *
     * @param e FormValidationException
     * @return map
     */
    private static Map<String, Object> getFormAttachment(final FormValidationException e) {
        if (MapUtils.isNotEmpty(e.getAttachment())) {
            return e.getAttachment();

        } else if (e.getFieldName() != null) {
            Map<String, Object> attachment = new HashMap<>(1);
            attachment.put(e.getFieldName(), e.getMessage());
            return attachment;
        }

        return null;
    }

    /**
     * 所有异常处理.
     *
     * @param response HttpServletResponse
     * @param ex       异常对象
     * @return result
     */
    @ExceptionHandler(value = Throwable.class)
    @ResponseBody
    public Result allExceptionHandler(final HttpServletResponse response, final Throwable ex) {
        LOG.error(ex.getMessage(), ex);

        Result result;
        try {
            // 根据异常构造响应内容
            result = buildResult(ex);

            if (ex instanceof DpException) {
                // dp异常自定义状态码
                response.setStatus(((DpException) ex).getHttpStatus().value());
            }

        } catch (Throwable t) {
            // 默认失败处理
            result = Result.fail(StatusCode.CODE_10010.getCode(), null, "业务请求失败。");
        }

        return result;
    }
}

package com.youngdatafan.dataintegration.file.management.utils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.web.util.HtmlUtils;
import org.springframework.web.util.JavaScriptUtils;

import java.beans.PropertyEditorSupport;

/**
 * 字符串转义工具类.
 *
 * @author liuhao
 * @since 10:47
 */

@ApiModel("字符串转义工具类")
@AllArgsConstructor
@NoArgsConstructor
public class StringEscapeEditor extends PropertyEditorSupport {

    /**
     * 编码html.
     */
    @ApiModelProperty("编码html")
    private boolean escapeHTML;

    /**
     * 编码js.
     */
    @ApiModelProperty("编码js")
    private boolean escapeJavaScript;


    /**
     * 获取字符串文本.
     * @return 文本数据
     */
    @Override
    public String getAsText() {
        Object value = getValue();
        return value != null ? value.toString() : "";
    }

    /**
     * 设置文本数据.
     * @param text 字符串文本.
     * @throws IllegalArgumentException 非法参数异常.
     */
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (text == null) {
            setValue(null);
        } else {
            String value = text;
            if (escapeHTML) {
                value = HtmlUtils.htmlEscape(value);
            }
            if (escapeJavaScript) {
                value = JavaScriptUtils.javaScriptEscape(value);
            }
            setValue(value);
        }
    }

}

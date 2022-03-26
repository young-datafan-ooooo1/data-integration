package com.youngdatafan.portal.system.management.common.utils;

import java.beans.PropertyEditorSupport;
import org.springframework.web.util.HtmlUtils;
import org.springframework.web.util.JavaScriptUtils;

/**
 * StringEscapeEditor.
 */
public class StringEscapeEditor extends PropertyEditorSupport {
    // 编码HTML
    private boolean escapeHTML;

    // 编码javascript
    private boolean escapeJavaScript;

    public StringEscapeEditor() {
    }

    public StringEscapeEditor(boolean escapeHTML, boolean escapeJavaScript) {
        this.escapeHTML = escapeHTML;
        this.escapeJavaScript = escapeJavaScript;
    }

    @Override
    public String getAsText() {
        Object value = getValue();
        return value != null ? value.toString() : "";
    }

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

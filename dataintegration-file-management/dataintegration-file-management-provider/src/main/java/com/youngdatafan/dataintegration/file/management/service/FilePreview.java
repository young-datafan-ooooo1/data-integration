package com.youngdatafan.dataintegration.file.management.service;

import java.io.InputStream;
import javax.servlet.http.HttpServletResponse;

/**
 * 文件预览service.
 *
 * @author songxiaolang
 */
public interface FilePreview {

    /**
     * 文件预览将流转成html.
     *
     * @param type        文件类型
     * @param inputStream inputStream
     * @param response    response
     */
    void filePreviewHandle(InputStream inputStream, HttpServletResponse response, String type);
}

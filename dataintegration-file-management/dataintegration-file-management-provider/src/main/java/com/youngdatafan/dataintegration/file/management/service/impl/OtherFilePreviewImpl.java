package com.youngdatafan.dataintegration.file.management.service.impl;

import com.youngdatafan.dataintegration.core.exception.DpException;
import com.youngdatafan.dataintegration.core.util.StatusCode;
import com.youngdatafan.dataintegration.file.management.service.FilePreview;
import java.io.InputStream;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

/**
 * OtherFilePreviewImpl.
 *
 * @author songxiaolang
 * @since 2021-11-24 11:02
 */
@Service
public class OtherFilePreviewImpl implements FilePreview {

    @Override
    public void filePreviewHandle(InputStream inputStream, HttpServletResponse response, String type) {
        throw new DpException(StatusCode.CODE_10010, type + "：类型文件预览尚未开发！");
    }
}

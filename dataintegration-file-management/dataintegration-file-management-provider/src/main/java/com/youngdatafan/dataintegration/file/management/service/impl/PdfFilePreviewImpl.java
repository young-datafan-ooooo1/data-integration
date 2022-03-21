package com.youngdatafan.dataintegration.file.management.service.impl;

import com.youngdatafan.dataintegration.file.management.service.FilePreview;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

/**
 * pdfFilePreviewImpl.
 *
 * @author songxiaolang
 * @since 2021-11-24 11:02
 */
@Service
public class PdfFilePreviewImpl implements FilePreview {

    @Override
    public void filePreviewHandle(InputStream inputStream, HttpServletResponse response, String type) {

        try {
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("application/pdf");
            IOUtils.copy(inputStream, outputStream);
            inputStream.close();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package com.youngdatafan.dataintegration.file.management.service.impl;

import com.youngdatafan.dataintegration.file.management.service.FilePreview;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.jodconverter.DocumentConverter;
import org.jodconverter.job.ConversionJobWithOptionalSourceFormatUnspecified;
import org.jodconverter.office.OfficeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * OfficeFilePreviewImpl.
 *
 * @author songxiaolang
 * @since 2021-11-24 11:02
 */
@Service
public class OfficeFilePreviewImpl implements FilePreview {

    @Resource
    private DocumentConverter converter;

    @Value("${jodconverter.local.office-home}")
    private String pdfPath;

    @Override
    public void filePreviewHandle(InputStream inputStream, HttpServletResponse response, String type) {
        //pdf生成目录
        File pdfFile = new File(String.format("%s/%d.pdf", pdfPath, System.currentTimeMillis()));
        //预览的文件
        File outputFile = new File(pdfPath + "/" + System.currentTimeMillis() + "." + type);
        try (OutputStream os = new FileOutputStream(outputFile)) {
            int bytesRead;
            byte[] buffer = new byte[1024 * 8];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //文件转化
        try {
            ConversionJobWithOptionalSourceFormatUnspecified convert = converter.convert(outputFile);
            convert.to(pdfFile).execute();
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("application/pdf");
            InputStream in = new FileInputStream(pdfFile);
            IOUtils.copy(in, outputStream);
            in.close();
            outputStream.close();
        } catch (OfficeException | IOException e) {
            e.printStackTrace();
        } finally {
            pdfFile.delete();
            outputFile.delete();
        }

    }
}

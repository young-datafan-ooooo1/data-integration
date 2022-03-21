package com.youngdatafan.dataintegration.file.management.service;

import com.youngdatafan.dataintegration.core.exception.DpException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * 文件操作模型服务.
 *
 * @Author: jeremychen
 * @Descripition:
 * @Date 2020/4/7 4:47 下午
 */
public interface FileOperationToModelService {


    /**
     * 添加文件.
     *
     * @param relativePath 文件路径
     * @param inputStream  文件内容
     * @throws DpException 文件异常
     */
    void addFile(String relativePath, InputStream inputStream) throws DpException;


    /**
     * 添加文件夹.
     *
     * @param relativePath 文件路径
     * @param files        文件清单
     * @throws IOException 文件io异常
     */
    void addFolder(String relativePath, MultipartFile[] files) throws IOException;


    /**
     * 获取文件根路径.
     * @return 文件根路径
     */
    String getRootPath();
}

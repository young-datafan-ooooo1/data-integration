package com.youngdatafan.dataintegration.file.management.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.youngdatafan.dataintegration.file.management.dto.FileSummary;
import java.io.InputStream;
import java.util.function.Function;
import com.youngdatafan.dataintegration.core.exception.DpException;

/**
 * 文件系统管理服务.
 *
 * @Author: jeremychen
 * @Descripition:
 * @Date 2020/4/7 4:47 下午
 */
public interface FileSystemManagerService {
    /**
     * 测试ftp服务.
     */
    void testFtp();

    /**
     * 添加文件.
     *
     * @param relativePath 文件名称
     * @param inputStream  文件内容
     * @param size         文件长度
     * @throws DpException 异常
     */
    void addFile(String relativePath, InputStream inputStream, long size) throws DpException;

    /**
     * 添加文件夹.
     *
     * @param parentFolder 父文件夹
     * @throws DpException shanjing自定义异常
     */
    void addFolder(String parentFolder) throws DpException;

    /**
     * 删除文件.
     *
     * @param filePath 文件路径
     * @throws DpException shanjing自定义异常
     */
    void delFile(String filePath) throws DpException;

    /**
     * 删除文件夹.
     *
     * @param filePath 文件路径
     * @throws DpException shanjing自定义异常
     */
    void delFolder(String filePath) throws DpException;

    /**
     * 循环文件夹.
     *
     * @param filePath 文件路径
     * @param function 功能函数
     */
    void loopFolder(String filePath, Function<FileSummary, String> function);

    /**
     * 获取文件最后一次更新的时间.
     *
     * @param filePath 文件路径.
     * @return 返回最后一次更新时间的时间戳.
     */
    long getLastModifyFileTime(String filePath);

    /**
     * 获取文件基础名称.
     *
     * @param fileKey 文件key
     * @return 文件名称
     */
    String getBaseName(String fileKey);

    /**
     * 获取文件对象.
     *
     * @param relativePath 文件相对路径
     * @return S3对象
     * @throws AmazonServiceException 亚马逊服务异常
     */
    InputStream getFileObject(String relativePath) throws AmazonServiceException;

    /**
     * 判断文件是否存在.
     *
     * @param filePath 文件路径
     * @return true 或 false
     */
    boolean exists(String filePath);

    /**
     * 获取用户名.
     *
     * @return 用户名.
     */
    String getUserName();

    /**
     * 获取密码.
     *
     * @return 密码.
     */
    String getPassword();

    /**
     * 获取根路径.
     *
     * @return 根路径.
     */
    String getRootPath();

}
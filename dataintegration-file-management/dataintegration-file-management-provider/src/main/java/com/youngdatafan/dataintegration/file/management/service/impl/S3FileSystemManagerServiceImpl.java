package com.youngdatafan.dataintegration.file.management.service.impl;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import com.youngdatafan.dataintegration.core.exception.DpException;
import com.youngdatafan.dataintegration.file.management.config.FileServerProperties;
import com.youngdatafan.dataintegration.file.management.config.S3Properties;
import com.youngdatafan.dataintegration.file.management.service.FileSystemManagerService;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * s3文件系统管理服务.
 *
 * @Author: jeremychen
 * @Descripition:
 * @Date 2020/4/7 4:48 下午
 */

@Service
public class S3FileSystemManagerServiceImpl implements FileSystemManagerService {

    private final Logger logger = LoggerFactory.getLogger(S3FileSystemManagerServiceImpl.class);

    private final FileServerProperties fileServerProperties;

    private final Pattern filePathCompile = Pattern.compile("(\\w+)://([\\d\\.\\:]+)/(.+)");

    private AmazonS3 s3Client;

    private S3Properties s3Properties;

    @Autowired
    public S3FileSystemManagerServiceImpl(FileServerProperties fileServerProperties) {
        this.fileServerProperties = fileServerProperties;
    }

    /**
     * 初始化方法.
     */
    @PostConstruct
    public void init() {
        s3Properties = fileServerProperties.getS3();

        AWSCredentials credentials = new BasicAWSCredentials(s3Properties.getUserName(), s3Properties.getPassword());
        ClientConfiguration clientConfig = new ClientConfiguration();
        clientConfig.setProtocol(Protocol.HTTP);
        // 120分钟
        clientConfig.setSocketTimeout(1000 * 60 * 120);

        s3Client = new AmazonS3Client(credentials, clientConfig);
        s3Client.setEndpoint(s3Properties.getServer() + ":" + s3Properties.getPort());
    }

    /**
     * 对象销毁时执行的方法.
     */
    @PreDestroy
    public void destroy() {
        if (s3Client != null) {
            s3Client.shutdown();
        }
    }

    @Override
    public void testFtp() {
        logger.info("Connection successfully established to ");
    }

    /**
     * 获取文件key.
     *
     * @param path 文件全路径
     * @return s3文件key
     */
    private String getFileKey(String path) {
        String finalPath = path;
        if (finalPath.startsWith("/")) {
            finalPath = finalPath.substring(1);
        }

        final Matcher matcher = filePathCompile.matcher(finalPath);
        if (matcher.find()) {
            finalPath = matcher.group(3);
        }

        if (finalPath.startsWith(s3Properties.getBucket())) {
            finalPath = finalPath.substring(s3Properties.getBucket().length() + 1);
        }

        return finalPath;
    }

    @Override
    public void addFile(String relativePath, InputStream inputStream, long size) throws DpException {
        if (size <= 1024 * 1024 * 500) {
            // 500m以内直接普通上传
            putObject(relativePath, inputStream, size);

        } else {
            TransferManager tm = new TransferManager(s3Client);
            try {
                String finalRelativePath = getFileKey(relativePath);
                final ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentLength(size);

                // 流式上传
                final Upload upload = tm.upload(s3Properties.getBucket(), finalRelativePath, inputStream, metadata);

                upload.waitForCompletion();

            } catch (InterruptedException e) {
                logger.error("upload.waitForCompletion error.", e);
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                tm.shutdownNow(false);
            }
        }

    }

    private void putObject(String relativePath, InputStream inputStream, long size) {
        try {
            String finalRelativePath = getFileKey(relativePath);
            final ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(size);
            s3Client.putObject(s3Properties.getBucket(), finalRelativePath, inputStream, metadata);

        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void addFolder(String parentFolder) throws DpException {
        s3Client.putObject(s3Properties.getBucket(), parentFolder, "");
    }

    /**
     * 删除文件路径.
     *
     * @param filePath 文件路径
     * @throws DpException Stella异常.
     */
    @Override
    public void delFile(String filePath) throws DpException {
        String delFilePath = getFileKey(filePath);
        s3Client.deleteObject(s3Properties.getBucket(), delFilePath);
    }

    /**
     * 删除文件夹.
     *
     * @param filePath 文件路径
     * @throws DpException Stella自定义异常处理
     */
    @Override
    public void delFolder(String filePath) throws DpException {
        String deleteFilePath = getFileKey(filePath);
        ObjectListing objectListing = s3Client.listObjects(s3Properties.getBucket(), deleteFilePath);
        do {
            for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
                s3Client.deleteObject(s3Properties.getBucket(), objectSummary.getKey());
            }
            objectListing = s3Client.listNextBatchOfObjects(objectListing);
        } while (objectListing.isTruncated());
    }


    /**
     * 循环文件.
     *
     * @param filePath 文件路径
     */
    @Override
    public void loopFolder(String filePath, Function<S3ObjectSummary, String> function) {
        String loopFilePath = getFileKey(filePath);
        ObjectListing objectListing = s3Client.listObjects(s3Properties.getBucket(), loopFilePath);
        do {
            for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
                function.apply(objectSummary);
            }
            objectListing = s3Client.listNextBatchOfObjects(objectListing);
        } while (objectListing.isTruncated());
    }

    @Override
    public long getLastModifyFileTime(String filePath) {
        String modifyFilePath = getFileKey(filePath);
        ObjectListing objectListing = s3Client.listObjects(s3Properties.getBucket(), modifyFilePath);

        long lastModifiedTime = 0;
        do {

            for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
                final long time = objectSummary.getLastModified().getTime();
                if (lastModifiedTime == 0) {
                    lastModifiedTime = time;
                } else if (lastModifiedTime < time) {
                    lastModifiedTime = time;
                }
            }
            objectListing = s3Client.listNextBatchOfObjects(objectListing);
        } while (objectListing.isTruncated());

        return lastModifiedTime;
    }

    @Override
    public String getBaseName(String fileKey) {
        if (fileKey == null) {
            return null;
        }

        final int i = fileKey.lastIndexOf("/");
        if (i > 0) {
            return fileKey.substring(i + 1);
        }
        return fileKey;
    }

    /**
     * 文件下载.
     *
     * @param relativePath 文件路径
     * @return S3Object
     */
    @Override
    public S3Object getFileObject(String relativePath) throws AmazonServiceException {
        String finalRelativePath = getFileKey(relativePath);
        S3Object object = null;
        try {
            object = s3Client.getObject(s3Properties.getBucket(), finalRelativePath);
        } catch (Exception e) {
            logger.error("s3 下载失败", e);
        }
        return object;
    }

    @Override
    public boolean exists(String filePath) {
        String isFilePath = getFileKey(filePath);
        return s3Client.doesObjectExist(s3Properties.getBucket(), isFilePath);
    }

    @Override
    public String getUserName() {
        return s3Properties.getUserName();
    }

    @Override
    public String getPassword() {
        return s3Properties.getPassword();
    }

    @Override
    public String getRootPath() {
        return s3Properties.getRootPath() + s3Properties.getServer() + ":" + s3Properties.getPort() + "/" + s3Properties.getBucket() + "/";
    }
}

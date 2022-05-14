package com.youngdatafan.dataintegration.file.management.service.impl;

import com.amazonaws.AmazonServiceException;
import com.youngdatafan.dataintegration.core.exception.DpException;
import com.youngdatafan.dataintegration.core.util.StatusCode;
import com.youngdatafan.dataintegration.file.management.config.FileServerProperties;
import com.youngdatafan.dataintegration.file.management.config.MinIoProperties;
import com.youngdatafan.dataintegration.file.management.dto.FileSummary;
import com.youngdatafan.dataintegration.file.management.service.FileSystemManagerService;
import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.ListObjectsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.Result;
import io.minio.StatObjectArgs;
import io.minio.StatObjectResponse;
import io.minio.messages.Item;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * MinIo.
 *
 * @author songxiaolang
 * @since 2022-03-29 14:54
 */
@Slf4j
@Service
@ConditionalOnProperty(prefix = "file.server", name = "useServer", havingValue = "minio")
public class MinIoFileSystemManagerServiceImpl implements FileSystemManagerService {

    private static MinioClient minioClient;

    @Value("${minio.upload.contentType:application/octet-stream}")
    private String contentType;

    private final Pattern filePathCompile = Pattern.compile("(\\w+)://([\\d\\.\\:]+)/(.+)");

    @Autowired
    private FileServerProperties fileServerProperties;

    private MinIoProperties minIoProperties;

    /**
     * 初始化minio配置.
     */
    @PostConstruct
    public void init() {
        minIoProperties = fileServerProperties.getMinIos3();
        try {
            minioClient =
                MinioClient.builder()
                    .endpoint(minIoProperties.getUrl())
                    .credentials(minIoProperties.getAccessKey(), minIoProperties.getSecretKey())
                    .build();

            createBucket(minIoProperties.getBucketName());
        } catch (Exception e) {
            throw new DpException(StatusCode.CODE_10010, "初始化minIo配置异常", e);
        }
    }

    /**
     * 创建 bucket.
     *
     * @param bucketName bucketName.
     */
    public static void createBucket(String bucketName) {
        boolean found;
        try {
            found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        } catch (Exception e) {
            throw new DpException(StatusCode.CODE_10010, "bucketExists error", e);

        }
        if (!found) {
            try {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            } catch (Exception e) {
                throw new DpException(StatusCode.CODE_10010, "makeBucket error", e);
            }
        }
    }

    @Override
    public void testFtp() {
        log.info("Connection successfully established to ");
    }

    @Override
    public void addFile(String relativePath, InputStream inputStream, long size) throws DpException {
        if (inputStream == null) {
            throw new DpException(StatusCode.CODE_10010, "文件流不能为空");
        }
        String finalRelativePath = getFileKey(relativePath);
        //开始上传
        try {
            minioClient.putObject(
                PutObjectArgs.builder().bucket(minIoProperties.getBucketName()).object(finalRelativePath).contentType(contentType)
                    .stream(inputStream, inputStream.available(), -1)
                    .build());
        } catch (Exception e) {
            throw new DpException(StatusCode.CODE_10010, "文件上传失败", e);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }

    }

    @Override
    public void addFolder(String parentFolder) throws DpException {
        String lastFolder = parentFolder + "/";
        try {
            minioClient.putObject(
                PutObjectArgs.builder().bucket(minIoProperties.getBucketName()).object(lastFolder).stream(
                    new ByteArrayInputStream(new byte[] {}), 0, -1)
                    .build());
        } catch (Exception e) {
            throw new DpException(StatusCode.CODE_10010, "MinIo创建文件夹失败", e);
        }
    }

    @Override
    public void delFile(String filePath) throws DpException {
        String deleteFilePath = getFileKey(filePath);
        try {
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(minIoProperties.getBucketName()).object(deleteFilePath).build());
        } catch (Exception e) {
            throw new DpException(StatusCode.CODE_10010, "MinIo删除文件失败", e);
        }
    }

    @Override
    public void delFolder(String filePath) throws DpException {
        String loopFilePath = getFileKey(filePath);
        try {
            if (StringUtils.isNotBlank(filePath)) {
                Iterable<Result<Item>> results = minioClient.listObjects(
                    ListObjectsArgs.builder().bucket(minIoProperties.getBucketName()).prefix(loopFilePath).build());
                results.forEach(e -> {
                    try {
                        minioClient.removeObject(
                            RemoveObjectArgs.builder()
                                .bucket(minIoProperties.getBucketName())
                                .object(e.get().objectName())
                                .build());
                    } catch (Exception exception) {
                        throw new DpException(StatusCode.CODE_10010, "MinIo删除文件失败", exception);
                    }
                });

            }
        } catch (Exception e) {
            throw new DpException(StatusCode.CODE_10010, "MinIo删除文件失败", e);

        }
    }

    @Override
    public void loopFolder(String filePath, Function<FileSummary, String> function) {
        String lastFilePath = getFileKey(filePath);
        try {
            Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder().bucket(minIoProperties.getBucketName())
                    .prefix(lastFilePath).build());
            for (Result<Item> result : results) {
                FileSummary fileSummary = new FileSummary();
                fileSummary.setKey(result.get().objectName());
                fileSummary.setSize(result.get().size());
                ZonedDateTime zonedDateTime = result.get().lastModified();
                fileSummary.setLastModified(Date.from(zonedDateTime.toInstant()));
                function.apply(fileSummary);
            }
        } catch (Exception e) {
            throw new DpException(StatusCode.CODE_10010, "循环文件夹异常", e);
        }

    }

    @Override
    public long getLastModifyFileTime(String filePath) {
        String lastFilePath = getFileKey(filePath);
        long lastModifiedTime = 0;
        try {
            StatObjectResponse statObjectResponse = minioClient.statObject(
                StatObjectArgs.builder().bucket(minIoProperties.getBucketName()).object(lastFilePath).build());
            ZonedDateTime zonedDateTime = statObjectResponse.lastModified();
            Timestamp timestamp = Timestamp.from(zonedDateTime.toInstant());
            final long time = timestamp.getTime();
            if (lastModifiedTime < time) {
                lastModifiedTime = time;
            }

        } catch (Exception e) {
            throw new DpException(StatusCode.CODE_10010, "获取文件最后修改时间失败", e);
        }
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

    @Override
    public InputStream getFileObject(String relativePath) throws AmazonServiceException {
        String finalRelativePath = getFileKey(relativePath);
        InputStream inputStream = null;
        try {
            inputStream = minioClient.getObject(
                GetObjectArgs.builder()
                    .bucket(minIoProperties.getBucketName())
                    .object(finalRelativePath)
                    .build());
            return inputStream;
        } catch (Exception e) {
            throw new DpException(StatusCode.CODE_10010, "获取文件流失败", e);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }

    }

    @Override
    public boolean exists(String filePath) {
        String isFilePath = getFileKey(filePath);
        try {
            StatObjectResponse statObjectResponse = minioClient.statObject(
                StatObjectArgs.builder().bucket(minIoProperties.getBucketName()).object(isFilePath).build());
            if (statObjectResponse != null) {
                return true;
            }
        } catch (Exception e) {
            throw new DpException(StatusCode.CODE_10010, "minIo判断文件失败", e);
        }
        return false;
    }

    @Override
    public String getUserName() {
        return minIoProperties.getAccessKey();
    }

    @Override
    public String getPassword() {
        return minIoProperties.getSecretKey();
    }

    @Override
    public String getRootPath() {
        return minIoProperties.getRootPath() + minIoProperties.getUrl() + "/" + minIoProperties.getBucketName() + "/";
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

        if (finalPath.startsWith(minIoProperties.getBucketName())) {
            finalPath = finalPath.substring(minIoProperties.getBucketName().length() + 1);
        }

        return finalPath;
    }
}

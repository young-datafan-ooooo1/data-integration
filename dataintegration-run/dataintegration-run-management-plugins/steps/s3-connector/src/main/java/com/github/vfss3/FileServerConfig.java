package com.github.vfss3;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.auth.StaticUserAuthenticator;
import org.apache.commons.vfs2.impl.DefaultFileSystemConfigBuilder;
import org.apache.commons.vfs2.provider.ftp.FtpFileSystemConfigBuilder;

public class FileServerConfig {

    public static FileSystemOptions getFileSystemOptions(String fileServerType, String username, String password) throws FileSystemException {
        FileSystemOptions opts = new FileSystemOptions();
        switch (fileServerType) {
            case "ftp":
                StaticUserAuthenticator auth = new StaticUserAuthenticator("username", username, password);
                DefaultFileSystemConfigBuilder.getInstance().setUserAuthenticator(opts, auth);
                FtpFileSystemConfigBuilder.getInstance().setPassiveMode(opts, true);
                break;
            case "s3":
                S3FileSystemConfigBuilder.getInstance().setUseHttps(opts, false);
                S3FileSystemConfigBuilder.getInstance().setCredentialsProvider(opts, new AWSStaticCredentialsProvider(new BasicAWSCredentials(username, password)));
                break;
            default:
                S3FileSystemConfigBuilder.getInstance().setUseHttps(opts, false);
                S3FileSystemConfigBuilder.getInstance().setCredentialsProvider(opts, new AWSStaticCredentialsProvider(new BasicAWSCredentials(username, password)));
                break;
        }
        return opts;
    }
}

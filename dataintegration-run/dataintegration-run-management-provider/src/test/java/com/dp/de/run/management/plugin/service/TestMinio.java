package com.dp.de.run.management.plugin.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.youngdatafan.dataintegration.core.util.UUIDUtils;
import com.github.vfss3.S3FileSystemConfigBuilder;
import org.apache.commons.vfs2.*;
import org.pentaho.di.core.vfs.KettleVFS;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: jeremychen
 * @Descripition:
 * @Date:2020/4/7 1:45 下午
 */
public class TestMinio {

    public static void main(String[] args) throws Exception {

        AtomicInteger count = new AtomicInteger(0);
        List<String> urls = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Thread t = new Thread(() -> {
                for (int j = 0; j < 10; j++) {
                    try {
                        FileSystemOptions options = new FileSystemOptions();
                        S3FileSystemConfigBuilder.getInstance().setUseHttps(options, false);
                        S3FileSystemConfigBuilder.getInstance().setCredentialsProvider(options,new AWSStaticCredentialsProvider(new BasicAWSCredentials("prime", "prime@2020")) );

                        String url = "s3://10.242.10.166:19000/dp-s3/51149b4a-86eb-4e5d-85ea-3f1529ac5b49/8e17f09b46284153bf076e0ab6f576eb/" + UUIDUtils.generateUUID32() + ".txt";
                        FileObject fileObject = KettleVFS.getFileObject(url, options);
                        if (!fileObject.exists()) {
                            fileObject.createFile();
                        }

                        try (final OutputStream outputStream = fileObject.getContent().getOutputStream()) {
                            outputStream.write("fdasfdsf".getBytes());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        urls.add(url);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                count.addAndGet(1);
            });
            t.start();
        }

        for (int i = 0; i < 10; i++) {
            Thread t = new Thread(() -> {

                for (int j = 0; j < 10; j++) {
                    for (String url : urls) {
                        try {
                            FileSystemOptions options = new FileSystemOptions();
                            S3FileSystemConfigBuilder.getInstance().setUseHttps(options, false);
                            S3FileSystemConfigBuilder.getInstance().setCredentialsProvider(options,new AWSStaticCredentialsProvider(new BasicAWSCredentials("prime", "prime@2020")) );

                            FileObject fileObject1 = KettleVFS.getFileObject(url, options);
                            System.out.println(count.get() + "--|||||" + fileObject1.exists());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                count.addAndGet(1);
            });
            t.start();
        }

        while (count.get() < 20) {
            TimeUnit.MILLISECONDS.sleep(200);
        }

    }

}


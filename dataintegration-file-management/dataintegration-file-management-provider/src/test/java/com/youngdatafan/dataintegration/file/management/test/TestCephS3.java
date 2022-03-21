package com.youngdatafan.dataintegration.file.management.test;

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
import com.amazonaws.util.StringUtils;

import java.io.ByteArrayInputStream;

/**
 * @Author: jeremychen
 * @Descripition:
 * @Date:2020/4/7 1:45 下午
 */
public class TestCephS3 {
    public static void main(String[] args) throws Exception {
        AWSCredentials credentials = new BasicAWSCredentials("BMXG3WP8JA9D1GSD2AJJ", "vl32x2t0sBxy0BEgcY9Iz442HK2HobPTNw4T99yK");

        ClientConfiguration clientConfig = new ClientConfiguration();
        clientConfig.setProtocol(Protocol.HTTP);

        AmazonS3 s3Client = new AmazonS3Client(credentials, clientConfig);
        s3Client.setEndpoint("192.168.10.241:38000");

        System.out.println(s3Client.listBuckets());
        final S3Object object = s3Client.getObject("escat-s3", "admin/ffffff/订阅测试_lizihao_1_20210906.csv");
        System.out.println(object);


        ObjectListing objectListing = s3Client.listObjects("escat-s3", "xiaosha/1026/");
        do {
            for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
                System.out.println(objectSummary.getKey() + "\t" +
                        objectSummary.getSize() + "\t" +
                        StringUtils.fromDate(objectSummary.getLastModified()));
            }
            objectListing = s3Client.listNextBatchOfObjects(objectListing);
        } while (objectListing.isTruncated());

        // 删除文件
        s3Client.deleteObject("escat-s3", "abc");

//        System.out.println(object);
        ByteArrayInputStream input = new ByteArrayInputStream("Hello World!".getBytes());
        final ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(input.available());
        s3Client.putObject("escat-s3", "hello.txt", input, metadata);

        System.out.println("xxxxxxxx" + s3Client.doesObjectExist("escat-s3", "abfdsafdfdsafdsafdsafdsa/fdsafasd/"));

        s3Client.deleteObject("escat-s3", "abfdsafdfdsafdsa");
    }

}


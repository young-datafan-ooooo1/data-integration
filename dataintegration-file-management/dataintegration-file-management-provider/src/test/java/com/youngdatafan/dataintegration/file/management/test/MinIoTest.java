package com.youngdatafan.dataintegration.file.management.test;



import com.youngdatafan.dataintegration.file.management.FileManagerApplication;
import com.youngdatafan.dataintegration.file.management.service.FileSystemManagerService;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import org.apache.poi.util.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

/**
 * minIo测试.
 *
 * @author songxiaolang
 * @since 2022-03-30 17:21:35
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ConditionalOnProperty(prefix = "file.server", name = "useServer", havingValue = "s3")
@SpringBootTest(classes = {FileManagerApplication.class, ProjectPreviewServiceTest.class}
    , webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class MinIoTest {

    @Autowired
    private FileSystemManagerService minIoFileSystemManagerService;

    /**
     * 文件上传测试.
     */
    @Test
    public void addFileTest() {
        String path = minIoFileSystemManagerService.getRootPath() + "songxiaolang" + "/" + "测试" + "/" + "字段信息13.xlsx";
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(new File("/Users/songxiaolang/资产平台.xlsx"));
            minIoFileSystemManagerService.addFile(path, inputStream, 23059);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(inputStream);
        }

    }

    /**
     * 新增文件夹测试.
     */
    @Test
    public void addFolderTest() {
        minIoFileSystemManagerService.addFolder("songxiaolang/测试");
    }

    /**
     * 删除文件测试.
     */
    @Test
    public void delFileTest() {
        String path = minIoFileSystemManagerService.getRootPath() + "songxiaolang" + "/" + "测试" + "/" + "字段信息1.xlsx";
        minIoFileSystemManagerService.delFile(path);
    }


    /**
     * 获取文件最后修改时间测试.
     */
    @Test
    public void getLastModifyFileTimeTest() {
        String path = minIoFileSystemManagerService.getRootPath() + "songxiaolang" + "/" + "测试" + "/" + "字段信息1.xlsx";
        long lastModifyFileTime = minIoFileSystemManagerService.getLastModifyFileTime(path);
        System.out.println(lastModifyFileTime);
    }

    /**
     * 获取文件名称测试.
     */
    @Test
    public void getBaseNameTest() {
        String key = "songxiaolang/测试/字段信息.xlsx";
        String baseName = minIoFileSystemManagerService.getBaseName(key);
        assertEquals("字段信息.xlsx", baseName);
    }

    /**
     * 下载文件测试.
     */
    @Test
    public void getFileObjectTest() {
        String path = minIoFileSystemManagerService.getRootPath() + "songxiaolang" + "/" + "测试" + "/" + "字段信息1233.xlsx";
        InputStream fileObject = null;
        try {
            fileObject = minIoFileSystemManagerService.getFileObject(path);
            String localPath = "/Users/songxiaolang/work/test/返回结果6.xlsx";
            int index;
            byte[] bytes = new byte[1024];
            FileOutputStream downloadFile = new FileOutputStream(localPath);
            while ((index = fileObject.read(bytes)) != -1) {
                downloadFile.write(bytes, 0, index);
                downloadFile.flush();
            }
            downloadFile.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(fileObject);
        }


    }

    /**
     * 获取文件夹列表信息测试测试.
     */
    @Test
    public void loopFolderTest() {
        String path = minIoFileSystemManagerService.getRootPath() + "songxiaolang/测试/";
        minIoFileSystemManagerService.loopFolder(path, objectSummary -> {
            System.out.println(objectSummary.getLastModified());
            System.out.println(objectSummary.getKey());
            System.out.println(objectSummary.getSize());
            return null;
        });
    }

    /**
     * 删除文件夹测试.
     */
    @Test
    public void delFolderTest() {
        String path = minIoFileSystemManagerService.getRootPath() + "songxiaolang/测试/";
        minIoFileSystemManagerService.delFolder(path);
    }


}
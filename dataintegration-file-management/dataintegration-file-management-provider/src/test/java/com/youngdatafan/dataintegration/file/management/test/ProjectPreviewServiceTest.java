package com.youngdatafan.dataintegration.file.management.test;

import com.youngdatafan.dataintegration.file.management.FileManagerApplication;
import com.youngdatafan.dataintegration.file.management.service.FileSystemManagerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author gavin
 * @create 2020/1/16 7:21 下午
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {FileManagerApplication.class, ProjectPreviewServiceTest.class}
        , webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ProjectPreviewServiceTest {

    @Autowired
    FileSystemManagerService fileSystemManagerService;

    @Test
    public void execute() throws Exception {
        fileSystemManagerService.getRootPath();
        fileSystemManagerService.getRootPath();

    }

}

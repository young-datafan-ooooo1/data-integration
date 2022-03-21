package com.youngdatafan.dataintegration.file.management.service;

import com.youngdatafan.dataintegration.file.management.FileManagerApplication;
import com.youngdatafan.dataintegration.file.management.model.DpPortalFileManager;
import com.youngdatafan.dataintegration.file.management.service.impl.DpPortalFileManagerServiceImpl;
import com.youngdatafan.dataintegration.file.management.test.ProjectPreviewServiceTest;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author songxiaolang
 * @since 2022-01-11 19:08
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {FileManagerApplication.class, ProjectPreviewServiceTest.class}
    , webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class DpPortalFileManagerServiceTest {

    @Autowired
    private DpPortalFileManagerServiceImpl dpPortalFileManagerService;

    @Test
    public void clearFiles() {
        //测试刷新某个文件夹
        List<DpPortalFileManager> dpPortalFileManagers = dpPortalFileManagerService.selectFileList(null, "1", "051ba25625ad40dda94c5317ef949aaf");
        dpPortalFileManagerService.clearFiles(dpPortalFileManagers);
    }
}
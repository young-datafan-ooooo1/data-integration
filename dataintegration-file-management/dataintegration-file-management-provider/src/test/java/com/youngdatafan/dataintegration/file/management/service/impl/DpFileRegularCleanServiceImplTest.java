package com.youngdatafan.dataintegration.file.management.service.impl;

import com.youngdatafan.dataintegration.file.management.FileManagerApplication;
import com.youngdatafan.dataintegration.file.management.dto.DpFileRegularCleanDTO;
import com.youngdatafan.dataintegration.file.management.service.DpFileRegularCleanService;
import com.youngdatafan.dataintegration.file.management.test.ProjectPreviewServiceTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author songxiaolang
 * @since 2022-01-05 14:48
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {FileManagerApplication.class, ProjectPreviewServiceTest.class}
    , webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class DpFileRegularCleanServiceImplTest {

    @Autowired
    private DpFileRegularCleanService dpFileRegularCleanService;

    @Test
    public void timingCleanFile() {
        DpFileRegularCleanDTO dpFileRegularCleanDTO = dpFileRegularCleanService.get();
        dpFileRegularCleanService.timingCleanFile(dpFileRegularCleanDTO);
    }
}
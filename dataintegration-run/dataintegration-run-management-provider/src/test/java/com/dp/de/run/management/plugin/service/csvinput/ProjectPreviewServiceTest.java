package com.dp.de.run.management.plugin.service.csvinput;

import com.dp.de.run.management.plugin.service.JsonToXmlTest;
import com.youngdatafan.di.run.management.DiRunManagementApplication;
import com.youngdatafan.di.run.management.server.dto.ProjectPreviewExecutorDTO;
import com.youngdatafan.di.run.management.server.service.ProjectPreviewExecutorService;
import com.youngdatafan.di.run.management.server.vo.ProjectFilePreviewExecutorVO;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.InputStream;

/**
 * @author gavin
 * @since 2020/1/16 7:21 下午
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {DiRunManagementApplication.class, ProjectPreviewServiceTest.class}
        , webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ProjectPreviewServiceTest {

    @Autowired
    ProjectPreviewExecutorService previewExecutorService;

    @Test
    public void execute() throws Exception {
        final InputStream resourceAsStream = JsonToXmlTest.class.getClassLoader().getResourceAsStream("1.xml");
        final byte[] data = new byte[resourceAsStream.available()];
        IOUtils.readFully(resourceAsStream, data);
        String xml = new String(data);
        ProjectFilePreviewExecutorVO projectFilePreviewExecutorVO = new ProjectFilePreviewExecutorVO();
        projectFilePreviewExecutorVO.setPreviewSize(1);
        projectFilePreviewExecutorVO.setProjectId("b38b3b6f5a5c462dae6d517803f4b13f11");
        projectFilePreviewExecutorVO.setProjectName("测试项目");
        projectFilePreviewExecutorVO.setProjectFile(xml);
        projectFilePreviewExecutorVO.setPreviewStepName("CSV文件输入");
        ProjectPreviewExecutorDTO previewExecutorDTO = previewExecutorService.executeByFile(projectFilePreviewExecutorVO);
        System.out.println(previewExecutorDTO);
    }

}

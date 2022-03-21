package com.youngdatafan.dataintegration.file.management.test;



import com.youngdatafan.dataintegration.file.management.FileManagerApplication;
import java.io.File;
import org.jodconverter.DocumentConverter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * openoffice测试.
 *
 * @author songxiaolang
 * @since 2021-11-25 15:37
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {FileManagerApplication.class, ProjectPreviewServiceTest.class}
    , webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class OfficeTest {

   // 第一步：转换器直接注入
    @Autowired
    private DocumentConverter converter;
    @Test
    public void officeTest() throws Exception {
        File file = new File("/Users/songxiaolang/漏洞修复报告.docx");//需要转换的文件
        try {
            //文件转化
            converter.convert(file).to(new File("/Users/songxiaolang/officetest/hello.pdf")).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void excelTest() throws Exception {
        File file = new File("/Users/songxiaolang/稠州银行数据中台项目组简历（宋晓朗).xls");//需要转换的文件
        try {
            //文件转化
            converter.convert(file).to(new File("/Users/songxiaolang/officetest/excel.pdf")).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void pptTest()  {
        File file = new File("/Users/songxiaolang/ceshi.pptx");//需要转换的文件
        try {
            //文件转化
            converter.convert(file).to(new File("/Users/songxiaolang/officetest/ppt.pdf")).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void jpgTest()  {
        File file = new File("/Users/songxiaolang/officetest/1.jpg");//需要转换的文件
        try {
            //文件转化
            converter.convert(file).to(new File("/Users/songxiaolang/officetest/jpg.pdf")).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
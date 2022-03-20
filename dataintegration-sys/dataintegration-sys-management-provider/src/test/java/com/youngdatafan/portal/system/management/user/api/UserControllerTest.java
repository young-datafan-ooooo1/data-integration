package com.youngdatafan.portal.system.management.user.api;

import com.youngdatafan.dataintegration.core.util.JsonUtils;
import com.youngdatafan.portal.system.management.SystemManagerApplication;
import com.youngdatafan.portal.system.management.user.vo.UserAddVO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author gavin
 * @Description
 * @Date 2020/1/16 7:21 下午
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {SystemManagerApplication.class, UserControllerTest.class})
public class UserControllerTest {


    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() throws Exception {
        //MockMvcBuilders.webAppContextSetup(WebApplicationContext context)：指定WebApplicationContext，将会从该上下文获取相应的控制器并得到相应的MockMvc；
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();//建议使用这种
    }

    @Test
    public void testAdd() throws Exception {
        UserAddVO userAddVO = new UserAddVO();
        userAddVO.setUserName("admin123");
        userAddVO.setUserEmail("hmshzx2403@126.com");
        userAddVO.setUserMobile("17712269278");
        userAddVO.setDescribe("测试用户");

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/user/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.toString(userAddVO)))
                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();
        System.out.println("----------------------");
        System.out.println(content);

        Assert.assertEquals(200, status);
    }


}

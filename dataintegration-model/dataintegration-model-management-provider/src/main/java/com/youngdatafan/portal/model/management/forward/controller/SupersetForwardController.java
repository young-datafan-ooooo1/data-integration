package com.youngdatafan.portal.model.management.forward.controller;

import com.youngdatafan.portal.model.management.forward.service.SupersetForwardService;
import com.youngdatafan.portal.model.management.superset.api.SupersetForwardServiceApi;
import com.youngdatafan.portal.model.management.superset.vo.SupersetVO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>****************************************************************************</p>
 * <ul style="margin:15px;">
 * <li>Description : description</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2020/3/3 3:22 PM</li>
 * <li>Author      : ksice_xt</li>
 * </ul>
 * <p>****************************************************************************</p>
 */
@Controller
@RequestMapping("/superset")
public class SupersetForwardController implements SupersetForwardServiceApi {
    @Resource
    SupersetForwardService supersetForwardService;

//    @RequestMapping("/forward")
//    public void forward1(String userId, SupersetVO supersetVO, HttpServletRequest request, HttpServletResponse response) {
//        supersetForwardService.execute(request, response, supersetVO);
//    }


    @Override
    public void forward(String userId, SupersetVO supersetVO, HttpServletRequest request, HttpServletResponse response) {
        supersetForwardService.execute(request, response, supersetVO);

    }
}

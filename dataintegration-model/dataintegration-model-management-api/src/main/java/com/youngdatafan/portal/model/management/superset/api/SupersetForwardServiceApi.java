package com.youngdatafan.portal.model.management.superset.api;

import com.youngdatafan.portal.model.management.superset.vo.SupersetVO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>****************************************************************************</p>
 * <ul style="margin:15px;">
 * <li>Description : description</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2020/3/3 3:49 PM</li>
 * <li>Author      : ksice_xt</li>
 * </ul>
 * <p>****************************************************************************</p>
 */
public interface SupersetForwardServiceApi {
    @ApiOperation(value = "superset重定向", produces = "application/json")
    @PostMapping(value = "/forward")
    void forward(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId, SupersetVO supersetVO,
                 HttpServletRequest httpServletRequest, HttpServletResponse response);


}

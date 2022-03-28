package com.youngdatafan.portal.model.management.superset.api;

import com.youngdatafan.portal.model.management.superset.vo.SupersetVO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * SupersetForwardServiceApi.
 */
public interface SupersetForwardServiceApi {
    /**
     * superset重定向.
     *
     * @param userId             userId
     * @param supersetVO         supersetVO
     * @param httpServletRequest httpServletRequest
     * @param response           response
     */
    @ApiOperation(value = "superset重定向", produces = "application/json")
    @PostMapping(value = "/forward")
    void forward(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId, SupersetVO supersetVO,
                 HttpServletRequest httpServletRequest, HttpServletResponse response);

}

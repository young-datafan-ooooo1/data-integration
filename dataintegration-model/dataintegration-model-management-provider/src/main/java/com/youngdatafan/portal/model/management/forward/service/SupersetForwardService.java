package com.youngdatafan.portal.model.management.forward.service;

import com.youngdatafan.portal.model.management.superset.vo.SupersetVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>****************************************************************************</p>
 * <ul style="margin:15px;">
 * <li>Description : description</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2020/3/3 3:23 PM</li>
 * <li>Author      : ksice_xt</li>
 * </ul>
 * <p>****************************************************************************</p>
 */
public interface SupersetForwardService {
    void execute(HttpServletRequest req, HttpServletResponse resp, SupersetVO supersetVO);
}

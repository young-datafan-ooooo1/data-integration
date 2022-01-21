package com.youngdatafan.portal.system.management.common.utils;

import com.github.pagehelper.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @param <T>
 * @author dengguangming
 */
public class BaseController<T> {

    @InitBinder
    public void initBinder(ServletRequestDataBinder binder) {
        /**
         * 自动转换日期类型的字段格式
         */
        binder.registerCustomEditor(Date.class,
                new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), true));

        /**
         * 防止XSS攻击
         */
        binder.registerCustomEditor(String.class, new StringEscapeEditor(true, false));
    }

    /**
     * @param pageSize 每页显示数量
     * @param curPage  当前页面
     * @return
     */
    public Page<T> initPage(String pageSize, String curPage) {
        int pageSize1 = 10;// 每页显示的条数
        int curPage1 = 1;// 当前页数
        if (StringUtils.isNotBlank(pageSize)) {
            pageSize1 = Integer.parseInt(pageSize);
        }
        if (StringUtils.isNotBlank(curPage)) {
            curPage1 = Integer.parseInt(curPage);
        }
        Page<T> page = new Page<>();
        page.setPageNum(curPage1);
        page.setPageSize(pageSize1);
        return page;
    }

    /**
     * 加载分页对象
     *
     * @param request
     * @return
     */
    public Page<Map<String, Object>> initCommonPage(HttpServletRequest request) {
        int pageSize = 10;// 每页显示的条数
        int curPage = 1;// 当前页数
        if (StringUtils.isNotBlank(request.getParameter("pageSize"))) {
            pageSize = Integer.parseInt(request.getParameter("pageSize"));
        }
        if (StringUtils.isNotBlank(request.getParameter("curPage"))) {
            curPage = Integer.parseInt(request.getParameter("curPage"));
        }
        Page<Map<String, Object>> page = new Page<>();
        page.setPageNum(curPage);
        page.setPageSize(pageSize);
        return page;
    }

    /**
     * 加载分页对象(包括查询条件)
     *
     * @param request
     * @return
     */
    public PageDTO<T> initPageWithQuery(HttpServletRequest request) {
        int pageSize = 10;// 每页显示的条数
        int curPage = 1;// 当前页数
        List<String> queryParams = new LinkedList<String>();
        if (StringUtils.isNotBlank(request.getParameter("pageSize"))) {
            pageSize = Integer.parseInt(request.getParameter("pageSize"));
        }
        if (StringUtils.isNotBlank(request.getParameter("curPage"))) {
            curPage = Integer.parseInt(request.getParameter("curPage"));
        }
        if (StringUtils.isNotBlank(request.getParameter("queryParams"))) {
            queryParams = Arrays.asList(request.getParameter("queryParams").split(","));
        }
        PageDTO<T> pageDTO = new PageDTO<>();
        Page<T> page = new Page<>();
        page.setPageNum(curPage);
        page.setPageSize(pageSize);
        pageDTO.setPage(page);
        if (queryParams != null && queryParams.size() > 0) {
            List<Map<String, Object>> tempList = new ArrayList<Map<String, Object>>();
            Map<String, Object> maps;
            for (String string : queryParams) {
                maps = new HashMap<String, Object>();
                try {
                    maps.put(string.split("\\$")[0],
                            URLDecoder.decode(URLDecoder.decode(string.split("\\$")[1], "UTF-8"), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                tempList.add(maps);

            }
            pageDTO.setQueryParams(tempList);
        }
        return pageDTO;
    }
}

package com.youngdatafan.dataintegration.file.management.utils;

import com.github.pagehelper.Page;
import io.swagger.annotations.ApiModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 基础控制器.
 *
 * @param <T> 泛型
 * @author dengguangming
 */
@ApiModel("基础控制器")
public class BaseController<T> {


    /**
     * 初始化数据的日期格式.
     *
     * @param binder 数据绑定.
     */
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
     * 初始化分页.
     *
     * @param pageSize 每页显示数量
     * @param curPage  当前页面
     * @return 处理后的分页对象.
     */
    public Page<T> initPage(String pageSize, String curPage) {
        // 每页显示的条数
        int pageSize1 = 10;

        // 当前页数
        int curPage1 = 1;
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
     * 加载分页对象.
     *
     * @param request 请求参数.
     * @return 处理后的分页对象.
     */
    public Page<Map<String, Object>> initCommonPage(HttpServletRequest request) {

        // 每页显示的条数
        int pageSize = 10;

        // 当前页数
        int curPage = 1;
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
     * 加载分页对象(包括查询条件).
     *
     * @param request 前端请求.
     * @return 自定义分页对象
     */
    public PageDTO<T> initPageWithQuery(HttpServletRequest request) {
        // 每页显示的条数
        int pageSize = 10;
        // 当前页数
        int curPage = 1;

        List<String> queryParams = new LinkedList<>();
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
        if (!queryParams.isEmpty()) {
            List<Map<String, Object>> tempList = new ArrayList<>();
            Map<String, Object> maps;
            for (String string : queryParams) {
                maps = new HashMap<>();
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

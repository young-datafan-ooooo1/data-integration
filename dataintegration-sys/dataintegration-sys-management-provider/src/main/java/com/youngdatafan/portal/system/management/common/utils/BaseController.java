package com.youngdatafan.portal.system.management.common.utils;

import com.github.pagehelper.Page;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

/**
 * BaseController.
 *
 * @author dengguangming
 */
public class BaseController<T> {

    /**
     * initBinder.
     *
     * @param binder binder
     */
    @InitBinder
    public void initBinder(ServletRequestDataBinder binder) {
        /**
         * 自动转换日期类型的字段格式.
         */
        binder.registerCustomEditor(Date.class,
            new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), true));

        /**
         * 防止XSS攻击.
         */
        binder.registerCustomEditor(String.class, new StringEscapeEditor(true, false));
    }

    /**
     * initPage.
     *
     * @param pageSize 每页显示数量
     * @param curPage  当前页面
     * @return Page
     */
    public Page<T> initPage(String pageSize, String curPage) {
        int pageSize1 = 10;
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
}

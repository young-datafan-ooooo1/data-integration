package com.youngdatafan.dataintegration.file.management.utils;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiModel;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.BeanUtils;

/**
 * github Pagehelper工具.
 *
 * @author yuyufeng
 * @since 2019/7/22 10:38
 */

@ApiModel("PageHelper工具")
public class PageInfoUtil {

    /**
     * 将PageInfo对象转换成 PageDTO对象.
     * @param pageInfoPO PageInfo对象
     * @param dClass PageInfoDTO对象
     * @param <P> 泛型
     * @param <D> 泛型
     * @return PageInfoDTO对象
     */
    public static <P, D> PageInfo<D> pageInfo2PageInfoDTO(PageInfo<P> pageInfoPO, Class<D> dClass) {
        Page<D> page = new Page<>(pageInfoPO.getPageNum(), pageInfoPO.getPageSize());
        page.setTotal(pageInfoPO.getTotal());
        for (P p : pageInfoPO.getList()) {
            D d = null;
            try {
                d = dClass.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            BeanUtils.copyProperties(p, d);
            page.add(d);
        }
        return new PageInfo<>(page);
    }

    /**
     * 初始化分页.
     *
     * @param pageSize 每页显示数量
     * @param curPage  当前页面
     * @return 初始化之后的分页对象.
     */
    public static Page<T> initPage(String pageSize, String curPage) {
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
}

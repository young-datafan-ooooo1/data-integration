package com.youngdatafan.portal.system.management.common.utils;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;

/**
 * @description:github Pagehelper工具
 * @Author:yuyufeng
 * @Date:2019/7/22 10:38
 */
public class PageInfoUtil {
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
}

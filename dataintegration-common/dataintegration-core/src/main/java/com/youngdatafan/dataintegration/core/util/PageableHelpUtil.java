package com.youngdatafan.dataintegration.core.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.ServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * 分页帮助工具类.
 *
 * @author qiguohui
 * @since 2021-07-16 10:13
 */
public class PageableHelpUtil {

    /**
     * 分页工具类方法.
     *
     * @param request 请求参数
     * @return 返回分页
     */
    public static Pageable startPage(ServletRequest request) {
        String pageString = request.getParameter("page");
        String pageSizeString = request.getParameter("pageSize");
        int page = pageString == null ? 0 : Integer.parseInt(pageString);
        int pageSize = pageSizeString == null ? 10 : Integer.parseInt(pageSizeString);
        String sorts = request.getParameter("sorts");

        //是否需要多级过滤
        if (StringUtils.isBlank(sorts)) {
            return PageRequest.of(page, pageSize);
        } else {
            List<String> sortList = Arrays.asList(sorts.split(","));
            List<Sort.Order> list = new ArrayList<>();
            sortList.forEach(field -> {
                String[] split = field.split(":");
                Sort.Order order = new Sort.Order(split[1].toUpperCase().equals(Sort.Direction.ASC.toString())
                    ? Sort.Direction.ASC : Sort.Direction.DESC, split[0]);
                list.add(order);
            });
            return PageRequest.of(page, pageSize, Sort.by(list));
        }
    }

    /**
     * 返回最终的排序字符串.
     * @param sort 排序对象
     * @return 最终排序字符串
     */
    public static String getOrders(Sort sort) {
        if (sort == null || sort.isEmpty()) {
            return null;
        }

        StringBuilder stringBuilder = new StringBuilder();
        sort.stream().forEach(item -> {
            stringBuilder.append(item.getProperty());
            stringBuilder.append(" ");
            stringBuilder.append(item.getDirection());
            stringBuilder.append(" ,");
        });

        return stringBuilder.substring(0, stringBuilder.length() - 1);
    }

}

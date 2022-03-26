package com.youngdatafan.portal.system.management.common.utils;

import com.github.pagehelper.Page;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.Data;

/**
 * 自定义分页对象,扩展pageHelper的page.
 *
 * @author dengguangming
 */
@Data
public class PageDTO<T> {

    private Page<T> page;

    private List<Map<String, Object>> queryParams = new ArrayList<Map<String, Object>>();
}

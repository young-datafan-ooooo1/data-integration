package com.youngdatafan.portal.system.management.common.utils;

import com.github.pagehelper.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 自定义分页对象,扩展pageHelper的page
 * 
 * @author dengguangming
 *
 */
public class PageDTO<T> {

	private Page<T> page;

	public Page<T> getPage() {
		return page;
	}

	public void setPage(Page<T> page) {
		this.page = page;
	}

	private List<Map<String, Object>> queryParams = new ArrayList<Map<String, Object>>();// 查询条件

	public List<Map<String, Object>> getQueryParams() {
		return queryParams;
	}

	public void setQueryParams(List<Map<String, Object>> queryParams) {
		this.queryParams = queryParams;
	}

}

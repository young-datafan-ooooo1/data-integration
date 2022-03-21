package com.youngdatafan.dataintegration.file.management.utils;

import com.github.pagehelper.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 自定义分页对象,扩展pageHelper的page.
 *
 * @author dengguangming
 */

@ApiModel("自定义的文件分页对象")
@Data
public class PageDTO<T> {

    /**
     * 原始文件对象.
     */
    @ApiModelProperty("原始文件对象")
    private Page<T> page;

    /**
     * 查询条件.
     */
    @ApiModelProperty("查询条件")
    private List<Map<String, Object>> queryParams = new ArrayList<Map<String, Object>>();


}

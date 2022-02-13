package com.youngdatafan.di.run.management.server.entity;

import lombok.Data;

/**
 * @Author: jeremychen
 * @Descripition:
 * @Date:2020/2/19 11:33 下午
 */
@Data
public class DpPortalPluginInfo {
    /**
     * 插件id
     */
    private String pluginId;

    /**
     * 插件名称
     */
    private String pluginName;

    /**
     * 插件描述
     */
    private String pluginDescribe;

    /**
     * 插件分类
     */
    private String pluginCategory;

    /**
     * 插件类型
     */
    private String pluginType;

    /**
     * 插件图片
     */
    private String pluginImage;

    private Integer categoryOrder;

    private Integer pluginOrder;

    /**
     * 插件二级类型 input-输入 output-输出  merge-合并类型 filter-过滤型 normal-基本型
     */
    private String secondPluginType;


    private String  pluginFilter;

    private String  pluginOutput;

}
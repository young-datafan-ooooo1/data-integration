package com.youngdatafan.di.run.management.server.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: jeremychen
 * @Descripition:
 * @Date:2020/2/18 3:48 下午
 */
@Data
@ApiModel("插件信息")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PluginInfoDTO<T>  {


    @ApiModelProperty("插件Id")
    private String pluginId;

    @ApiModelProperty("插件中文名")
    private String pluginName;

    @ApiModelProperty("插件描述")
    private String pluginDescribe;

    @ApiModelProperty("插件模型名称")
    private String modelName;

    @ApiModelProperty("插件模型中文名称")
    private String modelNameCn;

    @ApiModelProperty("模型类型")
    private String modelType;
    /**
     * 插件分类
     */
    @ApiModelProperty("插件分类")
    private String pluginCategory;

    /**
     * 插件类型（basic-基础拆件，business-业务插件）
     */
    @ApiModelProperty("插件类型（basic-基础拆件，business-业务插件）")
    private String pluginType;

    /**
     * 插件图片
     */
    @ApiModelProperty("插件图片")
    private String pluginImage;

    private Integer categoryOrder;

    private Integer pluginOrder;


    @ApiModelProperty("插件二级类型 input-输入 output-输出  merge-合并类型 filter-过滤型 normal-基本型")
    private String secondPluginType;


    @ApiModelProperty("用来判断是否可以分发复制")
    private String  pluginFilter;

    @ApiModelProperty("用来判断是否是主输出")
    private String  pluginOutput;

    private boolean hasParent = false;

    private boolean hasChildren = false;


    private  String  nodeId;

    private  String  id;

    private  String  label;


    private List<PluginInfoDTO<T>> children;


    public void initChildren(){
        this.children = new ArrayList<>();
    }
}

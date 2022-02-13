package com.youngdatafan.di.run.management.server.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: jeremychen
 * @Descripition:
 * @Date:2020/2/18 4:05 下午
 */

@Data
@ApiModel("插件分类信息")
public class PluginCategoryDTO {

    @ApiModelProperty("插件类型")
    private String category;

    @ApiModelProperty("插件分类子信息")
    private List<PluginCategoryDTO> pluginCategoryDTOs;

    @ApiModelProperty("插件清单")
    private List<PluginInfoDTO> pluginInfoDTOS;
}

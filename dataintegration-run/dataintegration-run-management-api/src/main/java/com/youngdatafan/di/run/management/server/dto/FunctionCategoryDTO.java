package com.youngdatafan.di.run.management.server.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: jeremychen
 * @Descripition:
 * @Date:2020/3/5 11:06 上午
 */
@Data
@ApiModel("公式分类信息")
public class FunctionCategoryDTO{

   @ApiModelProperty("公式类型")
   private String category;

   @ApiModelProperty("公式")
   private List<FunctionDTO> functionDTOs;
}

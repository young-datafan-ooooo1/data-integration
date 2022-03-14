package com.youngdatafan.portal.common.group.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * 模型分组输入参数.
 *
 * @author gavin
 * @since 2020/2/10 12:42 下午
 */
@Data
@NoArgsConstructor
@ApiModel(description = "模型分组新增对象")
public class GroupAddVO {

    @ApiModelProperty(value = "分组名称", required = true)
    @NotNull
    @Length(min = 1, max = 30)
    private String groupName;

    @ApiModelProperty(value = "分组描述")
    private String describe;

    @ApiModelProperty(value = "分组类型（使用拼音简写）：基本模型(JBMX)、业务模型(YWMX)、输出类模型(SCLMX)、自定义模型(ZDYMX)、探索脚本(TSJB)、集成脚本(JSJB)、报表(BB)、项目(XM)", required = true)
    @NotNull
    @Length(min = 1, max = 30)
    private String groupType;

    @ApiModelProperty(value = "分组排序")
    private Integer groupOrder;

    @ApiModelProperty(value = "是否启用\tT-启用；F-未启用；", required = true)
    @Length(min = 1, max = 1)
    private String enabled;

}

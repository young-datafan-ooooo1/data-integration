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
@ApiModel(description = "模型分组联合组件对象")
public class GroupUkVO {

    @ApiModelProperty(value = "分组名称", required = true)
    @NotNull
    @Length(min = 1, max = 30)
    private String groupName;

    @ApiModelProperty(value = "分组类型（英文）：基本模型、业务模型、探索脚本、集成脚本、报表", required = true)
    @NotNull
    @Length(min = 1, max = 30)
    private String groupType;

}

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
@ApiModel(description = "模型分组修改对象")
public class GroupUpdateVO {

    @ApiModelProperty(value = "分组编号", required = true)
    @NotNull
    @Length(min = 1, max = 50)
    private String groupId;

    @ApiModelProperty(value = "分组名称")
    @Length(min = 1, max = 30)
    private String groupName;

    @ApiModelProperty(value = "分组类型")
    @Length(min = 1, max = 30)
    private String groupType;

    @ApiModelProperty(value = "分组描述")
    private String describe;

    @ApiModelProperty(value = "分组排序")
    private Integer groupOrder;

    @ApiModelProperty(value = "是否启用\tT-启用；F-未启用；")
    private String enabled;

}

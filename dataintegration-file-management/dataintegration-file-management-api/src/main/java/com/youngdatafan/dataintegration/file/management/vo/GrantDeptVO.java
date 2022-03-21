package com.youngdatafan.dataintegration.file.management.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Data;

/**
 * 部门信息接受类.
 *
 * @author liuhao
 * @since 2022/1/19 16:47
 */

@Data
@ApiModel("部门信息接受类")
public class GrantDeptVO {

    /**
     * 所属部门名称.
     */
    @ApiModelProperty("所属部门名称")
    private String deptName;

    /**
     * 所属部门编码.
     */
    @ApiModelProperty("所属部门编码")
    private Integer deptId;

    /**
     * 用户信息.
     */
    @ApiModelProperty("用户信息")
    private List<GrantDeptUserVO> userList;
}

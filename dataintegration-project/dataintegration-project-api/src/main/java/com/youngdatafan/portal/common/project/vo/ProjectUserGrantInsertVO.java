package com.youngdatafan.portal.common.project.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 用户授权.
 *
 * @author gavin
 * @since 2020/2/12 1:02 下午
 */
@Data
@NoArgsConstructor
@ApiModel(description = "用户授权对象")
public class ProjectUserGrantInsertVO {

    @NotNull
    @Length(min = 1, max = 50)
    @ApiModelProperty(value = "项目编号(PK)", required = true)
    private String projectId;

    @ApiModelProperty(value = "项目用户授权集合")
    private List<ProjectUserGrantVO> projectUserGrants;

}

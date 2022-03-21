package com.youngdatafan.portal.common.project.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目附件对象.
 *
 * @author gavin
 * @since 2020/2/11 10:32 上午
 */
@Data
@NoArgsConstructor
@ApiModel(description = "项目附件对象")
public class ProjectFileVO {

    @ApiModelProperty(value = "项目文件内容", required = true)
    private String projectFile;

    @ApiModelProperty(value = "项目版本号", required = true)
    private Integer projectVersion;

    @ApiModelProperty(value = "项目Md5")
    private String md5;

}

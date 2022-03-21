package com.youngdatafan.portal.common.project.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
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
public class ProjectFileDTO {

    @ApiModelProperty(value = "项目编号")
    private String projectId;

    @ApiModelProperty(value = "项目文件内容")
    private String projectFile;

    @ApiModelProperty(value = "项目版本号")
    private String projectVersion;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "项目Md5")
    private String md5;

}

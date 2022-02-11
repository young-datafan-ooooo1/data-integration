package com.youngdatafan.di.run.management.server.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Map;

/**
 * @author gavin
 * @since 2020/2/13 10:20 上午
 */
@Data
@NoArgsConstructor
@ApiModel(description = "项目预览执行对象")
public class ProjectFilePreviewExecutorVO {

    @ApiModelProperty(value = "变量")
    Map<String, String> variables;
    @ApiModelProperty(value = "项目编号", required = true)
    @NotBlank
    @Length(min = 1, max = 100)
    private String projectId;
    @ApiModelProperty(value = "项目名称", required = true)
    @NotBlank
    @Length(min = 1, max = 100)
    private String projectName;
    @ApiModelProperty(value = "项目文件内容", required = true)
    @NotBlank
    private String projectFile;
    @ApiModelProperty(value = "预览行数", required = true)
    @NotBlank
    @Size(min = 1, max = 10000)
    private Integer previewSize;
    @ApiModelProperty(value = "预览步骤名", required = true)
    @NotBlank
    private String previewStepName;
}

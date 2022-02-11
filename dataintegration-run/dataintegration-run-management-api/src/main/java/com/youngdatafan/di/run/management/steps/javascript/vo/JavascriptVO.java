package com.youngdatafan.di.run.management.steps.javascript.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @author gavin
 * @since 2020/2/13 10:20 上午
 */
@Data
@NoArgsConstructor
public class JavascriptVO {

    @ApiModelProperty(value = "组件名称", required = true)
    @NotBlank
    @Length(min = 1, max = 100)
    private String name;

    @ApiModelProperty(value = "项目文件内容", required = true)
    @NotBlank
    private String projectFile;

}

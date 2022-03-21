package com.youngdatafan.dataintegration.file.management.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 文件脚本接受信息类.
 *
 * @Author: xiaosha
 * @Descripition:
 * @Date 2020/8/12 15:41
 */
@Data
@ApiModel("文件信息接受类")
public class FileSheetVO {
    /**
     * 文件id集合.
     */
    @ApiModelProperty(value = "文件id集合", required = true)
    @NotBlank
    private List<String> fileId;

}

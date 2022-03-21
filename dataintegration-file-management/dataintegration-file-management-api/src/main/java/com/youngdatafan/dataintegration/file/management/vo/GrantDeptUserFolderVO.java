package com.youngdatafan.dataintegration.file.management.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 权限部门用户文件夹信息接受类.
 *
 * @author liuhao
 * @since 2022/1/19 16:47
 */

@Data
@ApiModel("权限部分用户文件夹信息接受类")
public class GrantDeptUserFolderVO {

    /**
     * 文件夹id.
     */
    @ApiModelProperty("文件夹id")
    private String folderId;

    /**
     * 文件夹名称.
     */
    @ApiModelProperty("文件夹名称")
    private String folderName;
}

package com.youngdatafan.dataintegration.file.management.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 文件夹查询信息接受类.
 *
 * @author liuhao
 * @since 2022/1/19 16:45
 */

@Data
@ApiModel("文件夹查询信息接受类")
public class FolderQueryVO {

    /**
     * 文件夹id.
     */
    private String folderId;
}

package com.youngdatafan.dataintegration.file.management.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Data;

/**
 * 门户网站文件用户权限增加接受类.
 *
 * @author lizihao
 * @date 2021-12-27 14:53:37
 */
@ApiModel(value = "门户网站文件用户权限增加接受类")
@Data
public class DpPortalFileUserGrantAddVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文件id.
     */
    @ApiModelProperty("文件id")
    private String fileId;

    /**
     * 父目录id.
     */
    @ApiModelProperty("父目录id")
    private String pid;

    /**
     * 文件名.
     */
    @ApiModelProperty("文件名")
    private Integer userId;

    /**
     * 0文件1文件夹.
     */
    @ApiModelProperty("0文件1文件夹")
    private Integer grantType;
}

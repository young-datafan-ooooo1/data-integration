package com.youngdatafan.dataintegration.file.management.vo;

import com.youngdatafan.dataintegration.file.management.dto.FileGrantDTO;
import com.youngdatafan.dataintegration.file.management.dto.FolderGrantDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Data;

/**
 * 用户权限批量增加接受信息类.
 *
 * @author liuhao
 * @since 2022/1/19 16:34
 */

@Data
@ApiModel("用户权限批量增加接受信息类")
public class DpProtalFileUserGrantBatchAddVO {

    /**
     * 需要授权的用户集合.
     */
    @ApiModelProperty("需要授权的用户id集合")
    private List<Integer> userIdList;

    /**
     * 文件夹集合.
     */
    @ApiModelProperty("文件夹集合")
    private List<FolderGrantDTO> folderGrantDTOS;

    /**
     * 文件集合.
     */
    @ApiModelProperty("文件集合")
    private List<FileGrantDTO> fileGrantDTOS;
}

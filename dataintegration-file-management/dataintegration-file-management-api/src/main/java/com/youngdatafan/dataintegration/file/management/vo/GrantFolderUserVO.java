package com.youngdatafan.dataintegration.file.management.vo;

import com.youngdatafan.dataintegration.file.management.dto.GrantUserDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Data;

/**
 * 授权文件夹信息接受类.
 */

@Data
@ApiModel("授权文件夹信息接受类")
public class GrantFolderUserVO {

    /**
     * 所有用户.
     */
    @ApiModelProperty("所有用户")
    private List<GrantUserDTO> allUser;

    /**
     * 已授权的用户.
     */
    @ApiModelProperty("已授权的用户")
    private List<GrantUserDTO> grantUser;
}

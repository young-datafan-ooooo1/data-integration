package com.youngdatafan.dataintegration.file.management.vo;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class FolderRefreshParamVO {

    @ApiModelProperty("文件夹id集合")
    @NotEmpty(message = "文件夹id集合不能为空")
    private List<String> folderIds;
}

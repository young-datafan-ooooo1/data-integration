package com.youngdatafan.dataintegration.file.management.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.lang.Nullable;

import java.io.Serializable;

/**
 * 项目需求表样文件表.
 * @author songxiaolang
 * @since 2022/1/19 16:26
 */
@Data
public class DmDemandFileVO implements Serializable {

    /**
     * 任务id.
     * default value: null
     */
    @ApiModelProperty(name = "任务id")
    @Nullable
    private Integer taskId;


    /**
     * 项目id.
     */
    @ApiModelProperty(name = "项目ID")
    private Integer projectId;


    /**
     * 是否转换.
     */
    @ApiModelProperty(name = "是否转换")
    private boolean isCover;


}

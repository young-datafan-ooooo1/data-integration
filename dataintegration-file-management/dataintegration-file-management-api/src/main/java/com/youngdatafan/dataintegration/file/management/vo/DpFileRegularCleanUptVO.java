package com.youngdatafan.dataintegration.file.management.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 定时清理更新接受类.
 * @author songxiaolang
 * @date 2022-01-04 14:39:25
 */
@ApiModel("定时清理更新接受类")
@Data
public class DpFileRegularCleanUptVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文件保存范围天.
     */
    @NotNull(message = "文件保存范围不能为空")
    @ApiModelProperty("文件保存范围天")
    @Min(value = 30, message = "文件保存范围天数最小30")
    @Max(value = 720, message = "文件保存范围天数最大720")
    private Integer effectiveDays;

    /**
     * 是否同时按照业务设置保留时长.
     */
    @NotNull(message = "是否同时按照业务设置保留时长不能为空")
    @ApiModelProperty("是否同时按照业务设置保留时长1：是 0：否")
    private Integer isUseBusiness;

}

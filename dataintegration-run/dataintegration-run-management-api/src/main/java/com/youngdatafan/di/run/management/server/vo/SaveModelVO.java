package com.youngdatafan.di.run.management.server.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author gavin
 * @since 2020/3/6 5:15 下午
 */
@Data
@NoArgsConstructor
@ApiModel(description = "保存模型查询")
public class SaveModelVO {

    @ApiModelProperty(value = "模型名称")
    String modelName;

    @ApiModelProperty(value = "开始时间")
    Date startTime;

    @ApiModelProperty(value = "结束时间")
    Date endTime;

}

package com.youngdatafan.di.run.management.server.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("保存模型执行历史")
public class SavemodelHistoryDTO {
    /**
     * ID
     */

    private String id;

    /**
     * 模型id
     */
    private String modelId;

    /**
     * 模型名称
     */
    private String modelName;

    /**
     * 项目ID
     */
    private String projectId;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 步骤名称
     */
    private String stepName;

    /**
     * 状态（1运行中、2成功、3终止）
     */
    private String status;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 执行时间秒
     */
    private Integer execSecond;

    /**
     * 用户编号
     */
    private String userId;

    /**
     * 用户名称
     */
    private String userName;

}

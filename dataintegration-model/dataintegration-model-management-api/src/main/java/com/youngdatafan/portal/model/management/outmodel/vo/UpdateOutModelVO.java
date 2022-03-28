package com.youngdatafan.portal.model.management.outmodel.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 修改输出类模型对象.
 */
@Data
@ApiModel(description = "修改输出类模型对象")
public class UpdateOutModelVO {

    @NotNull
    @ApiModelProperty(value = "模型id")
    private String modelId;

    @ApiModelProperty(value = "模型名称")
    private String modelName;

    @ApiModelProperty(value = "项目id")
    private String projectId;

    @ApiModelProperty(value = "项目类型")
    private String projectType;

    @ApiModelProperty(value = "模型排序")
    private Integer modelOrder;

    @ApiModelProperty(value = "模型描述")
    private String description;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "创建人id")
    private String createUserId;

    @ApiModelProperty(value = "数据量总数")
    private String dataCount;

    @ApiModelProperty(value = "数据源id")
    private String datasourceId;

    @ApiModelProperty(value = "表名")
    private String tableName;

    @ApiModelProperty(value = "来源步骤名")
    private String stepName;

    @ApiModelProperty(value = "查询sql")
    private String querySql;

    @ApiModelProperty(value = "所属分组")
    private String groupId;

    @ApiModelProperty(value = "是否启用，T为启用，F为禁用或失效")
    private String enabled;

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    private List<OutModelMetaDataVO> outModelMetaDataVOS;

}

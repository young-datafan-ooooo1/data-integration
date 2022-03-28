package com.youngdatafan.portal.model.management.businessmodel.vo;

import com.youngdatafan.portal.model.management.common.entity.ModelFilterVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Data;

/**
 * 基础模型返回对象.
 */
@Data
@ApiModel(description = "基础模型返回对象")
public class UpdateBusinessModelVO {

    @ApiModelProperty(value = "模型id")
    private String modelName;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "所属分组")
    private String group;

    @ApiModelProperty(value = "基础模型组")
    private String basicModelGroupName;

    @ApiModelProperty(value = "基础模型")
    private String basicModelTableNameValue;

    @ApiModelProperty(value = "字段数量")
    private String columnCount;

    @ApiModelProperty(value = "排序")
    private String sortNum = "0";

    @ApiModelProperty(value = "备注")
    private String description;

    @ApiModelProperty(value = "是否有效")
    private String enabled;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "创建人")
    private String createUserName;

    @ApiModelProperty(value = "基础模型管理元数据")
    private List<BasicModelMetaDataCopyDTO> basicModelMetaDataCopyDTOS;

    @ApiModelProperty(value = "预过滤条件")
    private List<ModelFilterVO> modelFilterVOS;

}

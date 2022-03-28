package com.youngdatafan.portal.model.management.modelgrant.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Data;

/**
 * 新增用户授权信息表.
 */
@Data
@ApiModel("新增用户授权信息表")
public class UpdateModelGroupGrantUserVO {

    @ApiModelProperty(value = "用户授权id")
    private String userGrantId;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "是否有效")
    private String enabled;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "模型授权组")
    private List<String> modelGrantGroupNames;

    @ApiModelProperty(value = "模型名称")
    private List<String> modelNames;

}

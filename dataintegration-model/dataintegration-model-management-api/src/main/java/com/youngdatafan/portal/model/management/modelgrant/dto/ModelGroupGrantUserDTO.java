package com.youngdatafan.portal.model.management.modelgrant.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import java.util.List;
import lombok.Data;


/**
 * 用户授权信息.
 */
@Data
@ApiModel("用户授权信息")
public class ModelGroupGrantUserDTO {

    @ApiModelProperty(value = "用户授权id")
    private String userGrantId;

    @ApiModelProperty(value = "用户名称", required = true)
    private String userName;

    @ApiModelProperty(value = "用户名称", required = true)
    private String userId;

    @ApiModelProperty(value = "模型授权分组", required = true)
    private List<GroupIdAndGroupNamesDTO> grantGroups;

    @ApiModelProperty(value = "模型分组", required = true)
    private List<BusinessModelListDTO> models;

    @ApiModelProperty(value = "模型数量", required = true)
    private Integer modelCount = 0;

    @ApiModelProperty(value = "描述", required = true)
    private String description;

    @ApiModelProperty(value = "是否有效", required = true)
    private String enabled;

    @ApiModelProperty(value = "创建人", required = true)
    private String createUserName;

    @ApiModelProperty(value = "创建时间", required = true)
    private Date createTime;

}


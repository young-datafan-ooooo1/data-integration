package com.youngdatafan.portal.model.management.modelgrant.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Data;


/**
 * 查询授权组信息返回对象.
 */
@Data
@ApiModel("查询授权组信息返回对象")
public class ModelGrantGroupDTO {

    @ApiModelProperty(value = "授权组Id", required = true)

    private String groupId;

    @ApiModelProperty(value = "模型授权组名称", required = true)
    private String groupName;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "模型数量")
    private Integer modelCount = 0;

    @ApiModelProperty(value = "是否有效", required = true)
    private String enabled;

    @ApiModelProperty(value = "关联业务模型名称", required = true)
    private List<BusinessModelListDTO> businessModelListDTOS = new ArrayList<>();

    @ApiModelProperty(value = "创建时间", required = true)
    private Date createTime;

    @ApiModelProperty(value = "创建人", required = true)
    private String createUserName;
}

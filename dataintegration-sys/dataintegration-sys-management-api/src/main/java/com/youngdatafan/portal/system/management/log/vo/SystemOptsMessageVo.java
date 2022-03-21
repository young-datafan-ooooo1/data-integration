package com.youngdatafan.portal.system.management.log.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import lombok.Data;


/**
 * 系统操作消息表.
 */
@ApiModel(value = "com-dp-portal-system-management-log-entity-DpSystemOptsMessage")
@Data
public class SystemOptsMessageVo {


    /**
     * 操作用户id.
     */
    @ApiModelProperty(value = "操作用户id")
    @NotBlank
    private String opsUserId;

    /**
     * 操作用户名称.
     */
    @ApiModelProperty(value = "操作用户名称")
    @NotBlank
    private String opsUserName;

    /**
     * 操作关联用户id.
     */
    @ApiModelProperty(value = "操作关联用户id")
    @NotBlank
    private String opsToUserId;

    /**
     * 操作关联用户名称.
     */
    @ApiModelProperty(value = "操作关联用户名称")
    @NotBlank
    private String opsToUserName;

    /**
     * 操作类型（具体操作的中文描述）.
     */
    @ApiModelProperty(value = "操作类型（具体操作的中文描述）")
    @NotBlank
    private String opsType;

    /**
     * 操作内容id（项目id，图表id）.
     */
    @ApiModelProperty(value = "操作内容id（项目id，图表id）")
    private String opsItemId;

    /**
     * 操作内容名称.
     */
    @ApiModelProperty(value = "操作内容名称")
    private String opsItemName;

    /**
     * 操作内容类型（项目，图表等等）.
     */
    @ApiModelProperty(value = "操作内容类型（项目，图表等等）")
    private String opsItemType;

}

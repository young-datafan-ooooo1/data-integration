package com.youngdatafan.portal.system.management.log.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.Data;

/**
 * 系统操作消息表.
 */
@ApiModel(value = "com-dp-portal-system-management-log-entity-DpSystemOptsMessage")
@Data
public class DpSystemOptsMessage {
    /**
     * ID.
     */
    @ApiModelProperty(value = "ID")
    private String opsId;

    /**
     * 操作用户id.
     */
    @ApiModelProperty(value = "操作用户id")
    private String opsUserId;

    /**
     * 操作用户名称.
     */
    @ApiModelProperty(value = "操作用户名称")
    private String opsUserName;

    /**
     * 操作关联用户id.
     */
    @ApiModelProperty(value = "操作关联用户id")
    private String opsToUserId;

    /**
     * 操作关联用户名称.
     */
    @ApiModelProperty(value = "操作关联用户名称")
    private String opsToUserName;

    /**
     * 操作类型（具体操作的中文描述）.
     */
    @ApiModelProperty(value = "操作类型（具体操作的中文描述）")
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

    /**
     * 创建时间.
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 级别.
     */
    @ApiModelProperty(value = "级别")
    private Integer opsLevel;
}

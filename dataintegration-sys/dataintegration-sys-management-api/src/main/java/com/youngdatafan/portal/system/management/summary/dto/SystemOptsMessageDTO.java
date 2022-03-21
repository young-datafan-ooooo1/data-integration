package com.youngdatafan.portal.system.management.summary.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.Data;


/**
 * 系统操作消息表.
 */
@ApiModel(value = "com-dp-portal-system-management-log-entity-DpSystemOptsMessage")
@Data
public class SystemOptsMessageDTO {
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

    /**
     * 时间跨度.
     */
    @ApiModelProperty(value = "时间跨度")
    private String timeMessage;

    /**
     * getTimeMessage.
     *
     * @return String
     */
    public String getTimeMessage() {
        long time = System.currentTimeMillis() - createTime.getTime();
        if (time / 1000 < 1) {
            return "刚刚";
        } else if (time / 1000 >= 1 && time / 1000 < 60) {
            return time / 1000 + "秒前";
        } else if (time / (1000 * 60) >= 1 && time / (1000 * 60) < 60) {
            return time / (1000 * 60) + "分钟前";
        } else if (time / (1000 * 60 * 60) >= 1 && time / (1000 * 60 * 60) < 24) {
            return time / (1000 * 60 * 60) + "小时前";
        } else if (time / (1000 * 60 * 60 * 24) >= 1 && time / (1000 * 60 * 60 * 24) < 7) {
            return time / (1000 * 60 * 60 * 24) + "天前";
        } else if (time / (1000 * 60 * 60 * 24 * 7) >= 1 && time / (1000 * 60 * 60 * 24 * 7) < 5) {
            return time / (1000 * 60 * 60 * 24 * 7) + "周前";
        } else {
            return time / (1000 * 60 * 60 * 24 * 30) + "月前";
        }

    }
}

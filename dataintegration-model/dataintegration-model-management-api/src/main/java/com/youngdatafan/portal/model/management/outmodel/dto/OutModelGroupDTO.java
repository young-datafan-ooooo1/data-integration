package com.youngdatafan.portal.model.management.outmodel.dto;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * OutModelGroupDTO.
 */
@Data
public class OutModelGroupDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String groupId;

    private String groupName;

    private String describe;

    private String groupType;

    private Integer groupOrder;

    private String enabled;

    private Date createTime;

    private Date updateTime;

    private String createUserId;

}

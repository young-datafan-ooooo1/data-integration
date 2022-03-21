package com.youngdatafan.portal.system.management.gateway.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import java.util.StringJoiner;
import lombok.Getter;
import lombok.Setter;

/**
 * 路由规则对象.
 */
@Setter
@Getter
@ApiModel(description = "路由规则对象")
public class DpGatewayRouteDTO {

    @ApiModelProperty(value = "路由id", required = true)
    private String id;

    @ApiModelProperty(value = "路由转发地址", required = true)
    private String uri;

    @ApiModelProperty(value = "排序")
    private Integer order;

    @ApiModelProperty(value = "预处理规则")
    private String predicates;

    @ApiModelProperty(value = "过滤器")
    private String filters;

    @ApiModelProperty(value = "元数据，超时等")
    private String metadata;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "状态（1启用、2失效）")
    private Integer status;

    @Override
    public String toString() {
        return new StringJoiner(", ", DpGatewayRouteDTO.class.getSimpleName() + "[", "]")
            .add("id='" + id + "'")
            .add("uri='" + uri + "'")
            .add("order=" + order)
            .add("predicates='" + predicates + "'")
            .add("filters='" + filters + "'")
            .add("metadata='" + metadata + "'")
            .add("createTime=" + createTime)
            .add("updateTime=" + updateTime)
            .add("status=" + status)
            .toString();
    }
}

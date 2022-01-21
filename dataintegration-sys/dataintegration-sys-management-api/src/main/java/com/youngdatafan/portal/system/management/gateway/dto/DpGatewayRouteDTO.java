package com.youngdatafan.portal.system.management.gateway.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.StringJoiner;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getPredicates() {
        return predicates;
    }

    public void setPredicates(String predicates) {
        this.predicates = predicates;
    }

    public String getFilters() {
        return filters;
    }

    public void setFilters(String filters) {
        this.filters = filters;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

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

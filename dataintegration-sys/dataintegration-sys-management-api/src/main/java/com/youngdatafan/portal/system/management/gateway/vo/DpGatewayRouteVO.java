package com.youngdatafan.portal.system.management.gateway.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.StringJoiner;

@ApiModel(description = "路由规则对象")
public class DpGatewayRouteVO {

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

    @Override
    public String toString() {
        return new StringJoiner(", ", DpGatewayRouteVO.class.getSimpleName() + "[", "]")
                .add("id='" + id + "'")
                .add("uri='" + uri + "'")
                .add("order=" + order)
                .add("predicates='" + predicates + "'")
                .add("filters='" + filters + "'")
                .add("metadata='" + metadata + "'")
                .toString();
    }
}

package com.youngdatafan.portal.system.management.gateway.mapper;

import com.youngdatafan.portal.system.management.gateway.dto.DpGatewayRateLimitDTO;
import com.youngdatafan.portal.system.management.gateway.entity.DpGatewayRatelimit;

import java.util.List;

public interface DpGatewayRatelimitMapper {
    int deleteByPrimaryKey(String id);

    int insert(DpGatewayRatelimit record);

    int insertSelective(DpGatewayRatelimit record);

    DpGatewayRatelimit selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(DpGatewayRatelimit record);

    int updateByPrimaryKey(DpGatewayRatelimit record);

    List<DpGatewayRateLimitDTO> selectAll();

    DpGatewayRatelimit selectByResource(String resource);
}

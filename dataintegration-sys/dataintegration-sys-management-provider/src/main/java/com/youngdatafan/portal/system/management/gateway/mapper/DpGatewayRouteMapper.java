package com.youngdatafan.portal.system.management.gateway.mapper;

import com.youngdatafan.portal.system.management.gateway.dto.DpGatewayRouteDTO;
import com.youngdatafan.portal.system.management.gateway.entity.DpGatewayRoute;

import java.util.List;

public interface DpGatewayRouteMapper {
    int deleteByPrimaryKey(String id);

    int insert(DpGatewayRoute record);

    int insertSelective(DpGatewayRoute record);

    DpGatewayRoute selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(DpGatewayRoute record);

    int updateByPrimaryKey(DpGatewayRoute record);

    List<DpGatewayRouteDTO> selectAll();
}

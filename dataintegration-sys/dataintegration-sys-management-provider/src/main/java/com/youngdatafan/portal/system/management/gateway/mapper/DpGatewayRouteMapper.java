package com.youngdatafan.portal.system.management.gateway.mapper;

import com.youngdatafan.portal.system.management.gateway.dto.DpGatewayRouteDTO;
import com.youngdatafan.portal.system.management.gateway.entity.DpGatewayRoute;
import java.util.List;

/**
 * DpGatewayRouteMapper.
 */
public interface DpGatewayRouteMapper {
    /**
     * deleteByPrimaryKey.
     *
     * @param id id
     * @return int
     */
    int deleteByPrimaryKey(String id);

    /**
     * insert.
     *
     * @param record record
     * @return int
     */
    int insert(DpGatewayRoute record);

    /**
     * insertSelective.
     *
     * @param record record
     * @return int
     */
    int insertSelective(DpGatewayRoute record);

    /**
     * selectByPrimaryKey.
     *
     * @param id id
     * @return selectByPrimaryKey
     */
    DpGatewayRoute selectByPrimaryKey(String id);

    /**
     * updateByPrimaryKeySelective.
     *
     * @param record record
     * @return int
     */
    int updateByPrimaryKeySelective(DpGatewayRoute record);

    /**
     * updateByPrimaryKey.
     *
     * @param record record
     * @return int
     */
    int updateByPrimaryKey(DpGatewayRoute record);

    /**
     * selectAll.
     *
     * @return list
     */
    List<DpGatewayRouteDTO> selectAll();
}


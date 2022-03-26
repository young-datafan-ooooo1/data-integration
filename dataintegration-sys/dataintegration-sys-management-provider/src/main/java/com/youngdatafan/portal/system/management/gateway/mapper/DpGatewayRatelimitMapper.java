package com.youngdatafan.portal.system.management.gateway.mapper;

import com.youngdatafan.portal.system.management.gateway.dto.DpGatewayRateLimitDTO;
import com.youngdatafan.portal.system.management.gateway.entity.DpGatewayRatelimit;
import java.util.List;

/**
 * DpGatewayRatelimitMapper.
 */
public interface DpGatewayRatelimitMapper {
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
    int insert(DpGatewayRatelimit record);

    /**
     * insertSelective.
     *
     * @param record record
     * @return int
     */
    int insertSelective(DpGatewayRatelimit record);

    /**
     * selectByPrimaryKey.
     *
     * @param id id
     * @return DpGatewayRatelimit
     */
    DpGatewayRatelimit selectByPrimaryKey(String id);

    /**
     * updateByPrimaryKeySelective.
     *
     * @param record record
     * @return int
     */
    int updateByPrimaryKeySelective(DpGatewayRatelimit record);

    /**
     * updateByPrimaryKey.
     *
     * @param record record
     * @return int
     */
    int updateByPrimaryKey(DpGatewayRatelimit record);

    /**
     * selectAll.
     *
     * @return List
     */
    List<DpGatewayRateLimitDTO> selectAll();

    /**
     * selectByResource.
     *
     * @param resource resource
     * @return DpGatewayRatelimit
     */
    DpGatewayRatelimit selectByResource(String resource);
}

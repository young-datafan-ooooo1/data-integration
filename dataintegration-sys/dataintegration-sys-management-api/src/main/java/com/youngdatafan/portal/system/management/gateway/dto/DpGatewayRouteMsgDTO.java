package com.youngdatafan.portal.system.management.gateway.dto;

import com.youngdatafan.portal.system.management.gateway.MsgType;
import lombok.Getter;
import lombok.Setter;

/**
 * 网关路由消息对象.
 */
@Setter
@Getter
public class DpGatewayRouteMsgDTO {

    private MsgType msgType;

    private DpGatewayRouteDTO dpGatewayRouteDTO;

    public DpGatewayRouteMsgDTO() {
    }

    public DpGatewayRouteMsgDTO(MsgType msgType, DpGatewayRouteDTO dpGatewayRouteDTO) {
        this.msgType = msgType;
        this.dpGatewayRouteDTO = dpGatewayRouteDTO;
    }

}

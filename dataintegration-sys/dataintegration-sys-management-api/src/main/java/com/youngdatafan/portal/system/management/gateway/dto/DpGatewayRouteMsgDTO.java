package com.youngdatafan.portal.system.management.gateway.dto;

import com.youngdatafan.portal.system.management.gateway.MsgType;

/**
 * 网关路由消息对象
 */
public class DpGatewayRouteMsgDTO {

    private MsgType msgType;

    private DpGatewayRouteDTO dpGatewayRouteDTO;

    public DpGatewayRouteMsgDTO() {
    }

    public DpGatewayRouteMsgDTO(MsgType msgType, DpGatewayRouteDTO dpGatewayRouteDTO) {
        this.msgType = msgType;
        this.dpGatewayRouteDTO = dpGatewayRouteDTO;
    }

    public MsgType getMsgType() {
        return msgType;
    }

    public void setMsgType(MsgType msgType) {
        this.msgType = msgType;
    }

    public DpGatewayRouteDTO getDpGatewayRouteDTO() {
        return dpGatewayRouteDTO;
    }

    public void setDpGatewayRouteDTO(DpGatewayRouteDTO dpGatewayRouteDTO) {
        this.dpGatewayRouteDTO = dpGatewayRouteDTO;
    }
}

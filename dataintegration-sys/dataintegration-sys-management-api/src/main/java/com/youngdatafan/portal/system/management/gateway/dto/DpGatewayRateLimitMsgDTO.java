package com.youngdatafan.portal.system.management.gateway.dto;

import com.youngdatafan.portal.system.management.gateway.MsgType;

/**
 * 网关限流消息对象
 */
public class DpGatewayRateLimitMsgDTO {

    private MsgType msgType;

    private DpGatewayRateLimitDTO dpGatewayRateLimitDTO;

    public DpGatewayRateLimitMsgDTO() {
    }

    public DpGatewayRateLimitMsgDTO(MsgType msgType, DpGatewayRateLimitDTO dpGatewayRateLimitDTO) {
        this.msgType = msgType;
        this.dpGatewayRateLimitDTO = dpGatewayRateLimitDTO;
    }

    public MsgType getMsgType() {
        return msgType;
    }

    public void setMsgType(MsgType msgType) {
        this.msgType = msgType;
    }

    public DpGatewayRateLimitDTO getDpGatewayRateLimitDTO() {
        return dpGatewayRateLimitDTO;
    }

    public void setDpGatewayRateLimitDTO(DpGatewayRateLimitDTO dpGatewayRateLimitDTO) {
        this.dpGatewayRateLimitDTO = dpGatewayRateLimitDTO;
    }
}

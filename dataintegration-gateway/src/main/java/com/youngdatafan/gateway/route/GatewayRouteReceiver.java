package com.youngdatafan.gateway.route;


import com.youngdatafan.dataintegration.core.lock.CacheLock;
import com.youngdatafan.dataintegration.core.util.JsonUtils;
import com.youngdatafan.gateway.core.util.Constans;
import com.youngdatafan.portal.system.management.gateway.dto.DpGatewayRouteDTO;
import com.youngdatafan.portal.system.management.gateway.dto.DpGatewayRouteMsgDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author gavin
 * @since 2020/6/13 2:48 下午
 */
@Service
public class GatewayRouteReceiver {
    private final Logger log = LoggerFactory.getLogger(GatewayRouteReceiver.class);

    private final DbRouteDefinitionRepository dbRouteDefinitionRepository;

    @Autowired
    public GatewayRouteReceiver(DbRouteDefinitionRepository dbRouteDefinitionRepository) {
        this.dbRouteDefinitionRepository = dbRouteDefinitionRepository;
    }

    /**
     * 接收消息队列里的消息
     * @param message 接收到的消息
     */
    @CacheLock(key = Constans.LOCK_GATEWAY_ROUTE)
    public void receiveMessage(String message) {
        log.info("接收到新规则: {}", message);

        try {
            final DpGatewayRouteMsgDTO dpGatewayRouteMsgDTO = JsonUtils.parseObject(message, DpGatewayRouteMsgDTO.class);
            final DpGatewayRouteDTO dpGatewayRouteDTO = dpGatewayRouteMsgDTO.getDpGatewayRouteDTO();
            if (dpGatewayRouteDTO.getStatus() == null || dpGatewayRouteDTO.getStatus() == 0) {
                log.info("规则未启用: {}", message);
                return;
            }

            switch (dpGatewayRouteMsgDTO.getMsgType()) {
                case UPSET:
                    dbRouteDefinitionRepository.save(GatewayRouteSyncTask.dtoToRoute(dpGatewayRouteDTO));
                    break;
                case DELETE:
                    dbRouteDefinitionRepository.delete(dpGatewayRouteDTO.getId());
                    break;
                default:
                    log.warn("规则类型未定义");
                    break;
            }

        } catch (Exception e) {
            log.error("规则解析失败 {}", message);
        }
    }
}

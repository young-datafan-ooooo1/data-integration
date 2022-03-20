package com.youngdatafan.gateway.ratelimit;


import com.youngdatafan.dataintegration.core.lock.CacheLock;
import com.youngdatafan.dataintegration.core.util.JsonUtils;
import com.youngdatafan.gateway.core.util.Constans;
import com.youngdatafan.portal.system.management.gateway.dto.DpGatewayRateLimitDTO;
import com.youngdatafan.portal.system.management.gateway.dto.DpGatewayRateLimitMsgDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author gavin
 * @since 2020/6/13 2:48 下午
 */
@Service
public class GatewayRateLimitReceiver {
    private final Logger log = LoggerFactory.getLogger(GatewayRateLimitReceiver.class);

    private final DbRateLimitRepository rateLimitRepository;

    @Autowired
    public GatewayRateLimitReceiver(DbRateLimitRepository rateLimitRepository) {
        this.rateLimitRepository = rateLimitRepository;
    }

    /**
     * 接收消息队列里的消息
     * @param message 接收到的消息
     */
    @CacheLock(key = Constans.LOCK_GATEWAY_RATELIMIT)
    public void receiveMessage(String message) {
        log.info("接收到新规则: {}", message);

        try {
            final DpGatewayRateLimitMsgDTO dpGatewayRateLimitMsgDTO = JsonUtils.parseObject(message, DpGatewayRateLimitMsgDTO.class);
            final DpGatewayRateLimitDTO dpGatewayRateLimitDTO = dpGatewayRateLimitMsgDTO.getDpGatewayRateLimitDTO();
            if (dpGatewayRateLimitDTO.getStatus() == null || dpGatewayRateLimitDTO.getStatus() == 0) {
                log.info("规则未启用: {}", message);
                return;
            }

            switch (dpGatewayRateLimitMsgDTO.getMsgType()) {
                case UPSET:
                    rateLimitRepository.save(GatewayRateLimitSyncTask.toRateLimitConfig(dpGatewayRateLimitDTO));
                    break;
                case DELETE:
                    rateLimitRepository.delete(dpGatewayRateLimitDTO.getResource());
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

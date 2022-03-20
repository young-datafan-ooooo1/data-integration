package com.youngdatafan.di.run.management.server.websocket;

import com.youngdatafan.dataintegration.core.model.Result;

/**
 * 项目执行回调
 *
 * @author gavin
 * @since 2020/2/19 5:25 下午
 */
public interface ProjectExecuteCallback {

    /**
     * 执行器轮询调用此方法，传递执行状态
     *
     * @param destination      目标地址
     * @param webSocketMessage websocket消息
     */
    void onMessage(String destination, Result webSocketMessage);

}

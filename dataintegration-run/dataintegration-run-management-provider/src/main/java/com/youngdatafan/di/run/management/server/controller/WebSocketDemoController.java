package com.youngdatafan.di.run.management.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author gavin
 * @since 2020/2/13 10:27 上午
 */
@Controller
@RequestMapping("/demo")
public class WebSocketDemoController {

    @RequestMapping("/websocketDemo")
    public String websocketDemo() {
        return "websocket";
    }

}

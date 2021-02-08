package com.xuxd.baishun.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by dong on 2020/4/2.
 */
@RestController
@RequestMapping("/system")
public class SystemController extends BaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SystemController.class);

    @RequestMapping(path = "/active", method = RequestMethod.HEAD)
    public String active() {
        return "0";
    }

    @GetMapping("/exit")
    public void exit() {
        LOGGER.warn("收到一条退出请求，系统即将退出");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            LOGGER.error("", e);
        }
        System.exit(0);
    }
}

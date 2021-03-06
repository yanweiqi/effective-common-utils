package com.effective.common.http.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by yanweiqi on 2018/11/30.
 */
@Controller
public class SystemInfoController {

    /**
     * @param isOpen
     * @return
     */
    @RequestMapping(value = "/status")
    @ResponseBody
    public Object status(@RequestParam(required = false) String isOpen) {
        System.out.println(isOpen);
        return "OK";
    }

    @RequestMapping(value = "/ready")
    @ResponseBody
    public Object ready() {
        return "OK";
    }


    @RequestMapping(value = "/ready/timeout")
    @ResponseBody
    public Object readyTimeOut() throws InterruptedException {
        long start = System.currentTimeMillis();
        Thread.sleep(1000);
        long end = System.currentTimeMillis() - start;
        System.out.println("执行："+ end);
        return "OK";
    }
}

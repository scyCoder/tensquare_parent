package com.tensquare.base.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: tensquare68
 * @description:
 **/
@RequestMapping("/test")
@CrossOrigin
@RestController
@RefreshScope //用户刷新自定义配置文件
public class TestController {

    @Value("${ip.info}")
    private String ip;


    @GetMapping("/ipinfo")
    public String getIpInfo(){
        return ip;
    }
}

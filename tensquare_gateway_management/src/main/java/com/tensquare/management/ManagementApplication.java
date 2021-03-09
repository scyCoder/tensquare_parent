package com.tensquare.management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import util.JwtUtil;

/**
 * @program: tensquare68
 * @description:
 **/
@SpringBootApplication
@EnableEurekaClient
@EnableZuulProxy
public class ManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(ManagementApplication.class);
    }

    @Bean
    public JwtUtil jwtUtil(){
        return new JwtUtil();
    }
}

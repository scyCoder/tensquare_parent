package com.tensquare.friend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import util.JwtUtil;

/**
 * @program: tensquare68
 * @description:
 **/
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class FriendAppliaction {
    public static void main(String[] args) {
        SpringApplication.run(FriendAppliaction.class);
    }

    @Bean
    public JwtUtil jwtUtil(){
        return new JwtUtil();
    }
}

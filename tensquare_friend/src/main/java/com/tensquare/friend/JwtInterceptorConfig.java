package com.tensquare.friend;

import com.tensquare.friend.interceptors.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @program: tensquare68
 * @description:
 **/
@Configuration
public class JwtInterceptorConfig extends WebMvcConfigurationSupport{

    @Autowired
    private JwtInterceptor jwtInterceptor;
    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor);
    }
}

package com.tensquare.user.interceptors;

import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: tensquare68
 * @description: 验证jwt拦截器
 **/
@Component
public class JwtInterceptor extends HandlerInterceptorAdapter{

    @Autowired
    private JwtUtil jwtUtil;
    /**
     * 当请求目标controller方法之前执行
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //将用户/管理员登陆操作放行
        System.out.println("经过拦截器。。。。");
        String header = request.getHeader("Authorization");
        if(StringUtils.isNotBlank(header) && header.startsWith("Bearer ")){
            String token = header.substring(7);
            Claims claims = jwtUtil.parseJwt(token);
            if(claims!=null){
                String role = (String) claims.get("role");
                if("admin".equals(role)){
                    request.setAttribute("admin_claims", claims);
                }
                if("user".equals(role)){
                    request.setAttribute("user_claims", claims);
                }
            }
        }
        return true;
    }
}

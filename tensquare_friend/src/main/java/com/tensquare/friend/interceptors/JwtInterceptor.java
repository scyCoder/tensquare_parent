package com.tensquare.friend.interceptors;

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
 * @description:
 **/
@Component
public class JwtInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private JwtUtil jwtUtil;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String header = request.getHeader("Authorization");
        if(StringUtils.isNotBlank(header) && header.startsWith("Bearer ")){
            String token = header.substring(7);
            //解析token
            Claims claims = jwtUtil.parseJwt(token);
            if(claims!=null){
                if("admin".equals(claims.get("role"))){
                    request.setAttribute("admin_claims", claims);
                }
                if("user".equals(claims.get("role"))){
                    request.setAttribute("user_claims", claims);
                }
            }
        }
        return true;
    }
}

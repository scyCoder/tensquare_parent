package com.tensquare.management.filter;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * @program: tensquare68
 * @description: 验证token是否有效
 **/
@Component
public class JwtFilter extends ZuulFilter{
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Object run() throws ZuulException {
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();
        String header = request.getHeader("Authorization");
        if(!StringUtils.isEmpty(header)&&header.startsWith("Bearer ")){
            //判断用户提交token是否有效，角色必须是管理员
            String token = header.substring(7);
            Claims claims = jwtUtil.parseJwt(token);
            if(claims!=null && "admin".equals(claims.get("role"))){
                //如果条件满足，将请求转发到目标微服务
                currentContext.addZuulRequestHeader("Authorization", header);
                return null;
            }
        }

        Result result = new Result(false, StatusCode.ACCESS_ERROR, "无权操作");

        //如果条件不满足，返回“权限不足”提示信息给前端
        currentContext.setSendZuulResponse(false); //停止转发请求
        currentContext.setResponseStatusCode(401);
        //设置响应数据
        currentContext.setResponseBody(JSON.toJSONString(result));
        currentContext.getResponse().setContentType("text/json;charset=utf-8");
        return null;
    }
}

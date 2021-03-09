package com.tensquare.web.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @program: tensquare68
 * @description: 自定义过滤器
 **/
@Component
public class JwtFilter extends ZuulFilter{

    /**
     * 过滤器类型，决定过滤器逻辑什么时候执行
     * 取值：
     * pre: 前置过滤器,当请求目标微服务之前执行
     * route：在路由请求时候被调用
     * post: 请求目标微服务或者出现异常都要执行
     * error: 请求目标微服务出现异常执行
     */
    public String filterType() {
        return "pre";
    }

    /**
     * 指定过滤器执行顺序  如果有多个过滤器数字越小 优先执行
     * @return
     */
    public int filterOrder() {
        return 0;
    }

    /**
     * 是否经过过滤器逻辑
     * @return
     */
    public boolean shouldFilter() {
        //获取当前请求url，根据url判断是否经过过滤器逻辑
//        RequestContext currentContext = RequestContext.getCurrentContext();
//        HttpServletRequest request = currentContext.getRequest();
//        String url = request.getRequestURL().toString();
//        if(url==''){
//            return false;
//        }
        return true;
    }

    /**
     * 过滤器执行逻辑
     * @return 没有任何意义
     * @throws ZuulException
     */
    public Object run() throws ZuulException {
        System.out.println("经过网站前台网关自定义过滤器");
        //将网站前台用户提交请求头信息转发到目标微服务中
        //1、获取到request对象获取请求头信息
        //全局上下文请求对象
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();
        String header = request.getHeader("Authorization");
        //2、将请求头信息进行转发
        currentContext.addZuulRequestHeader("Authorization", header);

        return null;
    }
}

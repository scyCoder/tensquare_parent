package com.tensquare.qa.clients;

import com.tensquare.qa.clients.impl.LabelClientImpl;
import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 服务调用，调用基础微服务
 */
@FeignClient(value = "tensquare-base", fallback = LabelClientImpl.class) //指定调用目标微服务名称（跟微服务配置文件中应用名称一致）
public interface LabelClient {

    /**
     * 调用目标restApi方法
     * 注意：返回结果必须有无参构造 @PathVariable设置参数名称
     * @param labelId
     * @return
     */
    @GetMapping("/label/{labelId}")
    public Result findById(@PathVariable("labelId") String labelId);

}

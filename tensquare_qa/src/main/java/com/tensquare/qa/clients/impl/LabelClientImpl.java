package com.tensquare.qa.clients.impl;

import com.tensquare.qa.clients.LabelClient;
import entity.Result;
import entity.StatusCode;
import org.springframework.stereotype.Component;

/**
 * @program: tensquare68
 * @description: 如果目标微服务不可用调用当前微服务中实现类方法
 **/
@Component
public class LabelClientImpl implements LabelClient {
    @Override
    public Result findById(String labelId) {
        return new Result(false, StatusCode.ERROR, "服务暂时不可用，请稍后再试！");
    }
}

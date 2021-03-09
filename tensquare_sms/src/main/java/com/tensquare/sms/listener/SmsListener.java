package com.tensquare.sms.listener;

import com.tensquare.sms.util.SmsUtil;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @program: tensquare68
 * @description:
 **/
@Component
@RabbitListener(queues = "tensquare-sms")
public class SmsListener {

    @Autowired
    private SmsUtil smsUtil;

    @RabbitHandler
    public void sendSms(Map<String, String> map){
        String mobile = map.get("mobile");
        String templateCode = map.get("templateCode");
        String tempateParam = map.get("tempateParam");

        System.out.println(mobile+" --- "+templateCode);
        smsUtil.sendSms(mobile, templateCode, tempateParam);
    }
}

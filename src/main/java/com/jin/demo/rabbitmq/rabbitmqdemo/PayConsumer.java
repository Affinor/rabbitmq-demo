package com.jin.demo.rabbitmq.rabbitmqdemo;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class PayConsumer {

    public static volatile Boolean payStatus = false;

    @RabbitListener(queues = "demoQueue")
    public void demoQueue(String message) {
        PayConsumer.payStatus = true;
        System.out.println(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()) +",接收到消息：" + message+" ,十秒内没有支付,订单超时！！");
    }

}

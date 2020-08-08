package com.jin.demo.rabbitmq.rabbitmqdemo;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class PayController {

    @Autowired
    private AmqpTemplate rabbitTemplate;


    @RequestMapping("/pay/sendMessage")
    @ResponseBody
    public ModelAndView sendMessage(String msg) {

        rabbitTemplate.convertAndSend("demoTtlExchange", "demoTtlRoutes", msg, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                //设置10s过期，过期转发到指定路由
                message.getMessageProperties().setExpiration("10000");

                return message;
            }
        });
        ModelAndView mav = new ModelAndView();
        mav.setViewName("static/pay.html");
        PayConsumer.payStatus=false;
        return mav;
    }

    @RequestMapping("/pay/zhifu")
    @ResponseBody
    public ModelAndView zhifu(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("static/timeout.html");
        if (!PayConsumer.payStatus) {
            mav.setViewName("static/success.html");
        }
        return mav;
    }

}

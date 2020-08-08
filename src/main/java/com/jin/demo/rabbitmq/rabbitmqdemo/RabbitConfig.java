package com.jin.demo.rabbitmq.rabbitmqdemo;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitConfig {

    /**
     *
     * 创建实际消费队列绑定的交换机
     */
    @Bean
    public DirectExchange  demoExchange(){


        return new DirectExchange("demoExchange",true,false);
    }

    /**
     *
     * 创建延迟队列（死信队列）绑定的交换机
     */
    @Bean
    public DirectExchange  demoTtlExchange(){

        return new DirectExchange("demoTtlExchange",true,false);
    }


    /**
     *
     * 创建实际消费队列
     */
    @Bean
    public Queue demoQueue(){

        return new Queue("demoQueue",true);
    }


    /**
     *
     * 创建延迟队列（死信队列）
     */
    @Bean
    public Queue demoTtlQueue(){
        Map<String, Object> arguments=new HashMap<>();
        arguments.put("x-dead-letter-exchange", "demoExchange");
        arguments.put("x-dead-letter-routing-key", "demoRoutes");
        return new Queue("demoTtlQueue",true,false, false, arguments);
    }


    /**
     * 绑定实际消费队列到交换机
     */

    @Bean
    public Binding demoBinding(){

        return new Binding("demoQueue", Binding.DestinationType.QUEUE,"demoExchange","demoRoutes",null);
    }

    /**
     * 绑定延迟队列（死信队列）到交换机
     */
    @Bean
    public Binding demoTtlBinding(){


        return new Binding("demoTtlQueue", Binding.DestinationType.QUEUE,"demoTtlExchange","demoTtlRoutes",null);
    }

}

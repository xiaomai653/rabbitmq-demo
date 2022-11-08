package com.springboot.config;


import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;


/**
 * 死信队列---需两个交换机两个队列
 * <p>
 * 延时交换机---需安装插件
 */

@Configuration
public class TTLRabbitConfig {

    /**
     * 死信交换机
     */
    @Bean
    public DirectExchange dlkExchange() {
        return new DirectExchange("dlkExchange", true, false);
    }

    @Bean
    public Queue dlkQueue() {
        return new Queue("dlkQueue", true, false, false);
    }

    @Bean
    public Binding dlkBind() {
        return BindingBuilder.bind(dlkQueue()).to(dlkExchange()).with("dlk.routeKey");
    }

    /**
     * 业务交换机
     */
    @Bean
    public DirectExchange demoExchange() {
        return new DirectExchange("demoExchange", true, false);
    }

    @Bean
    public Queue demoQueue() {
        //只需要在声明业务队列时添加x-dead-letter-exchange，值为死信交换机
        Map<String, Object> map = new HashMap<>(1);
        map.put("x-dead-letter-exchange", "dlkExchange");
        //该参数x-dead-letter-routing-key可以修改该死信的路由key，不设置则使用原消息的路由key
        map.put("x-dead-letter-routing-key", "dlk.routeKey");
        return new Queue("demoQueue", true, false, false, map);
    }

    @Bean
    public Binding demoBind() {
        return BindingBuilder.bind(demoQueue()).to(demoExchange()).with("demo.routeKey");
    }

    /**
     * 延迟插件使用
     * 1、声明一个类型为x-delayed-message的交换机
     * 2、参数添加一个x-delayed-type值为交换机的类型用于路由key的映射
     */
    @Bean
    public CustomExchange dmpExchange() {
        Map<String, Object> arguments = new HashMap<>(1);
        arguments.put("x-delayed-type", "direct");
        return new CustomExchange("dmpExchange", "x-delayed-message", true, false, arguments);
    }

    @Bean
    public Queue dmpQueue() {
        return new Queue("dmpQueue", true, false, false);
    }

    @Bean
    public Binding dmpBind() {
        return BindingBuilder.bind(dmpQueue()).to(dmpExchange()).with("dmp.routeKey").noargs();
    }


}

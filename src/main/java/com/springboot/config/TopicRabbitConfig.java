package com.springboot.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 路由匹配
 */
@Configuration
public class TopicRabbitConfig {

    /**
     * 交换机
     */
    @Bean
    public TopicExchange myTopicExchange() {
        return new TopicExchange("myTopicExchange", true, false);
    }

    /**
     * 队列
     */
    @Bean
    public Queue myTopicQueue_01() {
        return new Queue("myTopicQueue_01", true);
    }

    @Bean
    public Queue myTopicQueue_02() {
        return new Queue("myTopicQueue_02", true);
    }

    @Bean
    public Queue myTopicQueue_03() {
        return new Queue("myTopicQueue_03", true);
    }

    @Bean
    public Queue myTopicQueue_04() {
        return new Queue("myTopicQueue_04", true);
    }

    /**
     * 绑定路由键为user.#
     */
    @Bean
    public Binding binding_01() {
        return BindingBuilder.bind(myTopicQueue_01()).to(myTopicExchange()).with("user.#");
    }

    /**
     * 绑定路由键为#.new
     */
    @Bean
    public Binding binding_02() {
        return BindingBuilder.bind(myTopicQueue_02()).to(myTopicExchange()).with("#.new");
    }

    /**
     * 绑定路由键为#.weather
     */
    @Bean
    public Binding binding_03() {
        return BindingBuilder.bind(myTopicQueue_03()).to(myTopicExchange()).with("#.weather");
    }

    /**
     * 绑定路由键为europe.#
     */
    @Bean
    public Binding binding_04() {
        return BindingBuilder.bind(myTopicQueue_04()).to(myTopicExchange()).with("europe.#");
    }

}
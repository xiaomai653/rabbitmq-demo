package com.springboot.config;

import com.springboot.listener.OrderListener;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 订单队列
 */
@Configuration
public class OrderRabbitConfig {

    /**
     * channel链接工厂
     */
    @Autowired
    private CachingConnectionFactory connectionFactory;

    /**
     * 监听器容器配置
     */
    @Autowired
    private SimpleRabbitListenerContainerFactoryConfigurer factoryConfigurer;

    /**
     * 注入订单对列消费监听器
     */
    @Autowired
    private OrderListener orderListener;

    /**
     * 交换机
     */
    @Bean
    public TopicExchange orderTopicExchange() {
        return new TopicExchange("orderTopicExchange", true, false);
    }

    /**
     * 队列
     */
    @Bean
    public Queue orderTopicQueue() {
        return new Queue("orderTopicQueue", true);
    }

    /**
     * 绑定路由键为order.#
     */
    @Bean
    public Binding order() {
        return BindingBuilder.bind(orderTopicQueue()).to(orderTopicExchange()).with("order.#");
    }

    /**
     * 声明订单队列监听器配置容器
     *
     * @return
     */
    @Bean
    public SimpleMessageListenerContainer orderListenerContainer() {
        //创建监听器容器工厂
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        //将配置信息和链接信息赋给容器工厂
        factoryConfigurer.configure(factory, connectionFactory);
        //容器工厂创建监听器容器
        SimpleMessageListenerContainer container = factory.createListenerContainer();
        //指定监听器
        container.setMessageListener(orderListener);
        //指定监听器监听的队列
        container.setQueues(orderTopicQueue());
        return container;
    }


}
package com.springboot.config;

import com.springboot.listener.MyAckReceiver;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * rabbitmq 队列监听配置
 */
@Configuration
public class MessageListenerConfig {

    @Autowired
    private CachingConnectionFactory cachingConnectionFactory;

    @Autowired
    private MyAckReceiver myAckReceiver;

    @Bean
    public SimpleMessageListenerContainer simpleMessageListenerContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(cachingConnectionFactory);

        // 监听队列名
        container.setQueueNames("myDirectQueue", "fanoutQueueA");
        // 当前消费者数量
        container.setConcurrentConsumers(1);
        // 最大消费者数量
        container.setMaxConcurrentConsumers(1);
        // 手动确认
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        // 设置监听器
        container.setMessageListener(myAckReceiver);

        return container;
    }
}
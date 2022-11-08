package com.springboot.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@Slf4j
public class RabbitConfig {


    //处理消息返回
//    @Bean
//    public MessageConverter jsonMessageConverter() {
//        return new Jackson2JsonMessageConverter();
//    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();

        rabbitTemplate.setConnectionFactory(connectionFactory);
        //设置消息发送格式为json
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        // 开启Mandatory, 才能触发回调函数，无论消息推送结果如何都强制调用回调函数
        rabbitTemplate.setMandatory(true);
        /**消息发送到exchange回调 需设置：spring.rabbitmq.publisher-confirms=correlated*/
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                if (ack) {
                    log.debug("发送到exchange成功:correlationData({}),ack({}),cause({})", correlationData, ack, cause);
                } else {
                    log.debug("发送到exchange失败:correlationData({}),ack({}),cause({})", correlationData, ack, cause);
                }
            }
        });
        /**消息从exchange发送到queue失败回调  需设置：spring.rabbitmq.publisher-returns=true*/
        rabbitTemplate.setReturnsCallback(new RabbitTemplate.ReturnsCallback() {
            @Override
            public void returnedMessage(ReturnedMessage returnedMessage) {
                //请注意!如果你使用了延迟队列插件，那么一定会调用该callback方法，因为数据并没有提交上去，而是提交在交换器中，过期时间到了才提交上去，并非是bug！你可以用if进行判断交换机名称来捕捉该报错
                if (returnedMessage.getExchange().equals("dmpExchange")) {
                    return;
                }
                log.error("消息丢失:交换机exchange({}),路由键route({}),回应码replyCode({}),回应信息replyText({}),消息message:{}", returnedMessage.getExchange(), returnedMessage.getRoutingKey(), returnedMessage.getReplyCode(), returnedMessage.getReplyText(), returnedMessage.getMessage());
            }

        });
        return rabbitTemplate;
    }
}
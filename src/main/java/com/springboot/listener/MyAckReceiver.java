package com.springboot.listener;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;


/**
 * @author xiaomai
 * @date: 2022/11/7
 */
@Component
public class MyAckReceiver implements ChannelAwareMessageListener {

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        // 消息的唯一性ID
        long deliveryTag = message.getMessageProperties().getDeliveryTag();

        try {
            String msg = message.toString();
            System.out.println("消息: " + msg);
            System.out.println("消息来自: " + message.getMessageProperties().getConsumerQueue());

            // 手动确认
            channel.basicAck(deliveryTag, true);
        } catch (Exception e) {
            // 拒绝策略
            // 处理失败,重新压入MQ
            channel.basicReject(deliveryTag, false);
            e.printStackTrace();
        }
    }
}
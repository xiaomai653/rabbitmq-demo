package com.springboot.listener;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import com.springboot.service.IGoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RabbitListener(queues = "orderTopicQueue")
public class OrderListener implements ChannelAwareMessageListener {

    @Autowired
    IGoodsService goodsService;

    public static int i = 0;


    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        try {
            long deliveryTag = message.getMessageProperties().getDeliveryTag();
            String msg = new String(message.getBody(), "utf-8");
            JSONObject jsonObject = JSONObject.parseObject(msg);
            //添加订单
            goodsService.buy(jsonObject.getString("goodsId"), jsonObject.getString("userId"));
            //确定消费
            channel.basicAck(deliveryTag, true);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}

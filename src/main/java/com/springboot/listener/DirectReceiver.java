package com.springboot.listener;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
//@RabbitListener(queues = "myDirectQueue")
public class DirectReceiver{

//    @RabbitHandler
//    @RabbitListener(queues = "myDirectQueue")
//    public void process(String msg) {
//        System.out.println(msg);
//    }

    @RabbitHandler
    @RabbitListener(queues = "dlkQueue")
    public void process1(String msg){
        System.out.println("死信队列 " + msg);
    }

    @RabbitHandler
    @RabbitListener(queues = "dmpQueue")
    public void process2(String msg){
        System.out.println("延时队列 " + msg);
    }


    @RabbitHandler
    @RabbitListener(queues = "fanoutQueueA")
    public void processA(String msg) {
        System.out.println("fanoutQueueA " + msg);
    }

    @RabbitHandler
    @RabbitListener(queues = "fanoutQueueB")
    public void processB(Message message, Channel channel) throws Exception{
        String msg = new String(message.getBody());
        System.out.println("fanoutQueueB " + msg);
        try {
            // 这里写各种业务逻辑
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            log.error(e.getMessage());
        }
    }


    /**
     * 手动ack确认
     */
    @RabbitHandler
    @RabbitListener(queues = "fanoutQueueC")
    public void processC(Message message, Channel channel) throws Exception{
        String msg = new String(message.getBody());
        System.out.println("fanoutQueueC " + msg);
        try {
            // 这里写各种业务逻辑
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            log.error(e.getMessage());
        }
    }


    @RabbitHandler
    @RabbitListener(queues = "myTopicQueue_01")
    public void process_01(String msg) {
        System.out.println("myTopicQueue_01 " + msg);
    }

    @RabbitHandler
    @RabbitListener(queues = "myTopicQueue_02")
    public void process_02(String msg) {
        System.out.println("myTopicQueue_02 " + msg);
    }

    @RabbitHandler
    @RabbitListener(queues = "myTopicQueue_03")
    public void process_03(String msg) {
        System.out.println("myTopicQueue_03 " + msg);
    }

    @RabbitHandler
    @RabbitListener(queues = "myTopicQueue_04")
    public void process_04(String msg) {
        System.out.println("myTopicQueue_04 " + msg);
    }


//    @RabbitHandler
//    @RabbitListener(queues = "orderTopicQueue")
//    public void process_05(String msg) {
//        System.out.println("orderTopicQueue " + msg);
//    }


}
package com.springboot.controller;

import com.springboot.entity.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.Random;

@RestController
@Slf4j
public class HelloController {


//    @Autowired
    @Resource(name = "rabbitTemplate")
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/sendError")
    public String sendError(){
        String msg = "你好！";
        rabbitTemplate.convertAndSend("myDirect", "my.direct.routing", msg);
        return "hello world";
    }

    @GetMapping("/send")
    public String send(){
//        String msg = "hello world";
        String msg = "你好！";
        rabbitTemplate.convertAndSend("myDirectExchange", "my.direct.routing", msg);
        return "hello world";
    }

    @GetMapping("/sendByFanout")
    public String sendByFanout() {
        String msg = "hello fanout";
        rabbitTemplate.convertAndSend("fanoutExchange", null, msg);
        return "hello fanout";
    }

    @RequestMapping("/sendByTopic")
    public String sendByTopic() {
        String msg = "hello";
        rabbitTemplate.convertAndSend("myTopicExchange", "user.weather", msg + " topic.01");
        rabbitTemplate.convertAndSend("myTopicExchange", "student.new", msg + " topic.xxx");
        return "hello topic";
    }

    @GetMapping("/ttl")
    public String ttl(){
        String msg = "你好！";
        /**死信队列*/
        rabbitTemplate.convertAndSend("demoExchange", "demo.routeKey", msg, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                //设置消息的过期时间，是以毫秒为单位的
                message.getMessageProperties().setExpiration("5000");
                return message;
            }
        });
        /**延时交换机、需装插件*/
        rabbitTemplate.convertAndSend("dmpExchange", "dmp.routeKey", msg, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                //使用延迟插件只需要在消息的header中添加x-delay属性，值为过期时间，单位毫秒
                message.getMessageProperties().setHeader("x-delay",5*1000);
                return message;
            }
        });
        return "hello ttl";
    }

    @GetMapping("/test")
    public String test() {
        Random random = new Random();
        int i = random.nextInt(10) + 1;
        Order order = new Order();
        order.setUserId(String.valueOf(i));
        order.setGoodsId("1");
        String key = order.getUserId()+"_"+order.getGoodsId();
        Boolean b = redisTemplate.opsForValue().setIfAbsent(key, "1", Duration.ofMinutes(5));
        if (b) {
            rabbitTemplate.convertAndSend("orderTopicExchange", "order.buy", order);
        }
        return "test";
    }


}
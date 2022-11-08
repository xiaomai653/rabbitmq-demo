package com.springboot.controller;


import com.springboot.service.IGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

/**
 * <p>
 * 商品表 前端控制器
 * </p>
 *
 * @author xiaomai
 * @since 2021-11-23
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private IGoodsService goodsService;

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/test")
    public String test(){
        return "test";
    }


    @GetMapping("/seckill")
    public String kill(){

        // 使用redis单线程时使用
        // 初始化商品数量
        Integer goodsSum = goodsService.getGoodsSum("1");
        redisTemplate.opsForValue().set("1"+":goodsSum",goodsSum, Duration.ofMinutes(2));

        for (int i = 0; i < 500; i++) {
            int n = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //不加锁秒杀
//                    goodsService.seckillUnLock("1",String.valueOf(n));

                    // 数据库乐观锁
//                    goodsService.seckillOptimisticLock("1",String.valueOf(n));

                    // 数据库悲观锁
//                    goodsService.seckillPessimisticLock("1",String.valueOf(n));

                    // java同步锁
//                    goodsService.seckillSynchronousLock("1",String.valueOf(n));

                    // java可重入锁
//                    goodsService.seckillReentrantLock("1",String.valueOf(n));

                    // redis 单线程
                    goodsService.seckillRedisSingleThread("1",String.valueOf(n));
                }
            }).start();
        }
        return "success";
    }


    @GetMapping("seckill_redis")
    public String seckillRedis(){
        //初始化商品数量
        Integer goodsSum = goodsService.getGoodsSum("1");
        redisTemplate.opsForValue().set("1"+":goodsSum",goodsSum, Duration.ofMinutes(2));
        for (int i = 0; i < 1; i++) {
            int n = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    goodsService.flashSellByLuaScript("1",String.valueOf(n));
                }
            }).start();
        }
        return "success";
    }



}


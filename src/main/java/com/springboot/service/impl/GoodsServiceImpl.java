package com.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springboot.common.Result;
import com.springboot.entity.Goods;
import com.springboot.entity.Order;
import com.springboot.mapper.GoodsMapper;
import com.springboot.service.IGoodsService;
import com.springboot.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>
 * 商品表 服务实现类
 * </p>
 *
 * @author xiaomai
 * @since 2021-11-23
 */
@Service
@Slf4j
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements IGoodsService {

    @Autowired
    IOrderService orderService;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    StringRedisTemplate stringRedisTemplate;


    private Lock lock = new ReentrantLock();

    /**
     * 不加锁实现秒杀
     * @param goodId
     * @return
     */
    @Override
    public void seckillUnLock(String goodId, String userId) {
        Goods goods = this.getById(goodId);
        if (goods==null){
            return;
        }
        int stock = goods.getStock();
        if (goods != null && stock > 0) {
            goods.setStock(goods.getStock() - 1);
            if (this.updateById(goods)){
                Order order = new Order();
                order.setUserId(userId);
                order.setGoodsId(goodId);
                orderService.add(order);
                log.info("{}购买了{}",userId,goodId);
            }
        }
    }


    /**
     * 数据库乐观锁实现秒杀
     * @param goodId
     * @return
     */
    @Override
    public void seckillOptimisticLock(String goodId, String userId) {
        Goods goods = this.getById(goodId);
        if (goods==null){
            return;
        }
        int stock = goods.getStock();
        if (goods != null && stock > 0) {
            goods.setStock(goods.getStock() - 1);
            boolean b = this.update(goods, new LambdaQueryWrapper<Goods>().eq(Goods::getId,goodId).eq(Goods::getStock,stock));  //加锁cas
            if (b){
                Order order = new Order();
                order.setUserId(userId);
                order.setGoodsId(goodId);
                orderService.add(order);
                log.info("{}购买了{}",userId,goodId);
            }else {
                log.info("{}购买失败{}",userId,goodId);
            }
        }
    }


    /**
     * 数据库悲观锁实现秒杀
     * @param goodId
     * @return
     */
    @Override
    @Transactional
    public void seckillPessimisticLock(String goodId, String userId) {
        Goods goods = this.getOne(new LambdaQueryWrapper<Goods>().eq(Goods::getId, goodId).last("for update"));
        if (goods==null){
            return;
        }
        int stock = goods.getStock();
        if (goods != null && stock > 0) {
            goods.setStock(goods.getStock() - 1);
            boolean b = this.updateById(goods);
            if (b){
                Order order = new Order();
                order.setUserId(userId);
                order.setGoodsId(goodId);
                orderService.add(order);
                log.info("{}购买了{}",userId,goodId);
            }else {
                log.info("{}购买失败{}",userId,goodId);
            }
        }
    }



    /**
     * java同步锁实现秒杀
     * @param goodId
     * @return
     */
    @Override
    public void seckillSynchronousLock(String goodId, String userId) {
        synchronized (this){
            Goods goods = this.getById(goodId);
            if (goods==null){
                return;
            }
            int stock = goods.getStock();
            if (goods != null && stock > 0) {
                goods.setStock(goods.getStock() - 1);
                if (this.updateById(goods)){
                    Order order = new Order();
                    order.setUserId(userId);
                    order.setGoodsId(goodId);
                    orderService.add(order);
                    log.info("{}购买了{}",userId,goodId);
                }
            }
        }
    }


    /**
     * java可重入锁实现秒杀
     * @param goodId
     * @return
     */
    @Override
    public void seckillReentrantLock(String goodId, String userId) {
        lock.lock();
        Goods goods = this.getById(goodId);
        if (goods==null){
            return;
        }
        int stock = goods.getStock();
        if (goods != null && stock > 0) {
            goods.setStock(goods.getStock() - 1);
            if (this.updateById(goods)){
                Order order = new Order();
                order.setUserId(userId);
                order.setGoodsId(goodId);
                orderService.add(order);
                log.info("{}购买了{}",userId,goodId);
            }
        }
        lock.unlock();
    }


    /**
     * redis 单线程处理秒杀
     * @param goodId
     * @return
     */
    @Override
    public void seckillRedisSingleThread(String goodId, String userId) {

        //增量计算剩余库存(利用redis的单线程特性)
        double goodsSurplusSum=redisTemplate.opsForValue().decrement(goodId+":goodsSum",1);

        if(goodsSurplusSum>=0){
            //扣除库存
            Goods goods = this.getById(goodId);
            goods.setStock(goods.getStock()-1);
            this.updateById(goods);
            //插入订单
            Order order = new Order();
            order.setUserId(userId);
            order.setGoodsId(goodId);
            orderService.add(order);
            log.info("{}购买了{}",userId,goodId);
        }else {
            log.info("{}购买失败{}",userId,goodId);
        }

    }

    /**
     * 数据库乐观锁实现秒杀
     * @param goodId
     * @return
     */
    @Override
    @Transactional
    public void buy(String goodId, String userId) {
        Goods goods = this.getById(goodId);
        if (goods==null){
            return;
        }
        int stock = goods.getStock();
        if (goods != null && stock > 0) {
            goods.setStock(goods.getStock() - 1);
//            boolean b = this.updateById(goods);
            boolean b = this.update(goods, new LambdaQueryWrapper<Goods>().eq(Goods::getId,goodId).eq(Goods::getStock,stock));  //加锁cas
            if (b){
                Order order = new Order();
                order.setUserId(userId);
                order.setGoodsId(goodId);
                orderService.add(order);
                log.info("{}购买了{}",userId,goodId);
            }else {
                String key = userId+"_"+goodId;
                redisTemplate.delete(key);
            }
        }
    }


    /**
     * 获取产品库存
     * @param goodId 产品ID
     * @return 库存数量
     */
    @Override
    public int getGoodsSum(String goodId) {
        Goods goods = this.getById(goodId);
        if (goods==null){
            return 0;
        }
        return goods.getStock();
    }

    @Override
    public Result flashSellByLuaScript(String goodId, String userId) {

        redisTemplate.setEnableTransactionSupport(true);  // 开启事务
        redisTemplate.watch(goodId+":goodsSum");     // 监听key，当该key被其它客户端改变时,则会中断当前的操作
        int num = (Integer) redisTemplate.opsForValue().get(goodId + ":goodsSum");
        if (num<=0){
            return Result.fail("商品已经被抢完");
        }
        redisTemplate.multi();
        redisTemplate.opsForValue().decrement(goodId+":goodsSum",1);
        List exec = redisTemplate.exec();
        if (null!=exec || exec.size()==0){
            log.info("{}购买失败{}",userId,goodId);
            return Result.fail();
        }
        //插入订单
        Order order = new Order();
        order.setUserId(userId);
        order.setGoodsId(goodId);
        orderService.add(order);
        log.info("{}购买了{}",userId,goodId);
        return Result.success();
    }


    @Override
    public void flashSellByRedisWatch(String skuCode, int buyNum) {

    }


}

package com.springboot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.springboot.common.Result;
import com.springboot.entity.Goods;

/**
 * <p>
 * 商品表 服务类
 * </p>
 *
 * @author xiaomai
 * @since 2021-11-23
 */
public interface IGoodsService extends IService<Goods> {


    /**
     * 不加锁，会超卖
     */
    void seckillUnLock(String goodId, String userId);


    /**
     * 数据库乐观锁
     */
    void seckillOptimisticLock(String goodId, String userId);


    /**
     * 数据库悲观锁
     */
    void seckillPessimisticLock(String goodId, String userId);


    /**
     * Java 同步锁
     */
    void seckillSynchronousLock(String goodId, String userId);

    /**
     * Java 可重入锁
     */
    void seckillReentrantLock(String goodId, String userId);

    /**
     * redis 单线程
     */
    void seckillRedisSingleThread(String goodId, String userId);

    /**
     * rabbitmq,redis
     */
    void buy(String id, String userId);

    /**
     * 获取产品库存
     */
    int getGoodsSum(String goodId);


    /**
     * 通过lua脚本实现的秒杀
     * @param goodId 商品ID
     * @param userId 用户ID
     */
    Result flashSellByLuaScript(String goodId, String userId);
    /**
     * 通过redis 事务 实现的秒杀
     * @param skuCode 商品编码
     * @param buyNum 购买数量
     * @return 购买数量
     */
    void flashSellByRedisWatch(String skuCode,int buyNum);


}

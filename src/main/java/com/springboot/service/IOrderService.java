package com.springboot.service;

import com.springboot.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author xiaomai
 * @since 2021-11-23
 */
public interface IOrderService extends IService<Order> {

    Boolean add(Order order);

}

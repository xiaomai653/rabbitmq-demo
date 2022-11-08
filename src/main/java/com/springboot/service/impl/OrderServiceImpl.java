package com.springboot.service.impl;

import com.springboot.entity.Order;
import com.springboot.mapper.OrderMapper;
import com.springboot.service.IOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author xiaomai
 * @since 2021-11-23
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    @Override
    public Boolean add(Order order) {
        return this.save(order);
    }
}

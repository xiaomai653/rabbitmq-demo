package com.springboot.mapper;

import com.springboot.entity.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 订单表 Mapper 接口
 * </p>
 *
 * @author xiaomai
 * @since 2021-11-23
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {

}

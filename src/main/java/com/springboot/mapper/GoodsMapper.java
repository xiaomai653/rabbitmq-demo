package com.springboot.mapper;

import com.springboot.entity.Goods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 商品表 Mapper 接口
 * </p>
 *
 * @author xiaomai
 * @since 2021-11-23
 */
@Mapper
public interface GoodsMapper extends BaseMapper<Goods> {

}

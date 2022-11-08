package com.springboot.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 商品表
 * </p>
 *
 * @author xiaomai
 * @since 2021-11-23
 */
@Getter
@Setter
@TableName("goods_info")
public class Goods implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商品主键
     */
    private String id;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品库存
     */
    private int stock;


}

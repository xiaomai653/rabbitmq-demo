package com.springboot.mapper;

import com.springboot.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author xiaomai
 * @since 2021-12-14
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}

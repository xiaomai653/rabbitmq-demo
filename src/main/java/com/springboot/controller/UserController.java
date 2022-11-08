package com.springboot.controller;


import com.springboot.common.Result;
import com.springboot.entity.User;
import com.springboot.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author xiaomai
 * @since 2021-12-14
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private static int i = 1;

    @Autowired
    IUserService userService;

    @GetMapping("/add")
    public String add(){
        User user = new User();
        user.setName("小明"+ i);
        userService.save(user);
        i++;
        return "success";
    }

}


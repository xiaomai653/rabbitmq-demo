package com.springboot.service;

import com.springboot.common.Result;

/**
 * 会员管理Service
 * Created by macro on 2018/8/3.
 */
public interface UmsMemberService {

    /**
     * 生成验证码
     */
    Result generateAuthCode(String telephone);

    /**
     * 判断验证码和手机号码是否匹配
     */
    Result verifyAuthCode(String telephone, String authCode);

}
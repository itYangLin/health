package com.it.controller;

import com.it.constant.MessageConstant;
import com.it.constant.RedisMessageConstant;
import com.it.utils.SMSUtils;
import com.it.utils.ValidateCodeUtils;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {

    @Autowired
    private JedisPool jedisPool;

    @RequestMapping("/send4Order")
    public Result send4Order(String telephone) {
        try {
            //随机生成6位验证码
            Integer validateCode = ValidateCodeUtils.generateValidateCode(6);
            //使用阿里云发送短信验证码
            // SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,validateCode.toString());
            System.out.println("发送成功"+validateCode);
            //保存在redis服务器 保存5分钟
            jedisPool.getResource().setex(RedisMessageConstant.SENDTYPE_ORDER+"_"+telephone,300,validateCode.toString());

            return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
    }

    @RequestMapping("/send4Login")
    public Result send4Login(String telephone) {
        //随机生成6位验证码
        Integer validateCode = ValidateCodeUtils.generateValidateCode(6);
        try {
            //使用阿里云发送短信验证码
            // SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,validateCode.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        System.out.println("发送成功"+validateCode);
        //保存在redis服务器 保存5分钟
        jedisPool.getResource().setex(RedisMessageConstant.SENDTYPE_LOGIN+"_"+telephone,300,validateCode.toString());

        return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }

}

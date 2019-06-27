package com.it.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.it.bean.Order;
import com.it.constant.MessageConstant;
import com.it.constant.RedisMessageConstant;
import com.it.service.OrderService;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Reference
    private OrderService orderService;

    @Autowired
    private JedisPool jedisPool;

    @RequestMapping("/submit")
    public Result submit(@RequestBody Map map) {
        try {
            String validateCode = (String) map.get("validateCode");
            String telephone = (String) map.get("telephone");
            String s = jedisPool.getResource().get(RedisMessageConstant.SENDTYPE_ORDER + "_" + telephone);
            if (s==null || !validateCode.equals(s)) {
                return new Result(false, MessageConstant.VALIDATECODE_ERROR);
            }
            Result result = orderService.submit(map);

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ORDER_FALL);
        }
    }

    @RequestMapping("/findById")
    public Result findById(Integer id) {
        try {
            Map map = orderService.findById(id);
            return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
        }
    }
}

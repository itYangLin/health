package com.it.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.it.bean.SetMeal;
import com.it.constant.MessageConstant;
import com.it.service.SetMealService;
import entity.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/setmeal")
public class SetMealController {

    @Reference
    private SetMealService steMealService;

    @RequestMapping("/getSetmeal")
    public Result getSetmeal() {
        try {
            List<SetMeal> list = steMealService.getSetMeal();
            return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }

    @RequestMapping("/findById")
    public Result findById(Integer id) {
        try {
            SetMeal setmeal = steMealService.findById(id);
            return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }



}

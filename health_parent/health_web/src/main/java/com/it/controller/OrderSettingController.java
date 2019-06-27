package com.it.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.it.bean.OrderSetting;
import com.it.constant.MessageConstant;
import com.it.service.OrderSettingService;
import com.it.utils.POIUtils;
import entity.Result;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {

    @Reference
    private OrderSettingService orderSettingService;


    //预约表格上传 设置
    @RequestMapping("/upload")
    public Result upload(@RequestParam("excelFile") MultipartFile excelFile) {
        try {
            //获取表里面的数据 通过POI工具类
            List<String[]> file = POIUtils.readExcel(excelFile);
            List<OrderSetting> list = new ArrayList<>();
            for (String[] strings : file) {
                //遍历数据 保存到对象里面
                OrderSetting orderSetting = new OrderSetting(new Date(strings[0]),Integer.parseInt(strings[1]));
                list.add(orderSetting);
            }
            orderSettingService.add(list);
            return new Result(true,MessageConstant.UPLOAD_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.UPLOAD_FAIL);
        }
    }

    @RequestMapping("/findAll")
    public Result findAll(String date) {
        try {
            List<Map> list = orderSettingService.findAll(date);
            return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
        }
    }

    @RequestMapping("/editNumberByDate")
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting) {
        try {
            String s = orderSettingService.editNumberByDate(orderSetting);
            if (s!=null) {
                return new Result(false,s);
            }
            return new Result(true,MessageConstant.ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ORDERSETTING_FAIL);
        }
    }

}

package com.it.service;


import com.it.bean.OrderSetting;

import java.util.List;
import java.util.Map;

public interface OrderSettingService {

    //上传文件新增数据
    void add(List<OrderSetting> list);

    //查询所有
    List<Map> findAll(String date);

    //设置当天预约人数
    String editNumberByDate(OrderSetting orderSetting);
}

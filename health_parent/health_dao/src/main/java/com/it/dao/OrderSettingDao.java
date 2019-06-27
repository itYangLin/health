package com.it.dao;


import com.it.bean.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderSettingDao {

    //查询
    int selectByDate(Date orderDate);

    //更新
    void editNumberByDate(OrderSetting orderSetting);

    //新增
    void add(OrderSetting orderSetting);

    //查询当前月份
    List<OrderSetting> getOrderSettingByDate(String s);

    //查询已预约数据
    OrderSetting selectOrderSettingByDate(Date orderDate);


    //更新可预约人数
    public void editNumberByOrderDate(OrderSetting orderSetting);

    //更新已预约人数
    public void editReservationsByOrderDate(OrderSetting orderSetting);
    public long findCountByOrderDate(Date orderDate);

    //根据日期范围查询预约设置信息
    public List<OrderSetting> getOrderSettingByMonth(Map date);

    //根据预约日期查询预约设置信息
    public OrderSetting findByOrderDate(Date orderDate);

}

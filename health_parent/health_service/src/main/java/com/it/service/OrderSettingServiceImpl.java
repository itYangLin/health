package com.it.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.it.bean.OrderSetting;
import com.it.dao.OrderSettingDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    @Override
    public void add(List<OrderSetting> list) {
        for (OrderSetting orderSetting : list) {
            //查询数据库是否已存在
          int i =  orderSettingDao.selectByDate(orderSetting.getOrderDate());

            if (i > 0) {
                //已经存在 更新数据库
                orderSettingDao.editNumberByDate(orderSetting);
            } else {
                //没有存在 新增数据
                orderSettingDao.add(orderSetting);
            }
        }
    }
    //方式一:
    @Override
    public List<Map> findAll(String date) {
        String s = date+"%";
        List<OrderSetting> orderSettings = orderSettingDao.getOrderSettingByDate(s);
        List<Map> list = new ArrayList<>();
        for (OrderSetting orderSetting : orderSettings) {
            Map map = new HashMap();
            map.put("date",orderSetting.getOrderDate().getDate());
            map.put("number",orderSetting.getNumber());
            map.put("reservations",orderSetting.getReservations());
            list.add(map);
        }
        return list;
    }
    //方式二:
    public List<OrderSetting> findAll2(String date) {
        String s = date+"%";
        List<OrderSetting> orderSettings = orderSettingDao.getOrderSettingByDate(s);
        return orderSettings;
    }
    //设置当天预约人数
    @Override
    public String editNumberByDate(OrderSetting orderSetting) {
        int i = orderSettingDao.selectByDate(orderSetting.getOrderDate());
        if (i > 0) {
            OrderSetting os = orderSettingDao.selectOrderSettingByDate(orderSetting.getOrderDate());
            if (orderSetting.getNumber() < os.getReservations()) {
               return ("设置预约人数失败!");
            }
            orderSettingDao.editNumberByDate(orderSetting);
        } else {
            orderSettingDao.add(orderSetting);
        }
        return null;
    }
}

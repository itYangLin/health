package com.it.service;


import com.alibaba.dubbo.config.annotation.Service;
import com.it.bean.Member;
import com.it.bean.Order;
import com.it.bean.OrderSetting;
import com.it.constant.MessageConstant;
import com.it.dao.MemberDao;
import com.it.dao.OrderDao;
import com.it.dao.OrderSettingDao;
import com.it.utils.DateUtils;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl  implements OrderService{

    @Autowired
    private OrderSettingDao orderSettingDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private MemberDao memberDao;


    //预约业务的处理
    @Override
    public Result submit(Map map) throws Exception {
       //1.判断当前的日期是否可以预约(根据orderDate查询t_ordersetting, 能查询出来可以预约;查询不出来,不能预约的)
        String orderDate = (String) map.get("orderDate");
        Date date = DateUtils.parseString2Date(orderDate);
        OrderSetting orderSetting = orderSettingDao.findByOrderDate(date);
        if (orderSetting==null) {
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }

        //2.判断当前日期是否预约已满(判断reservations是否==number)
        if (orderSetting.getReservations() >= orderSetting.getNumber()) {
            return new Result(false,MessageConstant.ORDER_FULL);
        }
       //3.判断是否是会员(根据手机号码查询t_member)
        String telephone = (String) map.get("telephone");
        String setmealId = (String) map.get("setmealId");
        Member member = memberDao.findByTelephone(telephone);
        if (member != null) {
            // - 如果是会员(能够查询出来), 防止重复预约(根据member_id,orderDate,setmeal_id查询t_order)
            Order order = new Order();
            order.setMemberId(member.getId());
            order.setOrderDate(date);
            order.setSetmealId(Integer.parseInt(setmealId));
            List<Order> orderList = orderDao.findByCondition(order);
            if (orderList.size()>0 && orderList !=null) {
                return new Result(false,MessageConstant.HAS_ORDERED);
            }
        } else {
            // - 如果不是会员(不能够查询出来),自动注册为会员(直接向t_member插入一条记录)
            member = new Member();
            member.setName((String) map.get("name"));
            member.setPhoneNumber(telephone);
            member.setIdCard((String) map.get("idCard"));
            member.setSex((String) map.get("sex"));
            member.setRegTime(new Date());
            memberDao.add(member);
        }
       //4.进行预约
       // - 向t_order表插入一条记录
        Order order = new Order(member.getId(), date, Order.ORDERTYPE_WEIXIN, Order.ORDERSTATUS_NO,Integer.parseInt(setmealId));
        orderDao.add(order);
       // - t_ordersetting表里面预约的人数+1
        orderSetting.setReservations(orderSetting.getReservations()+1);
        orderSettingDao.editReservationsByOrderDate(orderSetting);
        return new Result(true,MessageConstant.ORDER_SUCCESS,order);
    }


    @Override
    public Map findById(Integer id) {
        Map map = orderDao.findById4Detail(id);
        return map;
    }

    //统计运营数据
    @Override
    public Map getBusinessReportData() throws Exception {
        Map map = new HashMap();
        //当天日期
        String today = DateUtils.parseDate2String(new Date(),"yyyy-MM-dd");
        //每周的周一
        String thisWeek = DateUtils.parseDate2String(DateUtils.getThisWeekMonday());
        //每月的一号
        String firstDay4ThisMonth = DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth());

        //当天新增会员数
        Integer memberCountByDate = memberDao.findMemberCountByDate(today);
        //总会员数
        Integer memberTotalCount = memberDao.findMemberTotalCount();
        //本周新增会员
        Integer memberCountAfterDate = memberDao.findMemberCountAfterDate(thisWeek);
        //本月新增会员
        Integer memberCountAfterDate1 = memberDao.findMemberCountAfterDate(firstDay4ThisMonth);

        //今日预约数
        Integer orderCountByDate = orderDao.findOrderCountByDate(today);
        //今日到诊数
        Integer visitsCountByDate = orderDao.findVisitsCountByDate(today);
        //本周预约数
        Integer visitsCountAfterDate = orderDao.findOrderCountAfterDate(thisWeek);
        //本周到诊数
        Integer visitsCountAfterDate1 = orderDao.findVisitsCountAfterDate(thisWeek);
        //本月预约数
        Integer orderCountAfterDate = orderDao.findOrderCountAfterDate(firstDay4ThisMonth);
        //本月到诊数
        Integer visitsCountAfterDate2 = orderDao.findVisitsCountAfterDate(firstDay4ThisMonth);

        //热门套餐
        List<Map> hotSetmeal = orderDao.findHotSetmeal();

        map.put("reportDate",today);
        map.put("todayNewMember",memberCountByDate);
        map.put("totalMember",memberTotalCount);
        map.put("thisWeekNewMember",memberCountAfterDate);
        map.put("thisMonthNewMember",memberCountAfterDate1);
        map.put("todayOrderNumber",orderCountByDate);
        map.put("todayVisitsNumber",visitsCountByDate);
        map.put("thisWeekOrderNumber",visitsCountAfterDate);
        map.put("thisWeekVisitsNumber",visitsCountAfterDate1);
        map.put("thisMonthOrderNumber",orderCountAfterDate);
        map.put("thisMonthVisitsNumber",visitsCountAfterDate2);
        map.put("hotSetmeal",hotSetmeal);

        return map;
    }
}

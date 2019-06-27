package com.it.service;

import com.it.bean.SetMeal;
import entity.PageResult;
import entity.QueryPageBean;

import java.util.List;
import java.util.Map;

public interface SetMealService {

    //新增
    void add(SetMeal setmeal, Integer... checkgroupIds);
    //分页查询
    PageResult fingPage(QueryPageBean queryPageBean);
    //根据ID查询
    SetMeal selectById(Integer id);
    //根据id查询中间表
    List<Integer> selectSetmealByCheckGroupId(Integer id);
    //更新
    void edit(SetMeal checkGroup, Integer[] checkitemIds);
    //删除套餐
    void delete(Integer id , String img);


    //查询套餐
    List<SetMeal> getSetMeal();

    //查询对应套餐
    SetMeal findById(Integer id);

    //套餐统计
    List<Map> getSetmealReport();
}

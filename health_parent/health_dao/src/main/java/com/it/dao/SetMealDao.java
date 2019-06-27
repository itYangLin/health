package com.it.dao;

import com.github.pagehelper.Page;
import com.it.bean.SetMeal;

import java.util.List;
import java.util.Map;

public interface SetMealDao {
    //新增
    void add(SetMeal setmeal);

    //根据ID保存中间表
    void selestSetmealById(Map map);

    //分页查询
    Page<SetMeal> fingPage(String queryString);

    //id查询
    SetMeal selectById(Integer id);

    //查询中间表
    List<Integer> selectSetmealByCheckGroupId(Integer id);

    //编辑保存
    void edit(SetMeal setmeal);

    //删除
    void delete(Integer id);

    //删除中间表
    void deleteByid(Integer id);


    //查询套餐
    List<SetMeal> getSetMeal();
    //查询对应套餐
    SetMeal findById(Integer id);

    //统计套餐
    List<Map> getSetmealReport();
}

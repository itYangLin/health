package com.it.dao;

import com.github.pagehelper.Page;
import com.it.bean.CheckItem;

import java.util.List;

public interface CheckItemDao {
    //新增检查项
    public void add(CheckItem checkItem);

    //分页查询
    Page<CheckItem> selectByCondition(String queryString);

    //id查询
    Integer selectById(Integer id);

    //删除
    void delete(Integer id);

    //根据ID查询
    CheckItem selectCheckItemById(Integer id);

    //更新
    void edit(CheckItem checkItem);

    //查询所有
    List<CheckItem> findAll();
}

package com.it.service;

import com.it.bean.CheckItem;
import entity.PageResult;

import java.util.List;

public interface CheckItemService {

    //新增检查项
    void add(CheckItem checkItem);

    //分页查询
    PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString);

    //删除
    void delete(Integer id);

    //id查询
    CheckItem selectById(Integer id);

    //更新
    void edit(CheckItem checkItem);

    //查询所有
    List<CheckItem> findAll();
}

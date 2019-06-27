package com.it.service;


import com.it.bean.CheckGroup;
import entity.PageResult;
import entity.QueryPageBean;

import java.util.List;


public interface CheckGroupService {

    //新增检查项
    void add(CheckGroup checkItem,Integer... checkitemIds);

    //分页查询
    PageResult pageQuery(QueryPageBean queryPageBean);

    //删除
    void delete(Integer id);

    //id查询
    CheckGroup selectById(Integer id);

    //更新
    void edit(CheckGroup checkItem,Integer... checkitemIds);

    //查询中间表
    List<Integer> selectCheckGroupIdByCheckItemIds(Integer id);

    //查询所有
    List<CheckGroup> findAll();
}

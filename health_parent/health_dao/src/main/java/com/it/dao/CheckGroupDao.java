package com.it.dao;

import com.github.pagehelper.Page;
import com.it.bean.CheckGroup;

import java.util.List;
import java.util.Map;

public interface CheckGroupDao {

    //新增检查组
    void add(CheckGroup checkGroup);

    //保存中间表
    void insertCheckitemIdsByCheckGroupId(Map map);

    //分页查询
    Page<CheckGroup> selectByCondition(String queryString);

    //id查询
    CheckGroup selectById(Integer id);


    //根据ID查询中间表
    List<Integer> selectCheckGroupIdByCheckItemIds(Integer id);


    //更新
    void edit(CheckGroup checkGroup);

    //根据ID删除中间表
    void deleteByid(Integer id);


    //删除
    void delete(Integer id);

    //查询所有
    List<CheckGroup> findAll();
}

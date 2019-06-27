package com.it.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.it.bean.CheckGroup;
import com.it.dao.CheckGroupDao;
import entity.PageResult;
import entity.QueryPageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    private CheckGroupDao checkgroupDao;

    //新增
    @Override
    public void add(CheckGroup checkGroup, Integer... checkItemIds) {
        checkgroupDao.add(checkGroup);
        insertCheckitemIdsByCheckGroupId(checkGroup.getId(),checkItemIds);
    }

    //根据ID保存数据在中间表
    public void insertCheckitemIdsByCheckGroupId(int checkGroupId ,Integer...checkItemIds ) {
        if (checkItemIds != null) {
            for (Integer checkitemId : checkItemIds) {
                Map map = new HashMap();
                map.put("checkitemId",checkitemId);
                map.put("checkGroupId",checkGroupId);
                checkgroupDao.insertCheckitemIdsByCheckGroupId(map);
            }
        }
    }

    //分页查询
    @Override
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        Page<CheckGroup> cg = checkgroupDao.selectByCondition(queryPageBean.getQueryString());
        return new PageResult(cg.getTotal(),cg.getResult());
    }


    //id查询
    @Override
    public CheckGroup selectById(Integer id) {
        return checkgroupDao.selectById(id);
    }

    //查询中间表
    @Override
    public List<Integer> selectCheckGroupIdByCheckItemIds(Integer id) {
        return checkgroupDao.selectCheckGroupIdByCheckItemIds(id);
    }

    //查询所有
    @Override
    public List<CheckGroup> findAll() {
        return checkgroupDao.findAll();
    }

    //编辑保存
    @Override
    public void edit(CheckGroup checkGroup, Integer... checkitemIds) {
        checkgroupDao.edit(checkGroup);
        deleteById(checkGroup.getId());
        insertCheckitemIdsByCheckGroupId(checkGroup.getId(),checkitemIds);
    }


    //删除
    @Override
    public void delete(Integer id) {
        deleteById(id);
        checkgroupDao.delete(id);
    }

    //删除中间表
    public void deleteById(Integer id) {
        checkgroupDao.deleteByid(id);
    }


}

package com.it.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.it.bean.CheckItem;
import com.it.dao.CheckItemDao;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = CheckItemService.class)
@Transactional
public class CheckItemServiceImpl implements CheckItemService {

    @Autowired
    private CheckItemDao checkItemDao;

    //新增检查项
    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    //分页查询
    @Override
    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage,pageSize);
        Page<CheckItem> page = checkItemDao.selectByCondition(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }

    //根据id删除
    @Override
    public void delete(Integer id) {
        Integer c = checkItemDao.selectById(id);
        if (c > 0) {
            throw new RuntimeException("该选项已经被其他组件引用了,无法删除");
        } else {
            checkItemDao.delete(id);
        }
    }

    //根据ID查询
    @Override
    public CheckItem selectById(Integer id) {
        return checkItemDao.selectCheckItemById(id);
    }

    //更新
    @Override
    public void edit(CheckItem checkItem) {
        checkItemDao.edit(checkItem);
    }

    //查询所有
    @Override
    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }
}

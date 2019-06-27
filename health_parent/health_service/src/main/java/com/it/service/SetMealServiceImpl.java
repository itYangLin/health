package com.it.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.it.bean.SetMeal;
import com.it.constant.RedisConstant;
import com.it.dao.SetMealDao;
import entity.PageResult;
import entity.QueryPageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = SetMealService.class)
@Transactional
public class SetMealServiceImpl implements SetMealService {

    @Autowired
    private SetMealDao setmealDao;

    @Autowired
    private JedisPool jedisPool;

    //图片保存ID
    private String img;


    //新增
    @Override
    public void add(SetMeal setmeal, Integer... checkgroupIds) {
        setmealDao.add(setmeal);
        selestSetmealById(setmeal.getId(),checkgroupIds);
        //没报异常 把图片保存在redis里面
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,setmeal.getImg());
    }

    //分页查询
    @Override
    public PageResult fingPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        Page<SetMeal> page = setmealDao.fingPage(queryPageBean.getQueryString());
        return new PageResult(page.getTotal(),page.getResult());
    }



    //根据ID保存中间表
    private void selestSetmealById(Integer id, Integer... checkgroupIds) {
        if (checkgroupIds != null) {
            for (Integer checkgroupId : checkgroupIds) {
                Map map = new HashMap();
                map.put("setmeal",id);
                map.put("checkgroupId",checkgroupId);
                setmealDao.selestSetmealById(map);
            }
        }
    }

    //id查询
    @Override
    public SetMeal selectById(Integer id) {
        SetMeal setmeal = setmealDao.selectById(id);
        img = setmeal.getImg();
        return setmeal;
    }

    //查询中间表
    @Override
    public List<Integer> selectSetmealByCheckGroupId(Integer id) {
        return setmealDao.selectSetmealByCheckGroupId(id);
    }


    //编辑保存
    @Override
    public void edit(SetMeal setmeal, Integer... checkgroupIds) {

        setmealDao.edit(setmeal);
        deleteByid(setmeal.getId());
        selestSetmealById(setmeal.getId(),checkgroupIds);

        //判定图片旧的数据和新的数据是否相等 相等就不删除 不相等就删除旧数据
        if (img != null) {
            if (!img.equals(setmeal.getImg())) {
                jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_DB_RESOURCES,img);
            }
        }
        //没报异常 把图片保存在redis里面
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,setmeal.getImg());
    }


    //删除
    @Override
    public void delete(Integer id,String img) {
        deleteByid(id);
        setmealDao.delete(id);
        //删除redis里面的数据
        jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_DB_RESOURCES,img);
    }

    //删除中间表
    public void deleteByid(Integer id) {
        setmealDao.deleteByid(id);
    }

    /*
    * 查询套餐
    * */

    @Override
    public List<SetMeal> getSetMeal() {
        List<SetMeal> list =  setmealDao.getSetMeal();
        return list;
    }

    @Override
    public SetMeal findById(Integer id) {
        SetMeal setMeal =  setmealDao.findById(id);
        return setMeal;
    }

    //套餐统计
    @Override
    public List<Map> getSetmealReport() {
        return setmealDao.getSetmealReport();
    }


}

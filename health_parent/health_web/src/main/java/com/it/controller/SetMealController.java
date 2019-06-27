package com.it.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.it.bean.SetMeal;
import com.it.constant.MessageConstant;
import com.it.constant.RedisConstant;
import com.it.service.SetMealService;
import com.it.utils.QiniuUtils;
import com.it.utils.UploadUtils;
import entity.PageResult;
import entity.QueryPageBean;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.util.List;

@RestController
@RequestMapping("/setMeal")
public class SetMealController {

    @Reference
    private SetMealService setmealService;

    @Autowired
    private JedisPool jedisPool;

    //图片上传
    @RequestMapping("/upload")
    public Result upload(MultipartFile imgFile) {
        try {
            //获取文件名
            String filename = imgFile.getOriginalFilename();
            //把文件名改为UUID
            filename = UploadUtils.getUUIDName(filename);
            //调用工具类进行上传
            QiniuUtils.upload2Qiniu(imgFile.getBytes(),filename);
            //保存图片名字到redis里面
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,filename);

            return new Result(true,MessageConstant.PIC_UPLOAD_SUCCESS,filename);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true,MessageConstant.PIC_UPLOAD_FAIL);
        }
    }


    //新增
    @RequestMapping("/add")
    @PreAuthorize("hasAnyAuthority('SETMEAL_ADD')")
    public Result add(@RequestBody SetMeal setmeal, Integer... checkGroupIds) {
        try {
            setmealService.add(setmeal,checkGroupIds);
            return  new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return  new Result(true, MessageConstant.ADD_SETMEAL_FAIL);
        }
    }

    @RequestMapping("/findPage")
    public PageResult fingPage(@RequestBody QueryPageBean queryPageBean) {
        return setmealService.fingPage(queryPageBean);
    }


    //根据id查询表
    @RequestMapping("/selectById")
    public Result selectById(Integer id) {
        try {
            SetMeal setmeal= setmealService.selectById(id);
            return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_SETMEAL_SUCCESS);
        }
    }

    //根据id查询中间表
    @RequestMapping("/selectSetMealByCheckGroupId")
    public Result selectSetmealByCheckGroupId(Integer id) {
        try {
            List<Integer> checkItemIds= setmealService.selectSetmealByCheckGroupId(id);
            return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,checkItemIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_SETMEAL_SUCCESS);
        }
    }

    //更新
    @RequestMapping("/edit")
    @PreAuthorize("hasAnyAuthority('SETMEAL_EDIT')")
    public Result edit(@RequestBody SetMeal setmeal, Integer... checkItemIds) {
        try {
            setmealService.edit(setmeal,checkItemIds);
            return new Result(true,MessageConstant.EDIT_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_SETMEAL_FAIL);
        }
    }


    //删除
    @RequestMapping("/delete")
    @PreAuthorize("hasAnyAuthority('SETMEAL_DELETE')")
    public Result delete(Integer id,String img) {
        try {
            setmealService.delete(id,img);
            return new Result(true,MessageConstant.DELETE_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true,MessageConstant.DELETE_CHECKGROUP_FAIL);
        }
    }




}

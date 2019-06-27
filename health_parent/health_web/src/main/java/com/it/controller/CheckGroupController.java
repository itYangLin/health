package com.it.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.it.bean.CheckGroup;
import com.it.constant.MessageConstant;
import com.it.service.CheckGroupService;
import entity.PageResult;
import entity.QueryPageBean;
import entity.Result;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/checkGroup")
public class CheckGroupController {

    @Reference
    private CheckGroupService checkGroupService;

    //查询所有
    @RequestMapping("/findAll")
    public Result findAll() {
        try {
            List<CheckGroup> list = checkGroupService.findAll();
            return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }

    //新增
    @RequestMapping("/add")
    @PreAuthorize("hasAnyAuthority('CHECKGROUP_ADD')")
    public Result add(@RequestBody CheckGroup checkGroup, Integer... checkItemIds) {
        try {
            checkGroupService.add(checkGroup,checkItemIds);
            return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
        }
    }
    //分页查询
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult = checkGroupService.pageQuery(queryPageBean);
        return pageResult;
    }

    //根据id查询表
    @RequestMapping("/selectById")
    public Result selectById(Integer id) {
        try {
            CheckGroup checkGroup = checkGroupService.selectById(id);
            return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroup);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKGROUP_SUCCESS);
        }
    }

    //根据id查询中间表
    @RequestMapping("/selectCheckGroupIdByCheckItemIds")
    public Result selectCheckGroupIdByCheckItemIds(Integer id) {
        try {
            List<Integer> checkItemIds= checkGroupService.selectCheckGroupIdByCheckItemIds(id);
            return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkItemIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKGROUP_SUCCESS);
        }
    }

    //更新
    @RequestMapping("/edit")
    @PreAuthorize("hasAnyAuthority('CHECKGROUP_EDIT')")
    public Result edit(@RequestBody CheckGroup checkGroup, Integer... checkItemIds) {
        try {
            checkGroupService.edit(checkGroup,checkItemIds);
            return new Result(true,MessageConstant.EDIT_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
    }


    //删除
    @RequestMapping("/delete")
    @PreAuthorize("hasAnyAuthority('CHECKGROUP_DELETE')")
    public Result delete(Integer id) {
        try {
            checkGroupService.delete(id);
            return new Result(true,MessageConstant.DELETE_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true,MessageConstant.DELETE_CHECKGROUP_FAIL);
        }
    }



}

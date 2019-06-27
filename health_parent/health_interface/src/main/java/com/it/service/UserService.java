package com.it.service;

import com.it.bean.User;

public interface UserService {
    /**
     * 根据用户名查询用户
     * @param s
     * @return
     */
    User findByUserName(String s);

}

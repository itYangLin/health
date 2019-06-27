package com.it.dao;

import com.it.bean.User;

public interface UserDao {

    User findByUserName(String s);
}

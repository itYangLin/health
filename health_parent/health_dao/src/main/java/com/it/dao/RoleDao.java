package com.it.dao;

import com.it.bean.Role;

import java.util.Set;

public interface RoleDao {
    Set<Role> findByUid(Integer id);
}

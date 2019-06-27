package com.it.dao;

import com.it.bean.Permission;

import java.util.Set;

public interface PermissionDao {

    Set<Permission> findByRid(Integer role1Id);
}

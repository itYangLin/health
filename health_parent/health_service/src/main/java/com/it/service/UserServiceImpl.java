package com.it.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.it.bean.Permission;
import com.it.bean.Role;
import com.it.bean.User;
import com.it.dao.PermissionDao;
import com.it.dao.RoleDao;
import com.it.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements  UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private PermissionDao permissionDao;

    /**
     * 根据用户名查询用户,根据用户id查询角色,根据角色id查询权限
     * @param username
     * @return
     */
    @Override
    public User findByUserName(String username) {
        User user =  userDao.findByUserName(username);
        if (user==null) {
            return null;
        }
        Integer id = user.getId();
        Set<Role> role = roleDao.findByUid(id);
        if (role!=null && role.size()>0) {
            for (Role role1 : role) {
                Integer role1Id = role1.getId();
                Set<Permission> per = permissionDao.findByRid(role1Id);
                if (per!=null && per.size()>0) {
                    role1.setPermissions(per);
                }
            }
            user.setRoles(role);
        }
        return user;
    }


}

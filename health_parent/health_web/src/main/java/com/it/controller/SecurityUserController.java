package com.it.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.it.bean.Permission;
import com.it.bean.Role;
import com.it.bean.User;
import com.it.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller("securityUserController")
public class SecurityUserController implements UserDetailsService {

    @Reference
    private UserService userService;

    /**
     * 根据用户名查询用户,取出权限 封装并返回
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUserName(username);
        if (user == null) {
            return null;
        }
        List<GrantedAuthority> ga = new ArrayList<>();
        Set<Role> roles = user.getRoles();
        if (roles != null && roles.size() > 0) {
            for (Role role : roles) {
                Set<Permission> permissions = role.getPermissions();
                if (permissions!=null && permissions.size()>0) {
                    for (Permission permission : permissions) {
                        String keyword = permission.getKeyword();
                        ga.add(new SimpleGrantedAuthority(keyword));
                    }
                }
            }
        }
        UserDetails user1 = new org.springframework.security.core.userdetails.User(username, user.getPassword(), ga);
        return user1;
    }
}

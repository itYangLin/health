package com.it.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.it.bean.Member;
import com.it.constant.MessageConstant;
import com.it.constant.RedisMessageConstant;
import com.it.service.MemberService;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private MemberService memberService;

    @RequestMapping("/check")
    public Result check(HttpServletRequest request, @RequestBody Map map) {
        try {
            //手机号码
            String telephone = (String) map.get("telephone");
            //用户输入的验证码
            String validateCode = (String) map.get("validateCode");

            String s = jedisPool.getResource().get(RedisMessageConstant.SENDTYPE_LOGIN + "_" + telephone);
            Member member = null;
            if (s == null || !s.equals(validateCode)) {
                return new Result(false, MessageConstant.VALIDATECODE_ERROR);
            } else {
                //查询是否是会员 不是自动注册
                 member = memberService.findByTelephone(telephone);
                if (member == null) {
                    member = new Member();
                    member.setPassword(telephone);
                    member.setRegTime(new Date());
                    memberService.add(member);
                }
            }

            request.getSession().setAttribute("member",member);

            // //登陆成功 写入cookie 跟踪用户
            // Cookie cookie = new Cookie("login_member_telephone",telephone);
            // cookie.setPath("/");
            // cookie.setMaxAge(60*60*24*30); //设置30天
            // response.addCookie(cookie);
            return new Result(true,MessageConstant.LOGIN_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.VALIDATECODE_ERROR);
        }
    }
}

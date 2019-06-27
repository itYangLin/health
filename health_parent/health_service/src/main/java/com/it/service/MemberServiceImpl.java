package com.it.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.it.bean.Member;
import com.it.dao.MemberDao;
import com.it.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberDao memberDao;


    @Override
    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    //新增
    @Override
    public void add(Member member) {
        //建议把密码加密
        if(member.getPassword() != null && !"".equals(member.getPassword())){
            member.setPassword(MD5Utils.md5(member.getPassword()));
        }
        memberDao.add(member);
    }

    //查询每个月的会员人数 累计
    @Override
    public List<Integer> findMemberByMonths(List<String> mintList) {
        List<Integer> count = new ArrayList<>();
        if (mintList!=null&& mintList.size()>0) {
            for (String s : mintList) {
                s = s + "-32";
                Integer member = memberDao.findMemberCountBeforeDate(s);
                count.add(member);
            }
        }
        return count;
    }
}

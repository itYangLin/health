package com.it.service;

import com.it.bean.Member;

import java.util.List;


public interface MemberService {
    Member findByTelephone(String telephone);


    void add(Member member);

    List<Integer> findMemberByMonths(List<String> mintList);
}

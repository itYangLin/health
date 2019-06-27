package com.it.service;

import entity.Result;

import java.util.Map;

public interface OrderService {

    Result submit(Map map) throws Exception;

    Map findById(Integer id);

    //统计运营数据
    Map getBusinessReportData() throws Exception;
}

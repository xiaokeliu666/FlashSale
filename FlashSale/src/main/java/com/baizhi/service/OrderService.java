package com.baizhi.service;

public interface OrderService {
    // return the order id
    int kill(Integer id);

    String getMd5(Integer id, Integer userid);

    // kill method with md5 encryption
    int kill(Integer id, Integer userid, String md5);
}

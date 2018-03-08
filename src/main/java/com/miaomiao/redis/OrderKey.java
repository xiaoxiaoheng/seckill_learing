package com.miaomiao.redis;

/**
 * Created by cjq on 2018-03-07 16:30
 */
public class OrderKey extends BasePrefix{

    public OrderKey(int expireSeconds , String prefix) {
        super(expireSeconds , prefix);
    }

}

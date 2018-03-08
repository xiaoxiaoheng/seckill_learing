package com.miaomiao.redis;

/**
 * Created by cjq on 2018-03-07 16:16
 */
public interface KeyPrefix {
    int expireSeconds();

    String getPrefix();
}

package com.miaomiao.redis;

import com.fasterxml.jackson.databind.ser.Serializers;

/**
 * Created by cjq on 2018-03-07 16:28
 */
public class UserKey extends BasePrefix{
    private UserKey(String prefix) {
        super(prefix);
    }

    public static UserKey getById = new UserKey("id");
    public static UserKey getByName = new UserKey("name");
}

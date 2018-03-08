package com.miaomiao.redis;

import com.alibaba.fastjson.JSON;
import com.miaomiao.domain.MiaoShaUser;
import com.miaomiao.redis.KeyPrefix;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * Created by cjq on 2018-03-07 14:49
 */
@Service
public class RedisService {

    @Autowired
    JedisPool jedisPool;


    public <T> T get(KeyPrefix prefix , String key , Class<T> clazz) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = prefix.getPrefix() + key;
            String str = jedis.get(realKey);
            T ret = strToObject(str , clazz);
            return ret;
        } finally {
            returnToPool(jedis);
        }
    }

    public <T> boolean set(KeyPrefix prefix , String key , T value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String str = objToString(value);
            if(str == null || str.length() <= 0)
                return false;

            String realKey = prefix.getPrefix() + key;
            int expireSeconds = prefix.expireSeconds();
            if(expireSeconds <= 0)
                jedis.set(realKey , str);
            else
                jedis.setex(realKey , expireSeconds , str);
            return true;
        } finally {
            returnToPool(jedis);
        }
    }

    public <T> Long incr(KeyPrefix prefix , String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = prefix.getPrefix() + key;
            return jedis.incr(realKey);
        } finally {
            returnToPool(jedis);
        }
    }

    public <T> Long decr(KeyPrefix prefix , String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = prefix.getPrefix() + key;
            return jedis.decr(realKey);
        } finally {
            returnToPool(jedis);
        }
    }

    private <T> String objToString(T value) {
        if(value == null)
            return null;
        Class<?> clazz = value.getClass();
        if(clazz == int.class || clazz == Integer.class) {
            return String.valueOf(value);
        } else if(clazz == long.class || clazz == Long.class) {
            return String.valueOf(value);
        } else if(clazz == String.class) {
            return (String) value;
        } else {
            return JSON.toJSONString(value);
        }
    }

    private <T> T strToObject(String str, Class<T> clazz) {
        // 字符串转对象
        if(str == null || str.length() <= 0)
            return null;
        if(clazz == int.class || clazz == Integer.class) {
            return (T) Integer.valueOf(str);
        } else if(clazz == long.class || clazz == Long.class) {
            return (T) Long.valueOf(str);
        } else if(clazz == String.class) {
            return (T) str;
        } else
            return JSON.toJavaObject(JSON.parseObject(str) , clazz);
    }

    private void returnToPool(Jedis jedis) {
        if(jedis != null) {
            jedis.close();
        }
    }

    public MiaoShaUser getByToken(String token) {
        if(StringUtils.isEmpty(token))
            return null;
        return get(MiaoShaUserKey.token , token , MiaoShaUser.class);
    }

}

package com.miaomiao.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by cjq on 2018-03-07 15:44
 */

@Service
public class RedisPoolFactory {
    @Autowired
    RedisConfig redisConfig;

    @Bean
    public JedisPool JedisPoolFactory() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(redisConfig.getPoolMaxIdle());
        poolConfig.setMaxTotal(redisConfig.getPoolMaxTotal());
        poolConfig.setMaxWaitMillis(poolConfig.getMaxWaitMillis() * 1000);
        JedisPool jedisPool = new JedisPool(poolConfig , redisConfig.getHost() , redisConfig.getPort() , redisConfig.getTimeout() * 1000);
        return jedisPool;
    }
}

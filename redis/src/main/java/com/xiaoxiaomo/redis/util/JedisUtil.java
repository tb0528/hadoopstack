package com.xiaoxiaomo.redis.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * redis连接池
 * Created by xiaoxiaomo on 2013/4/27.
 */
public class JedisUtil {

    public static final String REDIS_IP = "192.168.33.88" ;
    public static final int REDIS_DEFAULT_PORT = 6379 ;
    private JedisUtil() {}

    private static JedisPool jedisPool = null;

    public static synchronized Jedis getJedis() {

        if( jedisPool == null ){
            JedisPoolConfig config = new JedisPoolConfig() ;
            config.setMaxIdle(500);
            config.setMaxTotal(5000);
            config.setMaxWaitMillis(2000);
            config.setTestOnBorrow(true);
            jedisPool = new JedisPool( config , REDIS_IP ,REDIS_DEFAULT_PORT ) ;
        }

        return jedisPool.getResource();
    }

    public static void returnRource( Jedis jedis ){
        jedis.close();
    }
}

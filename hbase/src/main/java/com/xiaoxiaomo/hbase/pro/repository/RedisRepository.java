package com.xiaoxiaomo.hbase.pro.repository;

import com.xxo.pro.utils.RedisUtils;

/**
 * Redis队列
 * Created by xiaoxiaomo on 2015/5/4.
 */
public class RedisRepository implements Repository {

    RedisUtils redis = new RedisUtils() ;
    @Override
    public String poll() {
        String poll = redis.poll(RedisUtils.heightkey);
        if( poll == null ){
            poll = redis.poll( RedisUtils.lowkey ) ;
        }
        return poll;
    }

    @Override
    public void add(String url) {
        redis.add( RedisUtils.lowkey , url );
    }

    @Override
    public void addHeigh(String url) {
        redis.add( RedisUtils.heightkey ,url );
    }
}

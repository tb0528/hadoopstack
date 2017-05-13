package com.xiaoxiaomo.redis.practice;

import com.xiaoxiaomo.redis.util.JedisUtil;
import redis.clients.jedis.Jedis;

/**
 * Created by xiaoxiaomo on 2013/4/28.
 */
public class InrcId {

    static {
        //初始化一下库存1000件
        Jedis jedis = JedisUtil.getJedis();
        jedis.set("id", "0") ;
        JedisUtil.returnRource(jedis);
    }

    public static void incrIds(String name) {
        Jedis jedis = JedisUtil.getJedis();
        jedis.incr("id");

        System.out.println(name + " " + jedis.get("id"));
        JedisUtil.returnRource(jedis);

    }

    public static void main(String[] args) throws InterruptedException {
        long start_time = System.currentTimeMillis();
        //这里模拟1000个用户
        Thread[] th = new Thread[100000];

        for(int i=0;i<th.length;i++){
            th[i]=new Thread(
                    new Runnable() {
                        public void run() {
                            incrIds(Thread.currentThread().getName());
                        }
                    }
            );

            th[i].start();
        }

        for(Thread t : th) {
            t.join(); // 用join()等待所有的线程
        }

        System.out.println(System.currentTimeMillis()-start_time);
    }
}

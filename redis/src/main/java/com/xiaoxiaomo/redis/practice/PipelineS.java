package com.xiaoxiaomo.redis.practice;

import com.xiaoxiaomo.redis.util.JedisUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

/**
 * Created by xiaoxiaomo on 2013/4/30.
 */
public class PipelineS {

    /**
     * 不使用管道初始化1W条数据
     *
     * 9044
     * @throws Exception
     */
    public void test5() throws Exception {
        Jedis jedis = JedisUtil.getJedis();
        long start_time = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            jedis.set("bb_"+i, i+"");
        }
        System.out.println(System.currentTimeMillis()-start_time);
    }

    /**
     * 使用管道初始化1W条数据
     * 158
     * @throws Exception
     */
    public void test6() throws Exception {
        Jedis jedis = JedisUtil.getJedis();

        long start_time = System.currentTimeMillis();
        Pipeline pipelined = jedis.pipelined();
        for (int i = 0; i < 10000; i++) {
            pipelined.set("cc_"+i, i+"");
        }
        pipelined.sync();//执行管道中的命令
        System.out.println(System.currentTimeMillis()-start_time);
    }
}

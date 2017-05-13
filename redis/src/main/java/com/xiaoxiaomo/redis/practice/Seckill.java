package com.xiaoxiaomo.redis.practice;

import com.xiaoxiaomo.redis.util.JedisUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

/**
 * 秒杀
 * Created by xiaoxiaomo on 2013/4/27.
 */
public class Seckill {

    public static final String TOTO_STOCK_KEY = "TOTO_STOCK" ;//默认库存KAY
    public static final String TOTO_STOCK_VAL= "100" ;//默认库存VALUE


    public static final String SNAG_NUM_KEY= "SNAG_NUM" ;//抢到数量KEY
    public static final String SNAG_USER_KEY= "SNAG_USER" ;//抢到数量KEY


    static {
        //初始化一下库存1000件
        Jedis jedis = JedisUtil.getJedis();
        jedis.set(TOTO_STOCK_KEY, TOTO_STOCK_VAL) ;
        jedis.set(SNAG_NUM_KEY,"0") ;
        JedisUtil.returnRource(jedis);
    }


    public static void fastSeckill2( String user  ) {
        Jedis jedis = JedisUtil.getJedis();
        int stock = Integer.valueOf(jedis.get(TOTO_STOCK_KEY));//库存
        Integer snag_num = Integer.valueOf(jedis.get(SNAG_NUM_KEY));//抢到库存

        try {
            if (snag_num <= stock) {
                jedis.watch(SNAG_USER_KEY);
                Transaction t = jedis.multi();

                //用户抢到了第几件incr

                t.hset(SNAG_USER_KEY, user, (snag_num) + "");
                t.incr(SNAG_NUM_KEY);
                if (t.exec() != null) {
                    System.out.println(user + "，成功抢购到第" + (snag_num) + "件！目前剩余库存：" + (stock - snag_num));
                }

                //抢购失败
                else {
                    System.out.println(user + "，抢购失败！目前剩余库存：" + (stock - snag_num));

                    //如果抢购失败让它等待后继续抢
//                    Thread.sleep( 2000 );
//                    fastSeckill( Thread.currentThread().getName() );
                }
                Thread.sleep(10);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            JedisUtil.returnRource(jedis);
        }
    }




//    public static void fastSeckill( String user  ) {
//        Jedis jedis = JedisUtil.getJedis();
//        int stock = Integer.valueOf(jedis.get(TOTO_STOCK_KEY));//库存
////        Integer snag_num = Integer.valueOf(jedis.get(SNAG_NUM_KEY));//抢到库存
//
//        Transaction t = jedis.multi();
//        Response<Long> incr = t.incr(SNAG_NUM_KEY);
//        if( < stock){
//            t.hset(SNAG_USER_KEY, user, (snag_num) + "");
//            System.out.println(user + "，成功抢购到第" + (snag_num) + "件！目前剩余库存：" + (stock - snag_num));
//
//        }else{
//            System.out.println(user + "，抢购失败！目前剩余库存：" + (stock - snag_num));
//        }
//
//
//    }


        //Test
    public static void main(String[] args) throws InterruptedException {
        //这里模拟1000个用户
        Thread[] th = new Thread[1000];

        for(int i=0;i<th.length;i++){
            th[i]=new Thread(
                new Runnable() {
                    public void run() {
                        fastSeckill2(Thread.currentThread().getName());
                    }
                }
            );

            th[i].start();
        }
    }
}

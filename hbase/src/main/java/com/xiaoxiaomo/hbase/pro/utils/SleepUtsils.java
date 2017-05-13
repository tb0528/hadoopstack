package com.xiaoxiaomo.hbase.pro.utils;

/**
 * Created by xiaoxiaomo on 2015/5/4.
 */
public class SleepUtsils {

    /**
     * 毫秒为单位
     * @param time
     */
    public static void sleep( long time ){
        try {
            Thread.sleep( time );
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

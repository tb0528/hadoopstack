package com.xiaoxiaomo.hbase.pro.repository;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * 带有优先级的队列
 * Created by xiaoxiaomo on 2015/5/4.
 */
public class QueueHLRepository implements Repository {

    Queue<String> queueLow = new ConcurrentLinkedDeque<String>() ;
    Queue<String> queueHeigh = new ConcurrentLinkedDeque<String>() ;

    public String poll() {
        if( queueHeigh != null ){
            return queueHeigh.poll() ;
        }
        return queueLow.poll();
    }

    public void add(String url) {
        queueLow.add(url) ;
    }

    public void addHeigh(String url) {
        queueHeigh.add(url);
    }
}

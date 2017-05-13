package com.xiaoxiaomo.kafka.common.partition;

import kafka.producer.Partitioner;
import kafka.utils.VerifiableProperties;

/**
 * 自定义partition
 * Created by xiaoxiaomo on 2015/5/14.
 */
public class MyPartition implements Partitioner {

    public MyPartition(VerifiableProperties verifiableProperties) {
        //记得要有这个构造函数，不然会报错！
    }

    public int partition(Object key, int numPartitions) {
        if( key == null ) return 0 ;
        Integer k = Integer.parseInt(key+"") ;
        return k % numPartitions;
    }
}

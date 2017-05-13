package com.xiaoxiaomo.hbase.coprocessor;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.coprocessor.AggregationClient;
import org.apache.hadoop.hbase.client.coprocessor.LongColumnInterpreter;
import org.apache.hadoop.hbase.coprocessor.AggregateImplementation;
import org.apache.hadoop.hbase.coprocessor.ColumnInterpreter;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 协处理器 - 行统计
 * Created by xiaoxiaomo on 2015/6/2.
 */
public class RowCountApp {

    private static Logger logger = LoggerFactory.getLogger(RowCountApp.class) ;
    public static void main(String[] args) {

        //1. 实例化一个聚合客户端
        AggregationClient client = new AggregationClient(HBaseConfiguration.create());

        Scan scan = new Scan();
        scan.addFamily(Bytes.toBytes( "f_1" )) ;

        long start= System.currentTimeMillis() ;
        long count = 0 ;
        try {
            count = client.rowCount(TableName.valueOf("t_2"), new LongColumnInterpreter(), scan);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            logger.error( "异常" , throwable );
        }

        logger.info( count+ "，条数据耗时："+(System.currentTimeMillis() - start) );


//2016-06-02 21:02:38 RowCountApp [INFO] 992951，条数据耗时：4156
    }
}

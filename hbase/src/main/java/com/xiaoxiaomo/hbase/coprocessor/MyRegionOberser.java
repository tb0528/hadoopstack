package com.xiaoxiaomo.hbase.coprocessor;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Durability;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.coprocessor.BaseRegionObserver;
import org.apache.hadoop.hbase.coprocessor.ObserverContext;
import org.apache.hadoop.hbase.coprocessor.RegionCoprocessorEnvironment;
import org.apache.hadoop.hbase.regionserver.wal.WALEdit;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.ArrayList;

/**
 * 这里我们实现自己的一个Region观察者，继承BaseRegionObserver
 * 这里我们主要来看一个常见的应用场景，hbase的二级索引
 * 逻辑 上，HBase 的表数据按
 * RowKey 进行字典排序， RowKey 实际上是数据表的一级索引
 * Created by xiaoxiaomo on 2015/6/2.
 */
public class MyRegionOberser extends BaseRegionObserver{


    @Override
    public void postPut(ObserverContext<RegionCoprocessorEnvironment> e, Put put, WALEdit edit, Durability durability) throws IOException {
        super.postPut(e, put, edit, durability);


        //二级索引其实就是另外的建立一张表，我们可以理解为建了一张索引表
        HTable table = new HTable( HBaseConfiguration.create(), "t_2_index");

        //Put信息为 rowKey 和
        //如果我们这里监控一张表，行键：由手机号和时间组成 ，而另一个需求是查询一段时间内所有的手机号，此时原表就无法完成
        //所以我们这里就，做一个行键为：时间组成+手机号，的索引表
        String row = Bytes.toString(put.getRow());

        //索引表行键：
        String rowkey = row.substring( 12 ) + StringUtils.reverse(row.substring(0,11)) ;


        ArrayList<Put> arrayList = new ArrayList<Put>();
        Put newPut = new Put(Bytes.toBytes(rowkey)); //新的Put
        newPut.add(   put.get(Bytes.toBytes("f_1"), Bytes.toBytes("upn") ).get(0)   );
        arrayList.add( newPut ) ;



        table.put( arrayList );
        table.close();
    }
}

package com.xiaoxiaomo.hbase.dml;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HRegionInfo;
import org.apache.hadoop.hbase.ServerName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.NavigableMap;

/**
 *
 * HBase 数据操作
 *
 * 1.使用java代码插入100万记录到一个表。
 * 2.插入记录时，比较使用put.setDurability(Durability.SKIP_WAL);前后的时间差异。
 * 3.使用过滤器写统计100万记录的表行数，计算统计时间。
 * 4.运行hbase    ‘表名称’，统计100万记录的表行数，计算统计时间。
 *
 * Created by xiaoxiaomo on 2015/6/1.
 */
public class HTableApp {

    private static Logger logger = LoggerFactory.getLogger(HTableApp.class) ;
    public static void main(String[] args) throws Exception {

        //1. 实例化一个Table
        HTable hTable = new HTable(HBaseConfiguration.create(), Bytes.toBytes("t_2"));


        //getRegionLocations(hTable);

//        getTableInfo(hTable);
//        Get get = new Get(Bytes.toBytes("r_1"));
//        Result result = hTable.get(get);
       //putData(hTable);

//        scan(hTable);

        count(hTable);

        hTable.close();

    }

    public static void count(HTable hTable) throws IOException {
//        2016-06-01 20:33:00 HTableApp [INFO] 总条数：1000000
//        2016-06-01 20:33:00 HTableApp [INFO] 耗时：20699
//        使用shell，耗时64.9170 seconds
        long start = System.currentTimeMillis();
        FirstKeyOnlyFilter firstKeyOnlyFilter = new FirstKeyOnlyFilter();
        Scan scan = new Scan();
        scan.setFilter(firstKeyOnlyFilter);

        ResultScanner scanner = hTable.getScanner(scan);
        int count = 0;
        for (Result result : scanner) {
            count+=result.size();
        }
        logger.info("总条数："+count);
        logger.info("耗时：" +( System.currentTimeMillis() - start ));
    }

    public static void scan(HTable hTable) throws IOException {
        Scan scan = new Scan();
        FilterList list = new FilterList();
        RowFilter filter = new RowFilter(CompareFilter.CompareOp.EQUAL, new BinaryComparator(Bytes.toBytes("r_2")));
        list.addFilter(filter);

        scan.setFilter(list);

        ResultScanner results = hTable.getScanner(scan);
        for (Result result : results) {
            getTableInfo(result);
        }
    }


    public static void putData(HTable hTable) throws InterruptedIOException, RetriesExhaustedWithDetailsException {
        //测试一十万条数据插入，耗时：15164
        //测试一百万条数据插入，耗时：62242
        //测试一百万条数据插入，耗时：58238 ,设置了Durability.SKIP_WAL
        ArrayList<Put> list = new ArrayList<Put>();
        Put put = null ;
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            put = new Put(Bytes.toBytes("r_"+i));
            put.setDurability(Durability.SKIP_WAL);
            list.add(put.add(Bytes.toBytes("f_1"), Bytes.toBytes("c" + i), Bytes.toBytes(i+"")));
            if( i%1000==0 ){
                hTable.put(list);
                list.clear();
            }
        }
        hTable.put(list);
        logger.info("测试一百万条数据插入，耗时："+(System.currentTimeMillis()-start));
    }

    /**
     * 获取表数据信息
     * @param result
     * @throws IOException
     */
    public static void getTableInfo(Result result) throws IOException {


        logger.info("result："+result.toString());
        NavigableMap<byte[], NavigableMap<byte[], NavigableMap<Long, byte[]>>> map = result.getMap();
        for (Map.Entry<byte[], NavigableMap<byte[], NavigableMap<Long, byte[]>>> navigableMapEntry : map.entrySet()) {

            //logger.info("列族："+Bytes.toString(navigableMapEntry.getKey())); //
            NavigableMap<byte[], NavigableMap<Long, byte[]>> navigableMap = map.get(navigableMapEntry.getKey());
            for (Map.Entry<byte[], NavigableMap<Long, byte[]>> entry : navigableMap.entrySet()) {

                logger.info("列族-限定符："+Bytes.toString(navigableMapEntry.getKey())+"-"+Bytes.toString(entry.getKey()));
                NavigableMap<Long, byte[]> map1 = navigableMap.get(entry.getKey());

                for (Map.Entry<Long, byte[]> longEntry : map1.entrySet()) {

                    //时间戳
                    logger.info("时间戳-值:"+longEntry.getKey()+"-"+Bytes.toString(longEntry.getValue()));
                }

            }

        }
    }


    /**
     * getRegionLocations
     * @param hTable
     * @throws IOException
     */
    private static void getRegionLocations(HTable hTable) throws IOException {
        NavigableMap<HRegionInfo, ServerName> regionLocations = hTable.getRegionLocations();
        for (Map.Entry<HRegionInfo, ServerName> entry : regionLocations.entrySet()) {
            logger.info(String.valueOf(regionLocations.get(entry.getKey())));
        }
    }
}

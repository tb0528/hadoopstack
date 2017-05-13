package com.xiaoxiaomo.hbase.ddl;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.concurrent.Executors;

/**
 * 表操作
 * Created by xiaoxiaomo on 2015/6/1.
 */
public class HBaseAdminApp {

    public static void main(String[] args) throws Exception {

        HConnection connection = getConnection(); //创建一个链接
        HBaseAdmin hBaseAdmin = new HBaseAdmin( connection ); //实例化一个HBaseAdmin

        //ClusterStatus clusterStatus = hBaseAdmin.getClusterStatus(); //获取集群状态
        //System.out.println(clusterStatus.toString());

        HTableDescriptor hTableDescriptor = new HTableDescriptor(TableName.valueOf("t_2"));

        //指定列族
        HColumnDescriptor f_1 = new HColumnDescriptor(Bytes.toBytes("f_1"));
        HColumnDescriptor f_2 = new HColumnDescriptor(Bytes.toBytes("f_2"));
        hTableDescriptor.addFamily(f_1);
        hTableDescriptor.addFamily(f_2);

        hBaseAdmin.createTable(hTableDescriptor);//创建表

        connection.close();

    }

    /**
     * 创建一个连接
     * @return
     * @throws IOException
     */
    private static HConnection getConnection() throws IOException {
        Configuration conf = HBaseConfiguration.create();
        return HConnectionManager.createConnection(conf, Executors.newFixedThreadPool(2));
    }
}

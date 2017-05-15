package com.xiaoxiaomo.hbase.book.ch05.admin;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.util.Bytes;
import util.HBaseHelper;

import java.io.IOException;

/**
 *
 *  Example using the various calls to disable, enable, and check that status of a table
 */
public class TableOperationsExample {

    public static void main(String[] args) throws IOException, InterruptedException {
        Configuration conf = HBaseConfiguration.create();

        HBaseHelper helper = HBaseHelper.getHelper(conf);
        helper.dropTable("testtable");

        Admin admin  = helper.getConnection().getAdmin();

        TableName tableName = TableName.valueOf("testtable");


        //创建表
        HTableDescriptor desc = new HTableDescriptor(tableName);
        HColumnDescriptor columnDescriptor = new HColumnDescriptor(Bytes.toBytes("colfam1"));
//        columnDescriptor.set
        desc.addFamily(columnDescriptor);
        admin.createTable(desc);

        //删除
        try {
            admin.deleteTable(tableName);
        } catch (IOException e) {
            System.err.println("Error deleting table: " + e.getMessage());
        }

        //Disable
        admin.disableTable(tableName);
        boolean isDisabled = admin.isTableDisabled(tableName);

        //TableAvailable
        boolean avail1 = admin.isTableAvailable(tableName);

        //Delete
        admin.deleteTable(tableName);

        //TableAvailable
        boolean avail2 = admin.isTableAvailable(tableName);

        admin.createTable(desc);
        boolean isEnabled = admin.isTableEnabled(tableName);
    }
}

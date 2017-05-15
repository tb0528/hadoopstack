package com.xiaoxiaomo.hbase.book.ch05.admin;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import util.HBaseHelper;

import java.io.IOException;

/**
 *
 * 【表属性】 ： 通常使用get、set方法操作，平时很少用到但了解后对性能改善有很大帮助
 *
 *  1. 文件大小限制：getMaxFileSize()、setMaxFileSize(long maxFileSize)，即每个存储单元的大小限制
 *  2. 只读：isReadOnly()、setReadOnly(boolean readOnly)
 *  3. memstore 刷写大小：getMemstoreFlushSize()、setMemstoreFlushSize(long memstoreFlushSize)
 *      写操作会写入到写缓冲区中，然后按照合适的条件顺序写入到磁盘的一个新存储文件中，这个过程称为刷写（flush）.默认64MB
 *      该参数越大，可以生成的存储文件就越大，文件数越少，同时也会导致更长的阻塞时间问题。这种情况下，
 *      region服务器不能持续接收新增加的数据，请求被阻塞的时间就随之增加了，此外一旦服务器崩溃，通过WAL恢复数据的时间也相应增加了，
 *      且更新的内存丢失。
 *
 *  4. 延时日志刷写：WAL保存到磁盘的两种方式，是否延时。isDeferredLogFlush()、setDeferredLogFlush()
 *
 *
 * Created by TangXD on 2017/5/15.
 */
public class TableInfoExample {

    public static void main(String[] args) throws IOException, InterruptedException {
        HBaseHelper helper = HBaseHelper.getHelper(HBaseConfiguration.create());
        helper.dropTable("testtable");

        Admin admin  = helper.getConnection().getAdmin();

        //创建表
        HTableDescriptor desc = new HTableDescriptor(TableName.valueOf("testtable"));
        HColumnDescriptor columnDescriptor = new HColumnDescriptor(Bytes.toBytes("colfam1"));
//        columnDescriptor.set
        desc.addFamily(columnDescriptor);
        admin.createTable(desc);

        Table table = helper.getConnection().getTable(TableName.valueOf("testtable"));


        //表属性
        HTableDescriptor tableDescriptor = table.getTableDescriptor();
        System.out.println(tableDescriptor.getMaxFileSize());
        System.out.println(tableDescriptor.isReadOnly());
        System.out.println(tableDescriptor.getMemStoreFlushSize());


    }


}

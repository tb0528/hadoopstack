package com.xiaoxiaomo.hbase.coprocessor;

import org.apache.hadoop.hbase.HRegionInfo;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.coprocessor.BaseMasterObserver;
import org.apache.hadoop.hbase.coprocessor.MasterCoprocessorEnvironment;
import org.apache.hadoop.hbase.coprocessor.ObserverContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Observer类似于触发器
 * HBase 中 的 observer 分 为 三 类 ， MasterObserver 、 RegionObserver 、 WALOBserver
 * 自定义一个master 观察者
 * 如下面我们创建表或者删除表的时候提示我们相应信息，当然我们这里只是简单的打印了信息，你可以去做任何其他你想做的事情
 * Created by xiaoxiaomo on 2015/6/2.
 */
public class MyMasterObserver extends BaseMasterObserver {
    private static Logger logger = LoggerFactory.getLogger(MyMasterObserver.class) ;


    //1. 继承 BaseMasterObserver
    //2. 打成 jar 包，放到 hbase 的 lib 目录下
    //3. 修改 hbase 的配置文件 hbase-site.xml 文件
    //4. 重启 HBase 集群
    @Override
    public void preCreateTable(ObserverContext<MasterCoprocessorEnvironment> ctx, HTableDescriptor desc, HRegionInfo[] regions) throws IOException {

        logger.info("=============创建表之前，操作===========");
        logger.info("HTableDescriptor: " + desc);
        for (HRegionInfo region : regions) {
            logger.info( "regions: "+ region.getRegionNameAsString() );
        }
        super.preCreateTable(ctx, desc, regions);
    }

    @Override
    public void postCreateTable(ObserverContext<MasterCoprocessorEnvironment> ctx, HTableDescriptor desc, HRegionInfo[] regions) throws IOException {
        super.postCreateTable(ctx, desc, regions);

        logger.info("=============已经CreateTable，操作===========" );
    }


    @Override
    public void preDeleteTable(ObserverContext<MasterCoprocessorEnvironment> ctx, TableName tableName) throws IOException {
        logger.warn("=============即将删除表："+ tableName );
        super.preDeleteTable(ctx, tableName);
    }


    @Override
    public void postDeleteTable(ObserverContext<MasterCoprocessorEnvironment> ctx, TableName tableName) throws IOException {
        logger.warn("=============已经删除表：" + tableName );
        super.postDeleteTable(ctx, tableName);
    }
}

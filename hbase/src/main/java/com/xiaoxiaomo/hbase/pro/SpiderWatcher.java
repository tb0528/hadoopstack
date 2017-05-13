package com.xiaoxiaomo.hbase.pro;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.util.List;

/**
 * 创建一个监视器，这个监视器需要实现watcher接口
 * 接口中有一个process方法。
 * 当监视器发现监视的节点发生变化的时候，这个process方法会被调用
 *
 * 所以这个监视器是一个守护进程，也就是说一个永远不会停止的进程，类似于死循环
 * Created by xiaoxiaomo on 2015/5/5.
 */
public class SpiderWatcher implements Watcher {

    CuratorFramework client ;
    List<String> chiList ;
    List<String> newChiList ;

    public SpiderWatcher() {

        //指定zk集群的地址
        String connectStr = "192.168.3.220:2181,192.168.3.221:2181,192.168.3.222:2181";

        //1000 ：代表是重试时间间隔     3：表示是重试次数
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3) ;

        //使用curator创建一个zk链接
        int sessionTimeoutMs = 3000 ;   //这个值必须在4s--40s之间，表示是链接失效的时间
        int connectionTimeoutMs = 4000 ;//链接超时时间
        client = CuratorFrameworkFactory.newClient(connectStr, sessionTimeoutMs, connectionTimeoutMs, retryPolicy);

        client.start(); //开启这个链接

        try {
            //使用SpiderWater监视Spider节点下面节点的所有变化
            //(想spider节点注册监视器，监视器需要重复注册)
            chiList =  client.getChildren().usingWatcher(this).forPath("/spider");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void process(WatchedEvent event) {
        try {

            //重复注册监视器
            newChiList = client.getChildren().usingWatcher(this).forPath("/spider");
            for (String s : chiList) {
                if( !newChiList.contains(s) ){
                    System.out.println("节点消失："+s);
                    //给管理员发送短信，或者邮件
                    //发短信的话可以使用一些第三方平台 云片网
                    //发邮件的话使用  javamail
                }
            }


            for (String s : chiList) {
                if( !chiList.contains(s) ){
                    System.out.println("节点新增："+s);
                }
            }

            this.chiList = newChiList ;

        } catch (Exception e) {
            e.printStackTrace();
        }

//        System.out.println("节点发生变化，"+event);
    }

    public void start(){
        //为了保证让这个方法一直运行
        while (true){
            ;
        }
    }

    public static void main(String[] args) {
        SpiderWatcher spiderWatcher = new SpiderWatcher();
        spiderWatcher.start();
    }
}

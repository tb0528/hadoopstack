package com.xxo.rpc;

import com.xxo.rpc.LoginServiceInterface;
import com.xxo.rpc.imp.LoginServiceImp;
import com.xxo.rpc.imp.NameNodeNameSystem;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;

import java.io.IOException;

/**
 * 发布一个RPC服务
 * Created by xiaoxiaomo on 2016/8/13.
 */
public class PublishServer {

    public static void main(String[] args) throws IOException {

        //1. 创建一个RPC Builder,并设置参数（IP、端口、接口和实现类）
        RPC.Builder builder1 = new RPC.Builder(new Configuration());
        builder1.setBindAddress("xxo07")
                .setPort(1888)
                .setProtocol(LoginServiceInterface.class)
                .setInstance(new LoginServiceImp()) ;

        //2. 调用build()方法创建
        RPC.Server server1 = builder1.build();

        //3. 启动一个RPC服务

        System.out.println("server1 启动了。。。。。。。。。。。。");
        server1.start();




        //NameNode
        RPC.Builder builder2 = new RPC.Builder(new Configuration());
        builder2.setBindAddress("xxo07")
                .setPort(1889)
                .setProtocol(ClientNameNodeProtocol.class)
                .setInstance(new NameNodeNameSystem()) ;

        //2. 调用build()方法创建
        RPC.Server server2 = builder2.build();

        //3. 启动一个RPC服务
        System.out.println("server2 启动了。。。。。。。。。。。。");
        server2.start();

    }


}

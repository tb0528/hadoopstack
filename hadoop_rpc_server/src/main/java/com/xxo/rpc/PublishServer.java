package com.xxo.rpc;

import com.xxo.rpc.LoginServiceInterface;
import com.xxo.rpc.imp.LoginServiceImp;
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
        RPC.Builder builder = new RPC.Builder(new Configuration());
        builder.setBindAddress("xxo07")
                .setPort(1888)
                .setProtocol(LoginServiceInterface.class)
                .setInstance(new LoginServiceImp()) ;

        //2. 调用build()方法创建
        RPC.Server server = builder.build();

        //3. 启动一个RPC服务
        server.start();

    }


}

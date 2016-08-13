package com.xxo.rpc.controller;

import com.xxo.rpc.LoginServiceInterface;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * RPC
 * 模拟用户请求
 * Created by xiaoxiaomo on 2016/7/17.
 */
public class UserLoginController {

    public static void main(String[] args) throws IOException {

        //1. 创建一个代理对象
        LoginServiceInterface loginService = RPC.getProxy(
                LoginServiceInterface.class,
                1L,
                new InetSocketAddress("xxo07", 1888),
                new Configuration());

        //2. 调用具体方法
        String result = loginService.login("xiaoxiaomo", "blog.xiaoxiaomo.com");

        System.out.println(result);


    }
}

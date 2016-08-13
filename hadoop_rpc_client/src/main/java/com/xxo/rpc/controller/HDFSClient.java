package com.xxo.rpc.controller;

import com.xxo.rpc.ClientNameNodeProtocol;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created by xiaoxiaomo on 2016/8/13.
 */
public class HDFSClient {

    public static void main(String[] args) throws IOException {
        ClientNameNodeProtocol client = RPC.getProxy(
                ClientNameNodeProtocol.class,
                11L,
                new InetSocketAddress("xxo07", 1889),
                new Configuration());

        String data = client.getMetaData("hdfs://192.168.33.99/");
        System.out.println(data);


    }
}

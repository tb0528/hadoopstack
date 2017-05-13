package com.xiaoxiaomo.storm.drpc;

import org.apache.thrift7.TException;

import backtype.storm.generated.DRPCExecutionException;
import backtype.storm.utils.DRPCClient;

public class DrpcClientTest {
	
	public static void main(String[] args) {
		//通过客户端链接到drpcserver
		DRPCClient drpcClient = new DRPCClient("192.168.1.171", 3772);
		try {
			String result = drpcClient.execute("hello", "hahah");
			System.out.println("服务端返回的结果："+result);
		} catch (TException e) {
			e.printStackTrace();
		} catch (DRPCExecutionException e) {
			e.printStackTrace();
		}
	}

}

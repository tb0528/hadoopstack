package com.xiaoxiaomo.rpc;

/**
 * HDFS客户端跟NameNode的远程过程调用使用的协议（接口）
 * Created by xiaoxiaomo on 2016/8/13.
 */
public interface ClientNameNodeProtocol {

    public static final long versionID = 11 ;
    public String getMetaData(String path) ;
}

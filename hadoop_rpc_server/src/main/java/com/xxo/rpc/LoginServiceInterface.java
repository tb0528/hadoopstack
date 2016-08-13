package com.xxo.rpc;

/**
 * RPC - 服务端，用户登录接口
 * Created by xiaoxiaomo on 2016/7/17.
 */
public interface LoginServiceInterface {
    public static final long versionID = 1 ;//服务端版本号和客户端保持一致

    public String login(String username, String password) ;
}

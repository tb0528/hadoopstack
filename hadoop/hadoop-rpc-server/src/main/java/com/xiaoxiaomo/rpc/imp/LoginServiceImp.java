package com.xiaoxiaomo.rpc.imp;

import com.xiaoxiaomo.rpc.LoginServiceInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RPC - 服务端，接口实现
 * Created by xiaoxiaomo on 2016/7/17.
 */
public class LoginServiceImp implements LoginServiceInterface{
    private static final Logger logger = LoggerFactory.getLogger( LoginServiceImp.class ) ;
    @Override
    public String login(String username, String password) {
        logger.debug( username + " login !~~~~~~~~~~~~~" );
        return "login success :" + username ;
    }
}

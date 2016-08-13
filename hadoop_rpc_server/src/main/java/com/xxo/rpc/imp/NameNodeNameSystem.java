package com.xxo.rpc.imp;

import com.xxo.rpc.ClientNameNodeProtocol;

/**
 * NameNode具体实现
 * Created by xiaoxiaomo on 2016/8/13.
 */
public class NameNodeNameSystem implements ClientNameNodeProtocol {
    @Override
    public String getMetaData(String path) {

        System.out.println("............getMetaData...........");
        return  path + "\n[blk_01,blk_02,blk_03]\n{blk_01:node01,node05}" ;
    }
}

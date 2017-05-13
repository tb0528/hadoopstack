package com.xiaoxiaomo.hbase.pro.download;

import com.xxo.pro.domain.Page;

/**
 * Created by xiaoxiaomo on 2015/5/3.
 */
public interface Downloadable {

    /**
     * 通过URL下载一个页面
     * @param url
     * @return
     */
    public Page download(String url) ;
}

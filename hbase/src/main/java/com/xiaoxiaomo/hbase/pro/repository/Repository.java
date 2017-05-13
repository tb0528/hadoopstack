package com.xiaoxiaomo.hbase.pro.repository;

/**
 * URL资源
 * Created by xiaoxiaomo on 2015/5/4.
 */
public interface Repository {

    String poll() ;

    void add(String url) ;

    void addHeigh(String url) ;
}

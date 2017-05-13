package com.xiaoxiaomo.hbase.pro.utils;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * 页面工具类
 * Created by xiaoxiaomo on 2015/5/3.
 */
public class PageUtils {

    /**
     * 通过URL获取信息
     * @param url
     * @return
     */
    public static String getContentByURL(String url) {

        //1. 通过HttpClients获取一个构造器
        HttpClientBuilder builder = HttpClients.custom();

        //2. 通过构造器获取一个httpClient对象
        CloseableHttpClient client = builder.build();

        //3. 通过执行httpClient的execute方法请求一个url并返回response对象
        try {
            CloseableHttpResponse response = client.execute(new HttpGet(url));
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity);
        } catch (IOException e) {
            e.printStackTrace();
        }

       return null ;
    }
}

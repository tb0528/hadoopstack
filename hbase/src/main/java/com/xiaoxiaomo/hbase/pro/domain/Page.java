package com.xiaoxiaomo.hbase.pro.domain;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Page
 * Created by xiaoxiaomo on 2015/5/3.
 */
public class Page {

    private String url ;

    //页面所用内容
    private String content ;

    /**
     * 临时保存列表页面解析出来的url
     */

    private List<String> urls = new ArrayList<String>();

    //页面详情属性
    private JSONObject prop = new JSONObject();


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public JSONObject getProp() {
        return prop;
    }

    public void setProp(String k , String v) {
        prop.put(k ,v) ;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void addUrl(String url){
        this.urls.add(url);
    }
}

package com.xiaoxiaomo.hbase.pro.process;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xxo.pro.domain.Page;
import com.xxo.pro.utils.HtmlUtils;
import com.xxo.pro.utils.PageUtils;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xiaoxiaomo on 2015/5/3.
 */
public class ProcessJDMobileImpl implements Processable {

    private Logger logger = LoggerFactory.getLogger(ProcessJDMobileImpl.class);
    public Page process( Page page ) {

        //页面URL
        String url = page.getUrl();
        if( url == null ){
            logger.error("url为空!");
            throw new RuntimeException("url为空") ;
        }

        HtmlCleaner cleaner = new HtmlCleaner();
        TagNode rootNode = cleaner.clean(page.getContent());

        //JD 手机列表页
        if( url.startsWith("http://list.jd.com/list.html") ){
            return processMobList(page , rootNode);
        }
        //JD 手机详情页面
        else{
            return processMobDetail(page , rootNode);
        }
    }

    /**
     * 解析页面列表信息
     * @param page
     * @return
     */
    private Page processMobList(Page page ,TagNode rootNode) {

        try {
            //1. 获取列表URL
            Object[] objects = rootNode.evaluateXPath("//*[@id=\"plist\"]/ul/li/div/div[1]/a");
            for (Object object : objects) {
                TagNode aNode = ( TagNode ) object ;
                page.addUrl("http:" + aNode.getAttributeByName("href"));
            }

            //2. 获取页面下一页URL
            Object[] nextObj = rootNode.evaluateXPath("//*[@id=\"J_topPage\"]/a[2]");
            if( nextObj != null && nextObj.length > 0 ){
                TagNode nextNode = (TagNode) nextObj[0];
                String nextHref = nextNode.getAttributeByName("href");
                if( !"javascript:;".equals( nextHref ) ){
                    String href = "http://list.jd.com"+ nextHref.replaceAll("\\^\\^", "");
                    page.addUrl(href);
                }else {
                    logger.info( "抓取页面列表结束！" );
                }
            }

        } catch (XPatherException e) {
            logger.info( "获取xpath路径异常！" , e );
        }

        return page;
    }


    /**
     * 获取手机详细信息
     * @param page
     * @return
     */
    private Page processMobDetail( Page page , TagNode rootNode ) {

        page.setProp( "title", HtmlUtils.getText(rootNode, "//*[@id=\"name\"]/h1"));
        page.setProp( "img", HtmlUtils.getAttributeByName(rootNode, "//*[@id=\"spec-n1\"]/img", "src"));
        page.setProp( "img", HtmlUtils.getAttributeByName(rootNode, "//*[@id=\"spec-n1\"]/img", "src"));
        page.setProp( "price" , getJDPrice(page) );

        try {
            JSONArray jsonArray = new JSONArray() ;
            JSONObject jsonObject = null ;
            Object[] objects = rootNode.evaluateXPath("//*[@id=\"product-detail-2\"]/table/tbody/tr");
            for (Object object : objects) {
                TagNode trNode = (TagNode) object;
                if( trNode != null && !"".equals(trNode.getText().toString().trim()) ){
                    jsonObject = new JSONObject() ;
                    //判断th出现的情况
                    Object[] thObj = trNode.evaluateXPath("/th");
                    if( thObj!=null && thObj.length>0 ){
                        //th
                        TagNode thNode = (TagNode) thObj[0];
                        jsonObject.put( "name", thNode.getText().toString()) ;
                        jsonObject.put( "value" , "") ;
                    }

                    //判断td出现的情况
                    Object[] tdObj = trNode.evaluateXPath("/td");
                    if( tdObj!=null && tdObj.length>0 ) {

                        TagNode tdNode0 = (TagNode) tdObj[0];
                        TagNode tdNode1 = (TagNode) tdObj[1];
                        jsonObject.put( "name" ,  tdNode0.getText().toString()) ;
                        jsonObject.put( "value" , tdNode1.getText().toString()) ;
                    }
                    jsonArray.add(jsonObject) ;
                }
            }

            page.setProp("property", jsonArray.toString());
        } catch (XPatherException e) {
            e.printStackTrace();
        }
        return page;
    }


    /**
     * 获取手机价格
     * @param page
     * @return
     */
    private String getJDPrice(Page page ) {
        String price = null ;
        String url = page.getUrl();
        Pattern compile = Pattern.compile("http://item.jd.com/([0-9]+).html");
        Matcher matcher = compile.matcher(url);
        String goodId = null;
        if( matcher.find() ){
            goodId = matcher.group(1) ;
            page.setProp("goodId" , "JD_" + goodId );
        }
        String content = PageUtils.getContentByURL("http://p.3.cn/prices/get?skuid=J_" + goodId);
        if( content != null && content.length() > 0 ){
            JSONArray jsonArray = JSON.parseArray(content);
            for (Object o : jsonArray) {
                JSONObject jsonObject = (JSONObject) o;
                price = jsonObject.getString("p");
            }
        }
        return price;
    }
}

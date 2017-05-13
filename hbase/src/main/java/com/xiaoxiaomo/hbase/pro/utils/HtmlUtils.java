package com.xiaoxiaomo.hbase.pro.utils;

import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

/**
 * Created by xiaoxiaomo on 2015/4/29.
 */
public class HtmlUtils {

    public static String getText(TagNode tagNode , String xpath){
        try {
            Object[] evaluateXPath = tagNode.evaluateXPath(xpath);
            if( evaluateXPath!=null && evaluateXPath.length > 0 ){
                TagNode node = (TagNode) evaluateXPath[0];
                return node.getText().toString() ;
            }

        } catch (XPatherException e) {
            e.printStackTrace();
        }
        return null ;
    }

    public static String getAttributeByName(TagNode tagNode , String xpath , String attr ){
        try {
            Object[] evaluateXPath = tagNode.evaluateXPath(xpath);
            if( evaluateXPath!=null && evaluateXPath.length > 0 ){
                TagNode node = (TagNode) evaluateXPath[0];
                return node.getAttributeByName(attr);
            }

        } catch (XPatherException e) {
            e.printStackTrace();
        }
        return null ;
    }

}

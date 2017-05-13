package com.xiaoxiaomo.hbase.pro.process;

import com.xxo.pro.utils.HtmlUtils;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Mapper;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xiaoxiaomo on 2015/6/3.
 */
public class ParserMapper extends Mapper<Text,BytesWritable,Text,Text>{

    private static final Logger logger = LoggerFactory.getLogger(ParserMapper.class);
    HtmlCleaner cleaner = new HtmlCleaner();    //解析器

    /**
     * 解析
     */
    @Override
    protected void map(Text key, BytesWritable value, Context context) throws IOException, InterruptedException {

        //1. 用户id,可以通过文件名来获取
        logger.info( " html: "+ key );
        String id = null ;
        Pattern compile = Pattern.compile("([0-9]+).html");
        Matcher matcher = compile.matcher(key.toString());
        if( matcher.find() ){
            id = matcher.group(1);
        }

        //2. 用户信息
        String pageCount = Bytes.toString(serialize(value));
        System.out.println(pageCount.substring(4));
        TagNode rootNode = cleaner.clean(pageCount.substring(4)) ;

        String name = HtmlUtils.getText(rootNode, "/html/body/div[2]/div[2]/div/div/div/div[2]/div[1]/h2");
        String[] properties = new String[]{ "0","0","0"} ;
        try {
            Object[] objects = rootNode.evaluateXPath("/html/body/div[2]/div[3]/div/div[1]/div[1]/div/div[1]/ul/li/a/strong");
            if( objects != null && objects.length > 2 ) {
                for (  int i = 0; i < 3; i++) {
                    TagNode trNode = (TagNode) objects[i];
                    properties[i] = trNode != null?trNode.getText().toString().trim():"0";
                }
            }
        } catch (XPatherException e) {
            logger.error("用户信息解析异常", e);
            e.printStackTrace();
        }



        //用户消费记录表
        String shop = null ;
        String shopId = null ;
        String pinfo = null ;
        String date = null ;
        try {
            Object[] objects2 = rootNode.evaluateXPath(
                    "/html/body/div[2]/div[3]/div/div[2]/div[1]");
            if( objects2 != null && objects2.length > 2 ) {
                TagNode divNode = (TagNode) objects2[0];

                //评论
                Object[] objects3 = divNode.evaluateXPath("/div[2]/div[1]/ul/li/div/div[1]/h6/a");
                for (Object o : objects3) {
                    //遍历所有评论
                    TagNode aNode = (TagNode) o;
                    shop = aNode != null?aNode.getText().toString().trim():"0";
                    String href = aNode.getAttributeByName("href");//href="/shop/18182261"
                    if( href!=null && href.length() > 6 ){
                        shopId = href.substring(6);
                    }

                    pinfo = HtmlUtils.getText(divNode, "/div[2]/div[1]/ul/li[1]/div/div[2]");
                    date = HtmlUtils.getText(divNode, "/div[2]/div[1]/ul/li[1]/div/div[3]/span[1]");


                    //reviews

                }



            }



        } catch (XPatherException e) {
            logger.error( "用户消费记录信息解析异常" , e );
            e.printStackTrace();
        }


    }



    private static void formatPrint(String type, String param, String hex, int length) {

        String format = "%1$-50s %2$-16s with length: %3$2d%n";
        System.out.format(format, "Byte array per " + type
                + "("+ param +") is:", hex, length);

    }

    /**
     * Utility method to serialize Writable object, return byte array
     * representing the Writable object
     *
     * */
    public static byte[] serialize(Writable writable) throws IOException {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream dataOut = new DataOutputStream(out);
        writable.write(dataOut);
        dataOut.close();

        return out.toByteArray();

    }

    /**
     * Utility method to deserialize input byte array, return Writable object
     *
     * */
    public static Writable deserialize(Writable writable, byte[] bytes)
            throws IOException {

        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        DataInputStream dataIn = new DataInputStream(in);
        writable.readFields(dataIn);

        dataIn.close();
        return writable;

    }
}

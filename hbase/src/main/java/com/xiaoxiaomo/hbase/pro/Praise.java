package com.xiaoxiaomo.hbase.pro;

import com.xxo.pro.process.ParserConfig;
import com.xxo.pro.utils.SequenceLocalFile2HDFS;
import org.apache.hadoop.fs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by xiaoxiaomo on 2015/6/3.
 */
public class Praise {


    private static final Logger logger = LoggerFactory.getLogger(SequenceLocalFile2HDFS.class) ;

    public static void main(String[] args) {

        //1. 序列化本地文件到hdfs
//        try {
//            SequenceLocalFile2HDFS.sequenceList( "F:\\members" , new Path( "/data/members.seq" ));
//            SequenceLocalFile2HDFS.sequenceList( "F:\\shops" , new Path( "/data/shops.seq" ));
//        } catch (IOException e) {
//            e.printStackTrace();
//            logger.error( " 序列化本地文件到hdfs失败！ " , e );
//        }


        //2. 通过mapper解析
        try {
            ParserConfig.init( new Path("/data/members.seq") );

        } catch (Exception e) {
            e.printStackTrace();
        }


        //3.

    }


}

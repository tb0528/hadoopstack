package com.xiaoxiaomo.hbase.pro.utils;

import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * 使用SequenceFile 存储小文件
 * Created by xiaoxiaomo on 2015/6/3.
 */
public class SequenceLocalFile2HDFS {

    private static final Logger logger = LoggerFactory.getLogger( SequenceLocalFile2HDFS.class ) ;

    /**
     * 序列化文件夹中的文件到hdfs
     * @param localDIR 本地文件夹
     * @param dfsPath HDFS路径
     * @throws IOException
     */
    public static void sequenceList(String localDIR , Path dfsPath ) throws IOException {

        Configuration conf = new Configuration();
        FileSystem fileSystem = null;
        SequenceFile.Writer writer = null ;
        try {
            fileSystem = FileSystem.newInstance(conf);
            writer = new SequenceFile.Writer( fileSystem ,conf ,dfsPath, Text.class , BytesWritable.class) ;
        } catch (IOException e) {
            logger.error( " 实例化失败！ " , e );
            throw  e ;
        }

        //遍历文件，并写到到SequenceFile中
        File file = new File( localDIR );
        File[] files = file.listFiles();
        for (File f : files) {
            byte[] bytes = FileUtils.readFileToByteArray(f);
            writer.append(new Text( f.getName() ), new BytesWritable(bytes));
            logger.debug(f.getName());
        }
        writer.close();
    }
}

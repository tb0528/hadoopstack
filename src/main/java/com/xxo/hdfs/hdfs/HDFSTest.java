package com.xxo.hdfs.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * HDFSTest 测试
 * 一些简单的HDFS操作
 * 查看、上传、下载
 * Created by xiaoxiaomo on 2016/5/9.
 */
public class HDFSTest {

    public static void main(String[] args) throws IOException, URISyntaxException {
        //获取一个FileSystem实例
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.newInstance(conf);

//        listStatus(fs);
        create(conf, fs);

//        open(conf, fs);


    }

    /**
     * 打开一个文件
     * @param conf
     * @param fs
     * @throws IOException
     */
    private static void open(Configuration conf, FileSystem fs) throws IOException {
        FSDataInputStream open = fs.open(new Path("/blog"));
        IOUtils.copyBytes(open, System.out, conf, true);
    }

    /**
     * 上传一个文件
     * @param conf
     * @param fs
     * @throws IOException
     */
    private static void create(Configuration conf, FileSystem fs) throws IOException {
        //1. 上传数据 - 一个FSDataOutputStream
        FSDataOutputStream creStream = fs.create(new Path("/aaaa/123.txt"));
        //2. 读入数据源
        FileInputStream fin = new FileInputStream("F:\\123.txt");

        //3. 复制数据
        IOUtils.copyBytes(fin, creStream, conf, true);
    }

    /**
     * 获取列表信息
     * @param fs
     * @throws IOException
     */
    private static void listStatus(FileSystem fs) throws IOException {
        FileStatus[] listStatus = fs.listStatus(new Path("/"));
        for (FileStatus listStatu : listStatus) {
            System.out.println(listStatu.toString());
        }
    }
}

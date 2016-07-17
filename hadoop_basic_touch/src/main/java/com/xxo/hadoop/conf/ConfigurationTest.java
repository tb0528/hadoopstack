package com.xxo.hadoop.conf;

import org.apache.hadoop.conf.Configuration;

import java.io.IOException;

/**
 * Hadoop 配置文件
 * org.apache.hadoop.conf.Configuration
 * 如果core-site里面没有配置，就会去加载默认的配置文件
 * 默认配置文件：core-default.xml
 * Created by xiaoxiaomo on 2014/5/9.
 */
public class ConfigurationTest {

    public static void main(String[] args) throws IOException {
        //1. 创建一个Configuration实例对象，即会去加载配置
        Configuration conf = new Configuration();

        //2. 可以获取/操作配置文件
        conf.set("www", "xiaoxiaomo.com");

        conf.writeXml(System.out);

        System.out.println(conf.get(""));
    }
}

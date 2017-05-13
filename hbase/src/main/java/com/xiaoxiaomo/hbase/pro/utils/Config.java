package com.xiaoxiaomo.hbase.pro.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 加载配置文件
 * Created by xiaoxiaomo on 2015/5/4.
 */
public class Config {

    static Properties properties ;
	static {
        InputStream inputStream = Config.class.getClassLoader().getResourceAsStream("conf.properties");
        properties = new Properties();
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int NTHREAD = Integer.parseInt(properties.getProperty("nThread"));
    public static long MILLION_1 = Long.parseLong(properties.getProperty("million_1"));
    public static long MILLION_5 = Long.parseLong(properties.getProperty("million_5"));

}

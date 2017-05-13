package com.xiaoxiaomo.kafka.common.project;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * 模拟生产用户访问数据
 * Created by xiaoxiaomo on 2015/5/14.
 */
public class ProduceUserData {

    static Map<Integer,String> path = new HashMap<Integer, String>();
    static Map<Integer,Integer> userId = new HashMap<Integer, Integer>();

    public static void main(String[] args) throws IOException, InterruptedException {

        //1. 创建一个生产者对象
        Properties prop = new Properties();
        prop.load( ProduceUserData.class.getClassLoader().getResourceAsStream("producer.properties") );
        Producer<String, String> producer = new Producer<String, String>(new ProducerConfig( prop ));

        //2. 构造message
        String topic = "world" ;  //world 主题
        List<KeyedMessage<String,String>> list = new ArrayList<KeyedMessage<String, String>>() ;

        //用户数据
        //init path
        path.put(0,"http://xiaoxiaomo.com/");
        path.put(1,"http://blog.xiaoxiaomo.com/");
        path.put(2,"http://blog.xiaoxiaomo.com/archives/");
        path.put(3,"http://blog.xiaoxiaomo.com/photo/");
        path.put(4,"http://blog.xiaoxiaomo.com/about/");

        //init userId
        userId.put(0,2010);
        userId.put(1,1001);
        userId.put(2,1002);
        userId.put(3,2001);
        userId.put(4,2002);
        userId.put(5,3003);
        userId.put(6,4004);
        userId.put(7,1007);
        userId.put(8,1008);
        userId.put(9,1009);


        Random random = new Random();
        while ( true ){
            //随机产生数据
            int pathIndex=random.nextInt(5);
            int userIndex=random.nextInt(10);
            int isVip=random.nextInt(2);

            //构造一个用户访问数据
            String visData = String.format("%s\t%s\t%s\t%s",
                    userId.get(userIndex),
                    path.get(pathIndex),
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), isVip) ;

            //使用了pathIndex作为一个Key分区用
            list.add( new KeyedMessage<String, String>( topic , String.valueOf(pathIndex) , visData ) );

            //3. 发送
            producer.send(list);

            Thread.sleep( random.nextInt(2000) );
        }

        //4. 关闭
        //producer.close();
    }
}

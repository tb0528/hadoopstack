package com.xiaoxiaomo.kafka.common;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;

import java.io.IOException;
import java.util.*;

/**
 * 消费者
 * 处理用户访问日志信息
 * Created by xiaoxiaomo on 2015/5/14.
 */
public class ConsumerUserVisit {

    private static int times = 0;
    static Map<String,Integer> map = new HashMap<String, Integer>();

    public static void main(String[] args) throws IOException {

        //1. 创建消费者
        Properties prop = new Properties();
        prop.load( ConsumerUserVisit.class.getClassLoader().getResourceAsStream( "consumer.properties" ) );
        ConsumerConnector connector = Consumer.createJavaConsumerConnector(new ConsumerConfig(prop));

        //2.
        String topic = "world" ;
        Map<String, Integer> topicCountMap =  new HashMap<String, Integer>();
        topicCountMap.put( topic , 3 ) ; //这里启用了三个消费者线程

        Map<String, List<KafkaStream<byte[], byte[]>>> streams = connector.createMessageStreams(topicCountMap);
        List<KafkaStream<byte[], byte[]>> streamList = streams.get(topic);
        for (KafkaStream<byte[], byte[]> stream : streamList) {
            ConsumerIterator<byte[], byte[]> iterator = stream.iterator();
            new Thread( new RunConsumer(  iterator ) ).start();
        }

        //处理访问数据
        Timer timer = new Timer(); //定时来打印一下信息
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                synchronized (map) {

                    for (String key:map.keySet()){
                        System.out.println(String.format("访问path： %s, 的次数： %s",key,map.get(key)));
                    }
                    System.out.println("总访问次数： "+times);
                    map.clear();
                    times=0;
                }
            }
        } , 0 ,5*1000 );

    }

    /**
     * 多线程处理数据，防阻塞
     */
    public static class RunConsumer implements Runnable {

        private ConsumerIterator<byte[], byte[]> iterator ;
        public RunConsumer(ConsumerIterator<byte[], byte[]> iterator) {
            this.iterator = iterator ;
        }

        public void run() {
            while ( iterator.hasNext() ){
                MessageAndMetadata<byte[], byte[]> next = iterator.next();
                String str = new String(next.message());
                String[] strs = str.split("\t");
                Integer key = map.get(strs[1]);
                synchronized ( map ) {
                    int count = 1 ;
                    if( key != null  ){
                        count += key ;
                    }
                    map.put( strs[1] , count ) ;
                    times ++ ;
                }
            }
        }
    }
}

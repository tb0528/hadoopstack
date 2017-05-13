package com.xiaoxiaomo.kafka.produce;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import java.io.IOException;
import java.util.Properties;

/**
 * 生产者
 * alter+shift+insert 列选择模式
 * Created by xiaoxiaomo on 2015/5/12.
 */
public class ProduceTest {

    public static void main(String[] args) throws IOException, InterruptedException {


        //1. 创建生产者
        Properties props = new Properties();
        props.load(ProduceTest.class.getClassLoader().getResourceAsStream("producer.properties"));
        Producer<String, String> producer = new Producer<String, String>(new ProducerConfig(props));


        //2. 组装消息对象
        String topic = "hello";

        KeyedMessage<String, String> message1 = new KeyedMessage<String, String>(topic, "1", "k1 " + System.currentTimeMillis());
        KeyedMessage<String, String> message2 = new KeyedMessage<String, String>(topic, "2", "k2 " + System.currentTimeMillis());
        KeyedMessage<String, String> message3 = new KeyedMessage<String, String>(topic, "3", "k3 " + System.currentTimeMillis());
        KeyedMessage<String, String> message4 = new KeyedMessage<String, String>(topic, "4", "k4 " + System.currentTimeMillis());
        KeyedMessage<String, String> message5 = new KeyedMessage<String, String>(topic, "5", "k5 " + System.currentTimeMillis());
        KeyedMessage<String, String> message6 = new KeyedMessage<String, String>(topic, "6", "k6 " + System.currentTimeMillis());
        KeyedMessage<String, String> message7 = new KeyedMessage<String, String>(topic, "7", "k7 " + System.currentTimeMillis());
        KeyedMessage<String, String> message8 = new KeyedMessage<String, String>(topic, "8", "k8 " + System.currentTimeMillis());

        //3. send
        producer.send(message1);
        producer.send(message2);
        producer.send(message3);
        producer.send(message4);
        producer.send(message5);
        producer.send(message6);
        producer.send(message7);
        producer.send(message8);

        //3.1 send list
//            List<KeyedMessage<String, String>> list = new ArrayList<KeyedMessage<String, String>>();
//            producer.send(list);
//            producer.send( new KeyedMessage<String, String>("hello" , "123"));

        //关闭
        producer.close();
        //如果key为null或者不传入key的值 这条消息不会经过自定义的partitioner进行分区过滤。
    }

}

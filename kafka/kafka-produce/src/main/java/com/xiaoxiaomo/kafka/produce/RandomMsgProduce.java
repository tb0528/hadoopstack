package com.xiaoxiaomo.kafka.produce;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import java.util.Properties;
import java.util.Random;

/**
 * Created by TangXD on 2017/2/15.
 */
public class RandomMsgProduce {

    public static void main(String[] args) throws Exception {

        //1. 读取配置文件
        Properties props = new Properties();
        props.load(ProduceTest.class.getClassLoader().getResourceAsStream("producer.properties"));

        //2. 创建生产者
        Producer<String, String> producer = new Producer<String, String>(new ProducerConfig(props));
        Random random = new Random();

        //3. 组装和发送消息
        int i = 0 ;
        while (true){
            i++ ;
            KeyedMessage<String, String> message = new KeyedMessage<String, String>("hello", i+"", "k " + random.nextLong() + System.currentTimeMillis());
            producer.send(message);
            Thread.sleep(1000);
        }
        //4. 关闭
//        producer.close();
    }
}

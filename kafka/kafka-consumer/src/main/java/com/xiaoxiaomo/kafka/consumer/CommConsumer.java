package com.xiaoxiaomo.kafka.consumer;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 普通消费者
 * Created by xiaoxiaomo on 2015/5/12.
 */
public class CommConsumer {

//    static String topic = "CarrierFetch";
    static String topic = "hello";
    static String groupId = "G_hello";

    public static void main(String[] args) throws IOException {


        //1. 创建消费者连接
        Properties props = new Properties();
        props.load(CommConsumer.class.getClassLoader().getResourceAsStream("consumer.properties"));
        props.put("group.id",groupId);


        ConsumerConnector connector = Consumer.createJavaConsumerConnector(new ConsumerConfig(props));

        //2. 消费者线程数
        HashMap<String, Integer> topicConnect = new HashMap<String, Integer>();

        topicConnect.put(topic, 1); //设置topic 和 消费者数量

        Map<String, List<KafkaStream<byte[], byte[]>>> messageStreams = connector.createMessageStreams(topicConnect);


        //3. Map 中的元素 key：和topicCountMap中的key是一致的。
        // value：List<KafkaStream<byte[], byte[]>> list的元素个数和topicCountMap中key对应的value是一致的。
        List<KafkaStream<byte[], byte[]>> streamList = messageStreams.get(topic);
        for (KafkaStream<byte[], byte[]> stream : streamList) {
            while (stream.iterator().hasNext()) {
                MessageAndMetadata<byte[], byte[]> next = stream.iterator().next();


                //自己测试
                String key = next.key() == null ? "空" : new String(next.key());
                String message = next.message() == null ? "空" : new String(next.message());

                System.out.println(String.format("消费者：键：%s,消息：%s,分区：%s,偏移量offset：%s",
                        key, message, next.partition(), next.offset()));

            }

        }
    }
}

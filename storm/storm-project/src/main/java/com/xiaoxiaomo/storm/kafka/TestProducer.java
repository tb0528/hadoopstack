package com.xiaoxiaomo.storm.kafka;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import kafka.serializer.StringEncoder;

import java.util.Properties;

/**
 * 生产者
 * Created by xiaoxiaomo on 2015/6/20.
 */
public class TestProducer {
	public static void main(String[] args) {
		
		Properties prop = new Properties();
		prop.put("metadata.broker.list", "192.168.33.172:9092");
		prop.put("serializer.class", StringEncoder.class.getName());
		
		ProducerConfig producerConfig = new ProducerConfig(prop );
		Producer<String, String> producer = new Producer<String,String>(producerConfig);
		KeyedMessage<String, String> message = new KeyedMessage<String, String>("spider1", "hello");
		producer.send(message);
		
	}
}

package com.xiaoxiaomo.kafka.utils;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by xiaoxiaomo on 2015/12/28.
 */
public class KafkaStat {

    static  KafkaUtil kafkaUtil=new KafkaUtil();

    static Map<Integer,String> path=new HashMap<Integer, String>();
    static Map<Integer,Integer> userId=new HashMap<Integer, Integer>();

    final String topic="hello";

    Integer times=0;
    Map<String,Integer> pathMap=new HashMap<String, Integer>();

    {

        //init path
        path.put(0,"/xxo/logout");
        path.put(1,"/xxo/getbyid");
        path.put(2,"/xxo/getlist");
        path.put(3,"/xxo/init");
        path.put(4,"/xxo/add/user");

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

    }

    public static void main(String[] args) throws IOException {
        KafkaStat kafkaStat=new KafkaStat();
        kafkaStat.test();
    }

    private void test(){

        //生产者线程
        new Thread(new DataProducer()).start();



        //消费者线程
        new Thread(new DataConsumer()).start();

        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                synchronized (pathMap) {

                    for (String key:pathMap.keySet()){
                        System.out.println(String.format("path:%s,:%s",key,pathMap.get(key)));
                    }
                    System.out.println("总"+times);
                    pathMap.clear();
                    times=0;
                }

            }
        },0, 5*1000);

    }


    class DataProducer implements Runnable{


        public void run() {

            Random random=new Random();
            try {
                kafkaUtil.initProducer();
            } catch (IOException e) {
                e.printStackTrace();
            }

            while (true){
                int pathIndex=random.nextInt(5);
                int userIndex=random.nextInt(10);
                int isVip=random.nextInt(2);
                //userId	path	time	isVip
               // System.out.println(String.format("%s\t%s\t%s\t%s",userId.get(userIndex),path.get(pathIndex),new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()),isVip));
                try {
                    kafkaUtil.produceData(topic,null,String.format("%s\t%s\t%s\t%s",userId.get(userIndex),path.get(pathIndex),new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),isVip));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }

        }
    }
    class DataConsumer implements Runnable{

        public void run() {

            try {
                    List<KafkaStream<byte[],byte[]>> lists= kafkaUtil.getConsumer(1, topic);
                    if (null!=lists&&lists.size()>=1){
                    ConsumerIterator<byte[],byte[]> iterator= lists.get(0).iterator();
                    while (iterator.hasNext()) {
                      String msg=  new String(iterator.next().message());
                        if (msg.contains("\t")){

                            String[] strs=msg.split("\t");
                            if (strs.length>=4){
                                synchronized (pathMap) {
                                    times++;
                                    int count = 1;
                                    if (pathMap.containsKey(strs[1])) {
                                        count = (pathMap.get(strs[1])+1);
                                    }
                                    pathMap.put(strs[1], count);
                                }
                            }

                        }
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }



}

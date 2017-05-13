package com.xiaoxiaomo.storm;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;
import com.xiaoxiaomo.storm.bolt.LogFilterBolt;
import storm.kafka.BrokerHosts;
import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import storm.kafka.ZkHosts;

/**
 * Kafka 与 Strom
 *
 * Sport为 : Kafka Sport
 * Created by xiaoxiaomo on 2015/6/20.
 */
public class LogProcessTopology {

    public static void main(String[] args) {

        //1. 创建一个topology
        TopologyBuilder builder = new TopologyBuilder();

        //2. 设置KafkaSport
//        BrokerHosts hosts = new ZkHosts("192.168.33.73:2181,192.168.33.74:2181,192.168.33.75:2181");
        BrokerHosts hosts = new ZkHosts("192.168.1.171:2181,192.168.1.172:2181,192.168.1.173:2181");
        String topic = "spider1" ;
        String zkRoot = "/kafkaSpout1"; //会在storm使用的zk集群中创建这个根节点
        String id = "abc" ; //类似于kafka中的groupid
        builder.setSpout( "sport_id" , new KafkaSpout(new SpoutConfig(hosts,topic , zkRoot , id))) ;

        //3. 设置bolt
        String boltName = LogFilterBolt.class.getSimpleName();
        builder.setBolt( boltName, new LogFilterBolt() ).shuffleGrouping("sport_id") ;

        //4. 提交到本地或集群
        String topologyName = LogProcessTopology.class.getSimpleName();

        Config map = new Config();
        map.setMaxSpoutPending( 1000 ); //防止雪崩问题

        if( args == null || args.length ==0 ){
            LocalCluster local = new LocalCluster();
            local.submitTopology( topologyName, map, builder.createTopology() );
        }

        //集群
        else{
            try {
                StormSubmitter.submitTopology( topologyName , map, builder.createTopology() );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }
}

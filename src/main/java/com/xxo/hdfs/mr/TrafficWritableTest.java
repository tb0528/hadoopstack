package com.xxo.hdfs.mr;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 *
 * 解析手机流量，并统计信息
 * Created by xiaoxiaomo on 2014/5/10.
 */
public class TrafficWritableTest {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Job job = Job.getInstance(new Configuration(), TrafficWritableTest.class.getSimpleName());
        job.setJarByClass(TrafficWritableTest.class);

        //1. 数据来源
        FileInputFormat.setInputPaths(job, args[0]);

        //2. 调用map

        job.setMapperClass(TrafficMap.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Traffics.class);

        //3. 调用reduce
        job.setReducerClass(TrafficReduce.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Traffics.class);

        //4. 写入数据
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        //5. 执行
        job.waitForCompletion(true) ;

    }

    /**
     * 自定义 Mapper
     */
    static class TrafficMap extends Mapper<LongWritable,Text,Text,Traffics>{

        Text K2 = new Text() ;
        Traffics V2 = new Traffics() ;
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            //处理单行数据信息
            String line = value.toString();
            String[] stirs = line.split("\t");
            K2.set(stirs[1]);
            V2.set(stirs[6],stirs[7],stirs[8],stirs[9]) ;
            context.write(K2 , V2);
        }
    }

    /**
     * 自定义 Reduce
     */
    static class TrafficReduce extends Reducer<Text , Traffics , Text ,Traffics >{

        Traffics V3 = new Traffics() ;
        @Override
        protected void reduce(Text key, Iterable<Traffics> values, Context context) throws IOException, InterruptedException {
            //统计流量
            long upPackNum = 0;
            long downPackNum = 0 ;
            long upPayLoad = 0;
            long downPayLoad = 0;
            for (Traffics value : values) {
                upPackNum += value.getUpPackNum() ;
                downPackNum += value.getDownPackNum() ;
                upPayLoad += value.getUpPayLoad() ;
                downPayLoad += value.getDownPayLoad() ;
            }

            V3.set( upPackNum, downPackNum , upPayLoad, downPayLoad);
            context.write( key , V3 );
        }
    }

    /**
     * 自定义序列化类
     */
    static class Traffics implements Writable{
        private Long upPackNum ;
        private Long downPackNum ;
        private Long upPayLoad ;
        private Long downPayLoad ;

        public void write(DataOutput out) throws IOException {
            out.writeLong(this.upPackNum);
            out.writeLong(this.downPackNum);
            out.writeLong(this.upPayLoad);
            out.writeLong(this.downPayLoad);
        }

        public void readFields(DataInput in) throws IOException {
            this.upPackNum = in.readLong();
            this.downPackNum = in.readLong();
            this.upPayLoad = in.readLong();
            this.downPayLoad = in.readLong();
        }

        public void set(String upPackNum ,String downPackNum ,String upPayLoad ,String downPayLoad) {
            this.upPackNum = Long.parseLong( upPackNum ) ;
            this.downPackNum = Long.parseLong( downPackNum ) ;
            this.upPayLoad = Long.parseLong( upPayLoad ) ;
            this.downPayLoad = Long.parseLong( downPayLoad ) ;

        }

        public void set(Long upPackNum ,Long downPackNum ,Long upPayLoad ,Long downPayLoad) {
            this.upPackNum = upPackNum;
            this.downPackNum = downPackNum ;
            this.upPayLoad = upPayLoad ;
            this.downPayLoad = downPayLoad ;
        }

        public Long getUpPackNum() {
            return upPackNum;
        }

        public Long getDownPackNum() {
            return downPackNum;
        }

        public Long getUpPayLoad() {
            return upPayLoad;
        }

        public Long getDownPayLoad() {
            return downPayLoad;
        }

        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer();
            sb.append(upPackNum).append('\t');
            sb.append(downPackNum).append('\t');
            sb.append(upPayLoad).append('\t');
            sb.append(downPayLoad);
            return sb.toString();
        }
    }
}

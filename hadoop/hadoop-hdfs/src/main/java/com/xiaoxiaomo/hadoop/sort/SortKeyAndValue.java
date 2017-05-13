package com.xiaoxiaomo.hadoop.sort;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * 倒叙排序key和value
 * Created by xiaoxiaomo on 2014/5/10.
 */
public class SortKeyAndValue {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Job job = Job.getInstance(new Configuration(), SortKeyAndValue.class.getSimpleName());
        job.setJarByClass(SortKeyAndValue.class);

        //1. 数据来源
        FileInputFormat.setInputPaths(job, args[0]);

        //2. 调用map

        job.setMapperClass(SortMap.class);
        job.setMapOutputKeyClass(MyWritable.class);
        job.setMapOutputValueClass(NullWritable.class);

        //3. 调用reduce
        job.setReducerClass(SortReduce.class);
        job.setOutputKeyClass(MyWritable.class);
        job.setOutputValueClass(NullWritable.class);

        //4. 写入数据
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        //5. 执行
        job.waitForCompletion(true) ;

    }

    /**
     * 自定义 Mapper
     */
    static class SortMap extends Mapper<LongWritable,Text,MyWritable,NullWritable>{

        MyWritable myWritable = new MyWritable() ;
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            //处理单行数据信息
            String line = value.toString();
            String[] stirs = line.split("\t");
            myWritable.set( stirs[0] , stirs[1] );
            context.write(myWritable , NullWritable.get());

        }
    }

    /**
     * 自定义 Reduce
     */
    static class SortReduce extends Reducer<MyWritable , NullWritable , MyWritable ,NullWritable >{

        @Override
        protected void reduce(MyWritable key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException {
            context.write( key , NullWritable.get() );
        }
    }

    /**
     * 自定义一个Writable 并重写compareTo方法
     */
    static class MyWritable implements WritableComparable<MyWritable>{

        int k1 ;
        int k2 ;

        public void set( String k1 , String k2 ){
            this.k1 = Integer.valueOf(k1) ;
            this.k2 = Integer.valueOf(k2) ;
        }

        public int compareTo(MyWritable o) {
            if( k1 < o.k1 ) return 1 ;
            if( k1 > o.k1 ) return -1 ;
            if( k2 < o.k2 ) return 1 ;
            if( k2 > o.k2 ) return -1 ;
            return 0 ;
        }

        public void write(DataOutput out) throws IOException {
            out.writeInt(k1);
            out.writeInt(k2);
        }

        public void readFields(DataInput in) throws IOException {
            k1 = in.readInt();
            k2 = in.readInt();
        }

        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer();
            sb.append(k1).append("\t");
            sb.append(k2).append("\t");
            return sb.toString();
        }
    }

}

package com.xxo.hdfs.hdfs;

import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;

import java.io.File;
import java.io.IOException;

/**
 *
 * 使用SequenceFile 存储小文件
 * Created by xiaoxiaomo on 2014/5/9.
 */
public class SequenceFileTest {

    public static void main(String[] args) throws IOException {

        Configuration conf = new Configuration();
        FileSystem fileSystem = FileSystem.newInstance(conf);

        SequenceFile.Writer writer = new SequenceFile.Writer( fileSystem ,conf ,new Path("/members2.seq"), Text.class , BytesWritable.class) ;


        //遍历文件，并写到到SequenceFile中
        File file = new File("H:\\chaorenxueyuan\\offline3video\\2016-05-09【HDFS和MapReduce】\\members2000");
        File[] files = file.listFiles();
        for (File f : files) {

            byte[] bytes = FileUtils.readFileToByteArray(f);
            writer.append(new Text( f.getName() ), new BytesWritable(bytes));

            System.out.println(f.getName());

        }

        writer.close();
    }
}

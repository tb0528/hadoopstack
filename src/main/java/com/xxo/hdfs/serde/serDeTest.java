package com.xxo.hdfs.serde;

import org.apache.hadoop.io.Writable;

import java.io.*;

/**
 *
 *
 * Created by xiaoxiaomo on 2016/5/9.
 */
public class serDeTest {


    public static void main(String[] args) throws IOException {
//        serializToDisk();


        Teacher teache = new Teacher(2 , 33 ,"momo" );
        FileOutputStream outputStream = new FileOutputStream("F://MOMO.txt");

        DataOutputStream stream = new DataOutputStream(outputStream);
        teache.write(stream);

        stream.close();
        outputStream.close();
    }





    /**
     * 普通java序列化到磁盘
     * @throws IOException
     */
    private static void serializToDisk() throws IOException {
        //序列化到磁盘
        Student stu = new Student(1 ,24 , "小小默") ;

        FileOutputStream outputStream = new FileOutputStream("F://小小默.txt");

        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

        objectOutputStream.writeObject(stu);

        objectOutputStream.close();
        outputStream.close();
    }


    static class Person implements Serializable{
        private Integer id;
        public void setId(Integer id) {
            this.id = id;
        }
    }

    static class Student extends Person implements Serializable{
        private Integer age;
        private String name;
        public Student(Integer id, Integer age, String name) {
            this.age = age;
            this.name = name;
        }
    }

    static class Teacher extends Person implements Writable{
        private Integer age;
        private String name;
        public Teacher(Integer id, Integer age, String name) {
            super.id = id;
            this.age = age;
            this.name = name;
        }

        public void write(DataOutput out) throws IOException {
            out.writeInt(age);
            out.writeInt(super.id);
            out.writeUTF(name);
        }

        public void readFields(DataInput in) throws IOException {
            age = in.readInt() ;
            name = in.readUTF() ;
            super.id = in.readInt() ;
        }
    }

}

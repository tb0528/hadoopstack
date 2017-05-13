package com.xiaoxiaomo.kafka.utils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;
public class HDFSUtils {

	Logger logger=Logger.getLogger(HDFSUtils.class);
   
	public static void main(String[] args) throws Exception {
		HDFSUtils hdfsUtils=new HDFSUtils();
		Configuration conf=new Configuration();
		String localPath="D:\\课堂\\互联网数据的接入与清洗\\ip.data";
		String ipPath="hdfs://xxo156.xxo:9000/util/ip.data";
		hdfsUtils.uplaod(conf,localPath,ipPath,false);
		hdfsUtils.readLine(conf, ipPath,false);
		hdfsUtils.readFully(conf, ipPath,false);
		
		
		hdfsUtils.createAndAppendeFile(conf, "hdfs://xxo156.xxo:9000/abc.txt", "\n"+new Date().toString(),false);
		hdfsUtils.readFully(conf, "hdfs://xxo156.xxo:9000/abc.txt",false);
		
//		hdfsUtils.deleteFile(conf, "hdfs://xxo156.xxo:9000/util", false);
	}
	


	/**
	 * 上传文件到hdfs上，如果hdfs上存在则会覆盖。
	 * @param conf
	 * @param from
	 * @param to
	 * @throws Exception
	 */
    public  void   uplaod(Configuration conf,String from,String to,boolean isCluster) throws Exception {
       // System.setProperty("HADOOP_USER_NAME","root");
        //本地文件
        Path src =new Path(from);
        //HDFS为止
        Path dst =new Path(to);
        FileSystem fs=getFileSystem(conf,dst,isCluster);
        fs.copyFromLocalFile(src, dst);
        fs.close();//释放资源
        logger.info("uplaod ok");
    }
    
    /**
     * 一行一行的读取hdfs的文件
     * @param conf
     * @param p
     * @throws Exception
     */
    public void readLine(Configuration conf,String p,boolean isCluster) throws Exception{
    	//System.setProperty("HADOOP_USER_NAME","root");
        Path path = new Path(p);
        FileSystem fs=getFileSystem(conf,path,isCluster);
        // check if the file exists
        if ( fs.exists(path) )
        {
            FSDataInputStream is = fs.open(path);
            // get the file info to create the buffer
            String readLen;
            BufferedReader reader= new BufferedReader(new InputStreamReader(is));
            while((readLen=reader.readLine())!=null)
            {
            	logger.info(readLen+"***********8");
            }
            reader.close();
            is.close();
            fs.close(); 
        }
        else
        {
            throw new Exception("the file is not found .");
        }
    	
    }
    
    public byte[] readFully(Configuration conf,String p,boolean isCluster) throws Exception
    {
        //System.setProperty("HADOOP_USER_NAME","root");
       
        Path path = new Path(p);
        FileSystem fs=getFileSystem(conf,path,isCluster);
        // check if the file exists
        if ( fs.exists(path) )
        {
            FSDataInputStream is = fs.open(path);
            // get the file info to create the buffer
            FileStatus stat = fs.getFileStatus(path);
            // create the buffer
            byte[] buffer = new byte[Integer.parseInt(String.valueOf(stat.getLen()))];
            is.readFully(0, buffer);
            is.close();
            fs.close();
            logger.info(new String(buffer));
            return buffer;
            
        }
        else
        {
            throw new Exception("the file is not found .");
        }
    }
    
    /**
     * 创建或者追加文件 有就追加，没有则新建一个并追加
     * @param conf
     * @param p
     * @param content
     * @throws Exception
     */
    public  void createAndAppendeFile(Configuration conf,String p,String content,boolean isCluster) throws Exception {
    	Path path =new Path(p);
    	FileSystem fs=getFileSystem(conf,path,isCluster);
    	// check if the file exists
        if(!fs.exists(path)){
        	fs.createNewFile(path);
        }
        FSDataOutputStream output=fs.append(path);
        //追加
    	output.write(content.getBytes());
    	output.close();
        fs.close();//释放资源
        logger.info("创建文件或者追加文件成功.....");
    	
    	
    }
    
    /**
     * 删除指定的目录
     * @param conf
     * @param p
     * @param isCluster
     * @return
     * @throws Exception
     */
    public boolean deleteFile(Configuration conf,String p,boolean isCluster) throws Exception{
    	Path path = new Path(p);
        FileSystem fs=getFileSystem(conf,path,isCluster);
        // check if the file exists
        if ( fs.exists(path) )
        {
        	if(fs.isDirectory(path)){
        		return fs.delete(path, true);
        	}else{
        		return fs.delete(path, false);
        	}
        }
        else
        {
            throw new Exception("the path is not found .");
        }
    	
    }
    
    /**
     * 获取filesystem
     * @param conf
     * @param path
     * @param isCluster
     * @return
     * @throws Exception
     */
    private FileSystem getFileSystem(Configuration conf,Path path,boolean isCluster)throws Exception  {
    	
    	 FileSystem fs;
         if(isCluster){
         	fs=FileSystem.get(conf);
         }else{
         	fs=path.getFileSystem(conf);
         }
         return fs;
    }

}
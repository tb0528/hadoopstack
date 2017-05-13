package com.xiaoxiaomo.hive.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * HIVE JDBC
 * 运行此代码需要
 * 1. 安装hive:http://blog.xiaoxiaomo.com
 * 2. 运行 HDFS：start-dfs.sh
 * 3. 运行 MySQL: 如果meta存入mysql，需要启动mysql:service mysql start
 * 4. 运行：$HIVE_HOME/bin/hive
 *
 * 准备：
 * 创建表
 * create external table test (
 line string
 ) row format delimited
 fields terminated by '\t';
 *
 * 加载数据
 * /test/resources/hive-store-1.txt
 *
 * 启动服务：./hive --service hiveserver2 --hiveconf hive.server2.thrift.port=10003
 *
 * //通过监听知道其实任然是去执行了hive MR
 * Created by xiaoxiaomo on 2015/5/6.
 */
public class MyJdbc {

	public static void main(String[] args) throws Exception {

		//1. 加载驱动
//		Class.forName("org.apache.hadoop.hive.jdbc.Driver");//hiverserver的方式
		Class.forName("org.apache.hive.jdbc.HiveDriver");

		//2. 获取连接
//		DriverManager.getConnection("jdbc:hive://xxo07:1000", "", "");
		Connection connection = DriverManager.getConnection("jdbc:hive2://xxo07:10003/default", "root", "");



		//3. 组装执行SQL
		String sql = "select word, count(word) as count from ( "
				   + " select explode(split(line, ' ')) as word from test ) w group by word" 
				   + " order by count desc";
		PreparedStatement ps = connection.prepareStatement(sql);
		
		ResultSet rs = ps.executeQuery();

		//4. 获取结果
		while(rs.next()) {
			String word = rs.getString("word");
			int count = rs.getInt("count");
			System.out.println("word=" + word + ", count=" + count);
		}

		//5. 关闭连接
		rs.close();
		ps.close();
		connection.close();
	}
}

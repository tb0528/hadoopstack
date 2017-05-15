package com.xiaoxiaomo.storm.core.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayListHandler;

import com.xiaoxiaomo.storm.core.entity.CityView;

public final class MyDbUtils2 {//拒绝继承
	private static String className = "com.mysql.jdbc.Driver";
	private static String url = "jdbc:mysql://192.168.1.155:3306/xxo?useUnicode=true&amp;characterEncoding=utf-8";
	private static String user = "root";
	private static String password = "haha";
	private static QueryRunner queryRunner = new QueryRunner();
	
	public static void main(String[] args) {
		List<CityView> list = MyDbUtils2.executeSqlObject("select c.city,sum(c.count) as allnum,p.lng,p.lat from city_view c LEFT JOIN `position` p on c.city=p.city GROUP BY c.city");
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for(int i=0;i<list.size();i++){
			CityView cityView = list.get(i);
			if(i>0){
				sb.append(",");
			}
			sb.append("{");
			sb.append("\"name\":\"").append(cityView.getCity()).append("\",");
			sb.append("\"value\":").append(cityView.getCount());
			sb.append("}");
		}
		sb.append("]");
		
		System.out.println(sb.toString());
	}
	
	public static List<String> executeSql(String sql){
		List<String> result = new ArrayList<String>();
		try {
			List<Object[]> requstList = queryRunner.query(getConnection(),sql, new ArrayListHandler(new BasicRowProcessor(){
				@Override
				public <Object> List<Object> toBeanList(ResultSet rs, Class<Object> type)
						throws SQLException {
					return super.toBeanList(rs, type);
				}
			}));
			for (Object[] objects : requstList) {
				result.add(objects[0].toString());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static List<CityView> executeSqlObject(String sql){
		List<CityView> result = new ArrayList<CityView>();
		try {
			List<Object[]> requstList = queryRunner.query(getConnection(),sql, new ArrayListHandler(new BasicRowProcessor(){
				@Override
				public <CityView> List<CityView> toBeanList(ResultSet rs, Class<CityView> type)
						throws SQLException {
					return super.toBeanList(rs, type);
				}
			}));
			for (int i = 0; i < requstList.size(); i++) {
				Object[] objects = requstList.get(i);
				CityView cityView = new CityView();
				cityView.setId(i);
				cityView.setCity(objects[0].toString());
				cityView.setCount(Integer.parseInt(objects[1].toString()));
				result.add(cityView);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	private static void update(String sql, Object... params) {
		try {
			Connection connection = getConnection();
			queryRunner.update(connection , sql, params);
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	//拒绝new一个实例
	private MyDbUtils2() {};
	static {//调用该类时既注册驱动
		try {
			Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new ExceptionInInitializerError(e);
		}
	}
	
	//获取连接
	private static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url, user, password);
	}
	
	
}
package com.xiaoxiaomo.hive.udf;

import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;

import java.sql.Date;
import java.util.Calendar;

public class MyZodiaAndC extends UDF{
	
	/**
	 * 
	 * @param date，注意，如果使用的参数是Date，必须是java.sql.Date，
	 * 		  这里无法识别
	 * @param type 1==>生肖，0==》星座
	 * @return
	 */
	 public Text evaluate(Date date, IntWritable type) {
		 if(type.get() == 1) { //生肖
			 return new Text(getZodica(date));
		 } else if(type.get() == 0) { //星座
			 return new Text(getConstellation(date));			 
		 } else {
			 return null;
		 }
	 }
	 
	 public  final String[] zodiacArr = { "猴", "鸡", "狗", "猪", "鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊" };
	 
	 public  final String[] constellationArr = { "水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "魔羯座" };
	  
	 public  final int[] constellationEdgeDay = { 20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22, 22 };
	  
	 /**
	  * 根据日期获取生肖
	  * @return
	  */
	 public String getZodica(java.util.Date date) {
	     Calendar cal = Calendar.getInstance();
	     cal.setTime(date);
	     return zodiacArr[cal.get(Calendar.YEAR) % 12];
	 }
	  
	 /**
	  * 根据日期获取星座
	  * @return
	  */
	 public String getConstellation(java.util.Date date) {
	     if (date == null) {
	         return "";
	     }
	     Calendar cal = Calendar.getInstance();
	     cal.setTime(date);
	     int month = cal.get(Calendar.MONTH);
	     int day = cal.get(Calendar.DAY_OF_MONTH);
	     if (day < constellationEdgeDay[month]) {
	         month = month - 1;
	     }
	     if (month >= 0) {
	         return constellationArr[month];
	     }
	     // default to return 魔羯
	     return constellationArr[11];
	 }
	
//	 public void test() {
//		 
//		 System.out.println(getConstellation(new java.util.Date()));
//		 System.out.println(getZodica(new java.util.Date()));
//	 }
	 
}

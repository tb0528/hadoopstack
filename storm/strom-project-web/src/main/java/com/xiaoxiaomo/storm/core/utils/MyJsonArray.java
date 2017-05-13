package com.xiaoxiaomo.storm.core.utils;

import org.json.JSONArray;

/**
 * 转化为dateGrid中的column列
 * @author xiaoxiaomo
 *
 */
public class MyJsonArray extends JSONArray {

	@Override
	public String toString() {
		String str = super.toString();
		str = str.replaceAll("\"", "");
		
		return "["+str+"]";
	}
	
}

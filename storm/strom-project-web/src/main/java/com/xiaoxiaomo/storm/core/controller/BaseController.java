package com.xiaoxiaomo.storm.core.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseController {
	public static final String CMS = "";
	public static final String HOME = "/home";
	public static final String COURSE = "/cms/course";
	public static final String TOPIC = "/topic";
	public static final String FW = "/fw";
	
	public static final String SUCCESS = "success";
	public static final String ERROR_404 = "error_404";
	public static final String ERROR_500 = "error_500";
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	protected void writeToPage(HttpServletResponse response, String result) {
		try {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;");
			response.getWriter().write(result);
		} catch (IOException e) {
			e.printStackTrace();
			PrintWriter toClient = null;
			try {
				toClient = response.getWriter();
			} catch (IOException e1) {
				e1.printStackTrace();
			} // 得到向客户端输出文本的对象
			toClient.write("加载失败!");
			toClient.close();
		}
	}

}

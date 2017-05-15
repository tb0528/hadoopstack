package com.xiaoxiaomo.storm.core.utils;

import java.io.File;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.xiaoxiaomo.storm.core.utils.web.SpringContextHolder;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

public class FreeMarkerUtil {
	
	public static String parseStringTemplate(String sqlTemplate,
											 Map<String, String> root) {
		
		Writer out = new StringWriter(2048);
		   try {
			Configuration cfg = new Configuration();
			    StringTemplateLoader stringLoader = new StringTemplateLoader();
			    stringLoader.putTemplate("myTemplate", sqlTemplate);
			    cfg.setTemplateLoader(stringLoader);
			    Template temp = cfg.getTemplate("myTemplate","utf-8");
			    temp.process(root, out);
			    out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out.toString();
	}
	/**
	 * 解析echart模版
	 * @param templatePath 文件所在目录
	 * @param fileName 文件名称
	 * @param root 
	 * @return
	 */
	public static String parseFileTemplate(String templatePath, String fileName,
										   Map<String, String> root) {
		
		Writer out = new StringWriter(2048);
		   try {
			   Configuration cfg = new Configuration();
			    FileTemplateLoader stringLoader = new FileTemplateLoader(new File(templatePath));
			    cfg.setTemplateLoader(stringLoader);
			    Template temp = cfg.getTemplate(fileName,"utf-8");
			    temp.process(root, out);
			    out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out.toString();
	}
	
	public static String parseTemplate(String templateName,
									   Map<?, ?> root) {
		final FreeMarkerConfigurer freeMarkerConfigurer = SpringContextHolder.getBean(FreeMarkerConfigurer.class);
		Writer out = new StringWriter(2048);
		   try {
			   Template temp = freeMarkerConfigurer.getConfiguration().getTemplate(templateName);
			    temp.process(root, out);
			    out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out.toString();
	}
}


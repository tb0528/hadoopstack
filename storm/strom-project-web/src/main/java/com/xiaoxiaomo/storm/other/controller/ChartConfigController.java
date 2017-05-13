package com.xiaoxiaomo.storm.other.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xiaoxiaomo.storm.core.entity.ChartConfig;
import com.xiaoxiaomo.storm.core.service.ChartConfigService;

@Controller
@RequestMapping("/chartConfig")
public class ChartConfigController{
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	private static final String PAGE_INDEX = "/chart/configchart";
	private static final String PAGE_CONFIG_EDIT = "/chart/configchart_edit";
	
	@Resource
	private ChartConfigService chartConfigService;
	
	@RequestMapping("")
	public String index(ModelMap modelMap) {
		return PAGE_INDEX;
	}
	
	/**
	 * 保存折线柱状图配置
	 * @param chartConfig
	 * @param modelMap
	 * @param request
	 * @return
	 */
	@RequestMapping("/saveLineBarChartConfig")
	public String saveLineBarChartConfig(ChartConfig chartConfig, ModelMap modelMap, HttpServletRequest request) {
		final String[] ytextArray = request.getParameterValues("ytext");
		final String[] ysqlArray = request.getParameterValues("ysql");
		
		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < ysqlArray.length; i++) {
			final JSONObject jsonObject = new JSONObject();
			jsonObject.put("title", ytextArray[i]);
			jsonObject.put("sql", ysqlArray[i]);
			jsonArray.put(i, jsonObject);
		}
		chartConfig.setYsql(jsonArray.toString());
		logger.debug("保存的折线柱状图配置对象{}", chartConfig);
		
		chartConfigService.save(chartConfig);
		return ChartController.PAGE_INDEX;
	}
	
	/**
	 * 查询柱状图配置信息做回显
	 * @param id
	 * @return
	 */
	@RequestMapping("/getLineBarChartConfig/{id}")
	public String getLineBarChartConfig(@PathVariable("id") Integer id, ModelMap modelMap){
		ChartConfig chartConfig = chartConfigService.getById(id);
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String ysql = chartConfig.getYsql();
		JSONArray jsonArray = new JSONArray(ysql);
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject obj = (JSONObject)jsonArray.get(i);
			Map<String,String> map = new HashMap<String, String>();
			map.put("title", obj.get("title").toString());
			map.put("sql", obj.get("sql").toString());
			list.add(map);
		}
		modelMap.put("chartConfig", chartConfig);
		modelMap.put("list", list);
		return PAGE_CONFIG_EDIT;
	}
	
	public static void main(String[] args) {
		JSONArray jsonArray = new JSONArray();   
		
		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("title", "text0");
		jsonObject.put("sql", "sql0");
        jsonArray.put(0, jsonObject);    
        
		final JSONObject jsonObject1 = new JSONObject();
		jsonObject1.put("title", "text1");
		jsonObject1.put("sql", "sql1");
        jsonArray.put(1,jsonObject1); 
        
        
        System.out.println("返回一个JSONArray对象："+jsonArray.toString());
	}
}

package com.xiaoxiaomo.storm.other.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiaoxiaomo.storm.core.domain.report.ChartView;
import com.xiaoxiaomo.storm.core.domain.report.GaugeChartView;
import com.xiaoxiaomo.storm.core.domain.report.LineChartView;
import com.xiaoxiaomo.storm.core.domain.report.PieChartView;
import com.xiaoxiaomo.storm.core.entity.ChartConfig;
import com.xiaoxiaomo.storm.core.service.ChartConfigService;
import com.xiaoxiaomo.storm.core.service.ChartService;
import com.xiaoxiaomo.storm.core.utils.FreeMarkerUtil;

@Controller
@RequestMapping("/chart")
public class ChartController {

	public static final String PAGE_INDEX = "/chart/chart";
	public static final String PAGE_CONFIG = "/chart/configchart";

	@Resource
	private ChartService chartService;
	@Resource
	private ChartConfigService chartConfigService;

	@RequestMapping("")
	public String index(ModelMap modelMap) {
		return PAGE_INDEX;
	}

	/**
	 * 进入页面的时候架加载左侧树
	 * 
	 * @param response
	 */
	@RequestMapping("/loadTree")
	public void loadTree(HttpServletResponse response) {
		List<ChartConfig> chartConfigList = chartConfigService
				.queryAllChartConfig();
		String json = chartService.installJson(chartConfigList);
		writeToPage(response, json);
	}

	/**
	 * 生成折线图
	 * 
	 * @param response
	 */
	@RequestMapping("/lineChart/{id}")
	public void lineChart(@PathVariable("id") Integer id,
			HttpServletResponse response) {
		LineChartView chartView = chartService.getLineChartViewById(id);
		buildChart(response, chartView, "Linechart.ftl");
	}

	/**
	 * 生成柱状图
	 * 
	 * @param response
	 */
	@RequestMapping("/barChart/{id}")
	public void barChart(@PathVariable("id") Integer id,
			HttpServletResponse response) {
		LineChartView chartView = chartService.getLineChartViewById(id);
		buildChart(response, chartView, "barchart.ftl");
	}

	/**
	 * 生成饼图
	 * 
	 * @param response
	 */
	@RequestMapping("/pieChart")
	public void pieChart(HttpServletResponse response) {
		PieChartView chartView = new PieChartView();
		chartView.addData("直接访问", "335");
		chartView.addData("邮件营销", "310");
		chartView.addData("联盟广告", "110");
		chartView.setTitle("某站点用户访问来源");

		buildChart(response, chartView, "piechart.ftl");
	}

	/**
	 * 生成仪表图
	 * 
	 * @param response
	 */
	@RequestMapping("/gaugeChart")
	public void getEchartsOptionForGaugeChart(HttpServletResponse response) {
		GaugeChartView chartView = new GaugeChartView();
		chartView.setDatastr("业务指标", "50");
		chartView.setTitle("某站点用户访问来源");
		chartView.setSeriesname("访问来源");

		buildChart(response, chartView, "gaugechart.ftl");
	}

	/**
	 * 生成图表
	 * 
	 * @param response
	 * @param chartView
	 * @param templateName
	 */
	private void buildChart(HttpServletResponse response, ChartView chartView,
			String templateName) {
		Map<String, ChartView> root = new HashMap<String, ChartView>();
		root.put("chartView", chartView);
		String result = FreeMarkerUtil.parseTemplate(templateName, root);
		writeToPage(response, result);
	}

	

	@RequestMapping("/loadDialog")
	public String loadDialog() {
		return PAGE_CONFIG;
	}
	/**
	 * 删除图表配置
	 * @param ids
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/deleteChartConfig")
	public String deleteChartConfig(
			@RequestParam(value = "ids", required = true) String ids) {
		boolean deleteFlag = chartService.deleteChartConfig(ids);
		return deleteFlag?"success":"error";
	}
	
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

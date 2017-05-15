package com.xiaoxiaomo.storm.other.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiaoxiaomo.storm.core.domain.web.PageVo;
import com.xiaoxiaomo.storm.core.entity.DataGrid;
import com.xiaoxiaomo.storm.core.entity.DataGridMore;
import com.xiaoxiaomo.storm.core.service.DataGridService;

@Controller
@RequestMapping("/dataGrid")
public class DataGridController{
	private static final String PAGE_INDEX = "/chart/dataGrid";
	private static final String PAGE_INDEX_MORE = "/chart/dataGridMore";
	private static final String CONFIG_GRID_PAGE = "/chart/configGrid";
	private static final String CONFIG_GRID_MORE_PAGE = "/chart/configGridMore";
	private static final String CONFIG_GRID_PAGE_EDIT = "/chart/configgrid_edit";

	private static final String ADD_DATE_PAGE = "/chart/adddata";

	@Resource
	private DataGridService dataGridService;

	@RequestMapping("/index/{id}")
	private String index(@PathVariable("id") Integer id, ModelMap modelMap) {
		DataGrid dataGrid = dataGridService.findDataGridById(id);
		String colums = dataGrid.getColums();
		JSONArray jsonArray = new JSONArray(colums);
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		dataGridService.getColumnForSearch(jsonArray, list, dataGrid);
		// 表头
		modelMap.put("column", "[" + colums + "]");
		// 为了组装查询条件
		modelMap.put("columnames", list);
		// 隐藏域保存当前datagrid配置
		modelMap.put("id", id);
		return PAGE_INDEX;
	}

	/**
	 * 加载datagrid表单数据
	 */
	@ResponseBody
	@RequestMapping("/loadData")
	private String loadData(
			@RequestParam(value = "parameters", required = false) String parameters,
			@RequestParam(value = "id", required = false) Integer id,
			PageVo pageVo) {
		Map<String, String> map = dataGridService.getParameters(parameters);
		DataGrid dataGrid = dataGridService.findDataGridById(id);
		String json = dataGridService.loadData(dataGrid, map, pageVo);
		return json;
	}

	/**
	 * 跳转
	 * 
	 * @return
	 */
	@RequestMapping("/toConfigGridPage")
	private String toConfigGridPage() {
		return CONFIG_GRID_PAGE;
	}

	/**
	 * 保存datagrid配置
	 * 
	 * @param dataGridConfig
	 * @param request
	 * @return
	 */
	@RequestMapping("/saveGridConfig")
	private String saveGridConfig(DataGrid dataGridConfig,
								  HttpServletRequest request) {
		String[] fields = request.getParameterValues("field");
		String[] titles = request.getParameterValues("title");
		String[] parameterValues = request.getParameterValues("searchkey");
		String[] styles = request.getParameterValues("style");
		int prikeyindex = Integer.parseInt(request.getParameter("prikey"));
		dataGridService.install(dataGridConfig, fields, titles, styles,prikeyindex);
		String parameters = StringUtils.join(parameterValues, ",");
		dataGridConfig.setCheckvalues(parameters);
		dataGridService.save(dataGridConfig);
		return CONFIG_GRID_PAGE;
	}
	
	/**
	 * 删除表单配置
	 * @param ids
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/deleteGridConfig")
	public String deleteGridConfig(
			@RequestParam(value = "ids", required = true) String ids,
			@RequestParam(value = "tablename", required = true) String tablename) {
		boolean deleteFlag = dataGridService.deleteGridConfig(ids,tablename);
		return deleteFlag?"success":"error";
	}
	

	/**
	 * 进入页面的时候架加载左侧树
	 * 
	 * @param response
	 */
	@ResponseBody
	@RequestMapping("/loadTree")
	public String loadTree() {
		List<DataGrid> dataGridList = dataGridService.queryAllDataGrid();
		String json = dataGridService.installJson(dataGridList);
		return json;
	}

	/**
	 * 查询dataGrid配置信息做回显
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/getDataGridConfig/{id}")
	public String getDataGridConfig(@PathVariable("id") Integer id,
									ModelMap modelMap) {
		DataGrid dataGrid = dataGridService.findDataGridById(id);
		String colums = dataGrid.getColums();
		JSONArray jsonArray = new JSONArray(colums);
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String[] searchColumn = new String[jsonArray.length()];
		dataGridService.getColumn(jsonArray, list, searchColumn, dataGrid);
		modelMap.put("dataGrid", dataGrid);
		modelMap.put("list", list);
		return CONFIG_GRID_PAGE_EDIT;
	}

	/**
	 * 
	 * 
	 * @param prikey
	 *            主键字段
	 * @param value
	 *            主键的值
	 * @param gridConfigId
	 *            表单配置信息的主键
	 * @return
	 */
	@RequestMapping("/toAddData/{gridConfigId}")
	public String toAddData(@PathVariable("gridConfigId") Integer gridConfigId,
							ModelMap modelMap) {
		DataGrid dataGrid = dataGridService.findDataGridById(gridConfigId);
		List<Map<String, String>> list = dataGridService
				.getColumForEditData(dataGrid);
		modelMap.put("columnList", list);
		modelMap.put("configid", gridConfigId);
		return ADD_DATE_PAGE;
	}

	/**
	 * 保存前台表单编辑之后的数据
	 * 
	 * @param id
	 *            dataGrid 配置ID
	 * @param jsonResult
	 *            前台数据的json{update:[{id:"1",name:"lisi"},{id:"1",name:"lisi"}]}
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/saveData")
	public String saveData(
			@RequestParam(value = "configId", required = true) Integer configId,
			@RequestParam(value = "jsonResult", required = true) String jsonResult) {
		boolean execResult = dataGridService.saveData(configId, jsonResult);
		return execResult ? "success" : "error";
	}

	

	/**
	 * 保存新增数据
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/saveDialogData")
	public String saveDialogData(HttpServletRequest request) {
		boolean result = dataGridService.saveDialogData(request);
		return result ? "success" : "error";
	}
	/**
	 * 删除数据
	 * @param configId
	 * @param rowsResult
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/deleteData")
	public String deleteData(
			@RequestParam(value = "configId", required = true) Integer configId,
			@RequestParam(value = "rowsResult", required = true) String rowsResult) {
		boolean deleteFlag = dataGridService.deleteData(configId,rowsResult);
		return deleteFlag?"success":"error";
	}
	
	
	
	/**
	 * 进入页面的时候架加载左侧树(多表查询)
	 * 
	 * @param response
	 */
	@ResponseBody
	@RequestMapping("/loadTreeMore")
	public String loadTreeMore() {
		List<Map<String, Object>> dataGridList = dataGridService.queryAllDataGridMore();
		String json = dataGridService.installJsonForMore(dataGridList);
		return json;
	}
	/**
	 * 跳转到datagrid界面(多表查询)
	 * @param id
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/indexMore/{id}")
	private String indexMore(@PathVariable("id") Integer id, ModelMap modelMap) {
		DataGridMore dataGridMore = dataGridService.findDataGridMoreById(id);
		String[] colums = dataGridService.getColumnsForMoreTable(dataGridMore);
		String jsonColumn = dataGridService.getColumnJsonForMore(colums);
		List<Map<String, Object>> columnListForMore = dataGridService.getColumnListForMore(dataGridMore);
		// 表头
		modelMap.put("column",  jsonColumn);
		// 为了组装查询条件
		modelMap.put("columnames", columnListForMore);
		// 隐藏域保存当前datagrid配置
		modelMap.put("id", id);
		return PAGE_INDEX_MORE;
	}

	/**
	 * 加载多表查询的datagrid数据(多表查询)
	 */
	@ResponseBody
	@RequestMapping("/loadDataMore")
	private String loadDataMore(
			@RequestParam(value = "parameters", required = false) String parameters,
			@RequestParam(value = "id", required = false) Integer id,
			PageVo pageVo) {
		Map<String, String> map = dataGridService.getParameters(parameters);
		DataGridMore dataGridMore = dataGridService.findDataGridMoreById(id);
		String json = dataGridService.loadDataMore(dataGridMore, map, pageVo);
		return json;
	}
	
	
	/**
	 * 跳转
	 * 
	 * @return
	 */
	@RequestMapping("/toConfigGridMorePage")
	private String toConfigGridMorePage() {
		return CONFIG_GRID_MORE_PAGE;
	}
	
	
	/**
	 * 保存datagrid配置(多表查询)
	 * 
	 * @param dataGridConfig
	 * @param request
	 * @return
	 */
	@RequestMapping("/saveGridConfigMore")
	private String saveGridConfigMore(DataGridMore dataGridConfig,
									  HttpServletRequest request) {
		String[] fieldnames = request.getParameterValues("fieldname");
		String[] fieldsqls = request.getParameterValues("fieldsql");
		dataGridConfig.setFieldname(StringUtils.join(fieldnames, ":"));
		dataGridConfig.setFieldsql(StringUtils.join(fieldsqls, ":"));
		dataGridService.saveMore(dataGridConfig);
		return CONFIG_GRID_MORE_PAGE;
	}
	
	/**
	 * 查询dataGrid配置信息做回显(多表查询)
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/getDataGridConfigMore/{id}")
	public String getDataGridConfigMore(@PathVariable("id") Integer id,
										ModelMap modelMap) {
		DataGridMore dataGridMore = dataGridService.findDataGridMoreById(id);
		String fieldname = dataGridMore.getFieldname();
		String fieldsql = dataGridMore.getFieldsql();
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		if(fieldname!=null&&!fieldname.equals("")&&fieldsql!=null&&!fieldsql.equals("")){
			String[] fieldnames = fieldname.split(":");
			String[] fieldsqls = fieldsql.split(":");
			for (int i=0;i<fieldnames.length;i++) {
				Map<String, String> map  = new HashMap<String, String>();
				map.put("fieldname", fieldnames[i]);
				map.put("fieldsql", fieldsqls[i]);
				list.add(map);
			}
		}
		//查询字段和对应的下拉数据集sql集合
		modelMap.put("list", list);
		modelMap.put("dataGridMore", dataGridMore);
		return CONFIG_GRID_MORE_PAGE;
	}
}

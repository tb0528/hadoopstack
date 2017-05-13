package com.xiaoxiaomo.storm.core.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.xiaoxiaomo.storm.core.biz.BaseService;
import com.xiaoxiaomo.storm.core.domain.web.PageVo;
import com.xiaoxiaomo.storm.core.entity.DataGrid;
import com.xiaoxiaomo.storm.core.entity.DataGridMore;
import com.xiaoxiaomo.storm.core.utils.MyJsonArray;

/**
 * datagrid 业务层
 * 
 * @author xiaoxiaomo
 *
 */
@Service
public class DataGridService extends BaseService {

	public void install(DataGrid dataGridConfig, String[] fields,
						String[] titles, String[] styles, int prikeyindex) {
		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < fields.length; i++) {
			final JSONObject jsonObject = new JSONObject();
			jsonObject.put("field", "'" + fields[i] + "'");
			jsonObject.put("title", "'" + titles[i] + "'");
			jsonObject.put("style", "'" + styles[i] + "'");
			if (prikeyindex == i) {
				jsonObject.put("checkbox", "true");
			} else {
				if (styles[i].toLowerCase().equals("int")) {
					jsonObject.put("editor", "'numberbox'");
				} else if (styles[i].toLowerCase().equals("string")) {
					jsonObject.put("editor", "'text'");
				}
			}
			jsonArray.put(jsonObject);
		}
		String string = jsonArray.toString();
		string = string.replaceAll("\"", "");
		dataGridConfig.setColums(string);
	}

	public String loadData(DataGrid dataGrid, Map<String, String> map2,
						   PageVo pageVo) {
		List<Map<String, Object>> list = getDataGrid(dataGrid, map2, pageVo);
		// 获取总记录数
		int count = getDataGridCount(dataGrid, map2);
		StringBuffer sb = new StringBuffer();
		sb.append("{\"total\":");
		sb.append("" + count);
		sb.append(",\"rows\":[");
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			if (i == 0) {
				sb.append("{");
			} else {
				sb.append(",{");
			}
			Iterator<String> iterator = map.keySet().iterator();
			int index = 0;
			while (iterator.hasNext()) {
				String key = iterator.next();
				if (index == 0) {
					sb.append("\"" + key + "\":\"" + map.get(key) + "\"");
				} else {
					sb.append(",\"" + key + "\":\"" + map.get(key) + "\"");
				}
				index++;
			}
			sb.append("}");
		}
		sb.append("]}");
		return sb.toString();
	}

	private int getDataGridCount(DataGrid dataGrid, Map<String, String> map2) {
		JdbcTemplate jdbc = dataGridDao.getJdbcTemplate();
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT COUNT(*) AS NUM FROM " + dataGrid.getTablename()
				+ " WHERE 1=1 ");
		Iterator<Entry<String, String>> it = map2.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String> next = it.next();
			sb.append(" AND " + next.getKey() + " like '%" + next.getValue()
					+ "%' ");
		}
		int queryForInt = jdbc.queryForInt(sb.toString());
		return queryForInt;
	}

	public List<Map<String, Object>> getDataGrid(DataGrid dataGrid,
												 Map<String, String> map2, PageVo pageVo) {
		// 计算分页
		// 当前页
		int intPage = pageVo.getPage() == 0 ? 1 : pageVo.getPage();
		// 每页显示条数
		int number = pageVo.getRows() == 0 ? 10 : pageVo.getRows();
		// 每页的开始记录 第一页为1 第二页为number +1
		int start = (intPage - 1) * number;

		String tablename = dataGrid.getTablename();
		String colums = dataGrid.getColums();
		JSONArray jsonArray = new JSONArray(colums);
		String colum = "";
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject object = (JSONObject) jsonArray.get(i);
			String field = object.getString("field");
			if (i == 0) {
				colum += field;
			} else {
				colum += "," + field;
			}
		}
		JdbcTemplate jdbc = dataGridDao.getJdbcTemplate();
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT " + colum + " FROM " + tablename + " WHERE 1=1 ");
		Iterator<Entry<String, String>> it = map2.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String> next = it.next();
			sbSql.append(" AND " + next.getKey() + " like '%" + next.getValue()
					+ "%' ");
		}
		sbSql.append(" LIMIT " + start + "," + number);
		List<Map<String, Object>> list = jdbc.queryForList(sbSql.toString());
		return list;
	}

	public DataGrid findDataGridById(int id) {
		List<DataGrid> list = dataGridDao.findByHql("from DataGrid where id=?",
				id);
		if (list.size() == 1) {
			return list.get(0);
		}
		return null;
	}

	public void save(DataGrid dataGridConfig) {
		dataGridDao.saveOrUpdate(dataGridConfig);
	}

	public List<DataGrid> queryAllDataGrid() {
		List<DataGrid> list = dataGridDao.findByHql("from DataGrid ");
		return list;
	}

	public String installJson(List<DataGrid> dataGridList) {
		StringBuffer sb = new StringBuffer();
		sb.append("[{ ");
		sb.append("\"id\":0,");
		sb.append("\"text\":\"我的表单(单表查询)\",");
		sb.append("\"children\":[");
		for (int i = 0; i < dataGridList.size(); i++) {
			DataGrid dataGrid = dataGridList.get(i);
			if (i != 0) {
				sb.append(",");
			}
			sb.append("{");
			sb.append("\"id\":" + dataGrid.getId() + ",");
			sb.append("\"text\":\"" + dataGrid.getName() + "\"");
			sb.append("}");
		}
		sb.append("]");
		sb.append("}]");
		return sb.toString();
	}

	/**
	 * 拼装参数
	 * 
	 * @param parameters
	 * @return
	 */
	public Map<String, String> getParameters(String parameters) {
		Map<String, String> map = new HashMap<String, String>();
		if (StringUtils.isNotEmpty(parameters)) {
			String[] split = parameters.split(",");
			for (int i = 0; i < split.length; i++) {
				String[] split2 = split[i].split(":");
				if (split2.length > 1) {
					map.put(split2[0], split2[1]);
				}
			}
		}
		return map;
	}

	/**
	 * 拼装参数
	 * 
	 * @param jsonArray
	 * @param list
	 * @param searchColumn
	 */
	public void getColumn(JSONArray jsonArray, List<Map<String, String>> list,
						  String[] searchColumn, DataGrid dataGrid) {
		String prikey = this.getPrikeyByDataGrid(dataGrid);
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject object = (JSONObject) jsonArray.get(i);
			String field = object.getString("field");
			searchColumn[i] = field;
			String title = object.getString("title");
			String style = object.getString("style");
			Map<String, String> map = new HashMap<String, String>();
			if (field.equals(prikey)) {
				map.put("checkbox", "true");
			}
			map.put("field", field);
			map.put("title", title);
			map.put("style", style);
			list.add(map);
		}
	}

	public void getColumnForSearch(JSONArray jsonArray,
								   List<Map<String, String>> list, DataGrid dataGrid) {
		String checkvalues2 = dataGrid.getCheckvalues();
		if (checkvalues2 != null) {
			String[] checkvalues = checkvalues2.split(",");
			for (int i = 0; i < checkvalues.length; i++) {
				int num = Integer.parseInt(checkvalues[i]);
				JSONObject object = (JSONObject) jsonArray.get(num);
				Map<String, String> map = new HashMap<String, String>();
				map.put("field", object.getString("field"));
				map.put("title", object.getString("title"));
				list.add(map);
			}
		}
	}

	public List<Map<String, String>> getColumForEditData(DataGrid dataGrid) {
		String colums = dataGrid.getColums();
		JSONArray jsonArray = new JSONArray(colums);
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject object = (JSONObject) jsonArray.get(i);
			String field = object.getString("field");
			String title = object.getString("title");
			Map<String, String> map = new HashMap<String, String>();
			map.put("field", field);
			map.put("title", title);
			list.add(map);
		}
		return list;
	}

	/**
	 * 通过datagrid 获取表单配置的主键字段
	 * 
	 * @param dataGrid
	 * @return
	 */
	public String getPrikeyByDataGrid(DataGrid dataGrid) {
		// 暂时仅支持一个主键
		String prikey = "";
		String colums = dataGrid.getColums();
		JSONArray jsonArray = new JSONArray(colums);
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject object = (JSONObject) jsonArray.get(i);
			if (!object.isNull("checkbox")) {
				prikey = object.getString("field");
			}
		}
		return prikey;
	}

	/**
	 * 获取每个字段的类型 格式id:int name:string
	 * 
	 * @param dataGrid
	 * @return
	 */
	public Map<String, String> getFieldsStyle(DataGrid dataGrid) {
		Map<String, String> map = new HashMap<String, String>();
		String colums = dataGrid.getColums();
		JSONArray jsonArray = new JSONArray(colums);
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject object = (JSONObject) jsonArray.get(i);
			String field = object.getString("field");
			String style = object.getString("style");
			map.put(field, style);
		}
		return map;
	}

	/**
	 * 批量执行新增，修改，删除sql 并获取执行结果
	 * 
	 * @param sql
	 *            []
	 * @return
	 */
	public boolean doExecuteBatchUpdate(String[] updateSql) {
		boolean flag = true;
		JdbcTemplate jdbcTemplate = dataGridDao.getJdbcTemplate();
		int[] batchUpdate = jdbcTemplate.batchUpdate(updateSql);
		for (int i = 0; i < batchUpdate.length; i++) {
			if (batchUpdate[i] <= 0) {
				flag = false;
			}
		}
		return flag;
	}

	/**
	 * 执行新增，修改，删除sql 并获取执行结果
	 * 
	 * @param sql
	 * @return
	 */
	public boolean doExecuteUpdate(String updateSql) {
		boolean flag = true;
		JdbcTemplate jdbcTemplate = dataGridDao.getJdbcTemplate();
		int num = jdbcTemplate.update(updateSql);
		if (num <= 0) {
			flag = false;
		}
		return flag;
	}

	/**
	 * 拼装sql
	 * 
	 * @param prikey
	 * @param styleMap
	 * @param tablename
	 * @param updateSql
	 * @param i
	 * @param object
	 */
	public void getUpdateSqlstr(String prikey, Map<String, String> styleMap,
								String tablename, String[] updateSql, int i, JSONObject object) {
		@SuppressWarnings("unchecked")
		Iterator<String> keys = object.keys();
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE " + tablename + " SET ");
		while (keys.hasNext()) {
			String key = keys.next();
			// 拼接SQL
			if (!prikey.equals(key)) {
				sql.append(getDifferentStyleSql(styleMap, key, object) + " ,");
			}
		}
		sql.append(" WHERE " + getDifferentStyleSql(styleMap, prikey, object));
		// 去掉sql中的最后一个逗号
		updateSql[i] = sql.toString().replaceAll(", WHERE", " WHERE");
	}

	/**
	 * 根据字段的不同类型获取sql
	 * 
	 * @param styleMap
	 * @param key
	 * @param object
	 * @return
	 */
	private String getDifferentStyleSql(Map<String, String> styleMap,
										String key, JSONObject object) {
		String style = styleMap.get(key).toLowerCase();
		if (style.equals("int")) {
			return key + "=" + object.getInt(key);
		} else if (style.equals("string")) {
			return key + "='" + object.getString(key) + "'";
		}
		return "";
	}

	/**
	 * 拼接完整的insertsql
	 * 
	 * @param styleMap
	 * @param parameterMap
	 * @param list
	 * @param keystr
	 * @param tablename
	 * @return
	 */
	public String getInsertSqlAll(Map<String, String> styleMap,
								  Map<String, Object> parameterMap, List<String> list, String keystr,
								  String tablename) {
		StringBuffer sb = new StringBuffer();
		sb.append(" INSERT INTO " + tablename + "(" + keystr + ") VALUES(");
		for (int i = 0; i < list.size(); i++) {
			String key = list.get(i);
			if (i != 0) {
				sb.append(",");
			}
			sb.append(getInsertSql(parameterMap, key, styleMap));

		}
		sb.append(")");
		return sb.toString();
	}

	/**
	 * 拼接insertsql
	 * 
	 * @param parameterMap
	 * @param key
	 * @param styleMap
	 */
	private String getInsertSql(Map<String, Object> parameterMap, String key,
								Map<String, String> styleMap) {
		String value = styleMap.get(key).toLowerCase();
		String[] object = (String[]) parameterMap.get(key);
		String string = "";
		if (object.length > 0) {
			string = object[0];
		}
		if (value.equals("int")) {
			int i = 0;
			if (!string.equals("")) {
				i = Integer.parseInt(string);
			}
			return i + "";
		} else if (value.equals("string")) {

			return "'" + string + "'";
		}
		return "";
	}

	public boolean saveData(Integer configId, String jsonResult) {
		DataGrid dataGrid = this.findDataGridById(configId);
		String prikey = this.getPrikeyByDataGrid(dataGrid);
		Map<String, String> styleMap = this.getFieldsStyle(dataGrid);
		String tablename = dataGrid.getTablename();
		JSONObject jsonObject = new JSONObject(jsonResult);
		boolean execResult = true;
		if (!jsonObject.isNull("update")) {
			JSONArray jsonArray = new JSONArray(jsonObject.getString("update"));
			String[] updateSql = new String[jsonArray.length()];
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject object = (JSONObject) jsonArray.get(i);
				this.getUpdateSqlstr(prikey, styleMap, tablename, updateSql, i,
						object);
			}
			execResult = this.doExecuteBatchUpdate(updateSql);
		}
		return execResult;
	}

	public boolean saveDialogData(HttpServletRequest request) {
		// 根据configid获取所有字段
		int configid = Integer.parseInt(request.getParameter("configid"));
		DataGrid dataGrid = this.findDataGridById(configid);
		Map<String, String> styleMap = this.getFieldsStyle(dataGrid);
		Map<String, Object> parameterMap = request.getParameterMap();
		Set<String> keySet = parameterMap.keySet();
		List<String> list = new ArrayList<String>(keySet);
		list.remove("configid");
		String keystr = StringUtils.join(list.iterator(), ",");
		String tablename = dataGrid.getTablename();
		String sql = this.getInsertSqlAll(styleMap, parameterMap, list, keystr,
				tablename);
		boolean result = this.doExecuteUpdate(sql);
		return result;
	}

	/**
	 * 删除数据
	 * 
	 * @param configId
	 * @param rowsResult
	 * @return
	 */
	public boolean deleteData(Integer configId, String rowsResult) {
		DataGrid dataGrid = this.findDataGridById(configId);
		String prikey = this.getPrikeyByDataGrid(dataGrid);
		Map<String, String> styleMap = this.getFieldsStyle(dataGrid);
		String tablename = dataGrid.getTablename();
		// 解析rowsresult 获取主键的值
		JSONArray jsonArray = new JSONArray(rowsResult);
		String[] keystrs = new String[jsonArray.length()];
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject object = (JSONObject) jsonArray.get(i);
			String key = object.getString(prikey);
			keystrs[i] = key;
		}
		StringBuffer sql = new StringBuffer();
		sql.append("DELETE FROM " + tablename);
		sql.append(" WHERE " + prikey + " in ");

		String style = styleMap.get(prikey).toLowerCase();
		if (style.equals("string")) {
			String joinString = StringUtils.join(keystrs, "','");
			sql.append("('" + joinString + "')");
		} else if (style.equals("int")) {
			String joinString = StringUtils.join(keystrs, ",");
			sql.append("(" + joinString + ")");
		}
		boolean executeUpdate = this.doExecuteUpdate(sql.toString());
		return executeUpdate;
	}

	public boolean deleteGridConfig(String ids, String tablename) {
		boolean flag = true;
		ids = ids.substring(1, ids.length());
		ids = ids.substring(0, ids.length() - 1);
		ids = ids.replaceAll(",", ",");
		JdbcTemplate jdbcTemplate = chartConfigDao.getJdbcTemplate();
		int num = jdbcTemplate.update("DELETE FROM " + tablename
				+ " WHERE ID IN (" + ids + ")");
		if (num <= 0) {
			flag = false;
		}
		return flag;
	}

	/**
	 * 查询多表的datagrid配置(多表查询)
	 * 
	 * @return
	 */
	public List<Map<String, Object>> queryAllDataGridMore() {
		JdbcTemplate jdbcTemplate = dataGridDao.getJdbcTemplate();
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList("SELECT ID,SQLSTR,NAME FROM DATA_GRID_MORE");
		return list;
	}

	/**
	 * 查询多表的datagrid配置(多表查询)
	 * 
	 * @return
	 */
	public DataGridMore findDataGridMoreById(Integer id) {
		JdbcTemplate jdbcTemplate = dataGridDao.getJdbcTemplate();
		Map<String, Object> queryForMap = jdbcTemplate.queryForMap(
				" SELECT ID,SQLSTR,NAME,FIELDNAME,FIELDSQL FROM DATA_GRID_MORE WHERE ID = ?", id);
		DataGridMore dataGridMore = new DataGridMore();
		dataGridMore.setId(Integer.parseInt(queryForMap.get("ID").toString()));
		dataGridMore.setName(queryForMap.get("NAME").toString());
		dataGridMore.setSqlstr(queryForMap.get("SQLSTR").toString());
		dataGridMore.setFieldname(queryForMap.get("FIELDNAME")==null?"":queryForMap.get("FIELDNAME").toString());
		dataGridMore.setFieldsql(queryForMap.get("FIELDSQL")==null?"":queryForMap.get("FIELDSQL").toString());
		return dataGridMore;
	}

	/**
	 * 组装左侧树json(多表查询)
	 * 
	 * @param dataGridList
	 * @return
	 */
	public String installJsonForMore(List<Map<String, Object>> dataGridList) {
		StringBuffer sb = new StringBuffer();
		sb.append("[{ ");
		sb.append("\"id\":0,");
		sb.append("\"text\":\"我的表单(多表查询)\",");
		sb.append("\"children\":[");
		for (int i = 0; i < dataGridList.size(); i++) {
			Map<String, Object> dataGrid = dataGridList.get(i);
			if (i != 0) {
				sb.append(",");
			}
			sb.append("{");
			sb.append("\"id\":" + dataGrid.get("id").toString() + ",");
			sb.append("\"text\":\"" + dataGrid.get("name").toString() + "\"");
			sb.append("}");
		}
		sb.append("]");
		sb.append("}]");
		return sb.toString();
	}

	/**
	 * 获取多表sql中的查询字段(多表查询) sql 格式 select a.name as aname, b.name as bname from
	 * one a,two b where a.id = b.id
	 * 
	 * @param dataGridMore
	 */
	public String[] getColumnsForMoreTable(DataGridMore dataGridMore) {
		String sqlstr = dataGridMore.getSqlstr().toUpperCase();

		// 截取sql中select和from之间的字段信息
		sqlstr = sqlstr.substring(sqlstr.indexOf("SELECT") + 6,
				sqlstr.indexOf("FROM"));
		String[] split = sqlstr.split(",");
		for (int i = 0; i < split.length; i++) {
			String key = split[i];
			if (key.indexOf("AS") > -1) {
				split[i] = key.substring(key.indexOf("AS") + 2, key.length())
						.trim();
			}else{
				split[i] = key.trim();
			}
		}
		return split;
	}

	/**
	 * 组装多表的表头(多表查询)
	 * 
	 * @param colums
	 * @return
	 */
	public String getColumnJsonForMore(String[] colums) {
		MyJsonArray jsonArray = new MyJsonArray();
		for (int i = 0; i < colums.length; i++) {
			JSONObject object = new JSONObject();
			object.put("field", "'" + colums[i] + "'");
			object.put("title", "'" + colums[i] + "'");
			jsonArray.put(object);
		}
		return jsonArray.toString();
	}

	/**
	 * 组装多表的表头list(多表查询)
	 * 
	 * @param dataGridMore
	 */
	public List<Map<String, Object>> getColumnListForMore(
			DataGridMore dataGridMore) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String sqlstr = dataGridMore.getSqlstr().toUpperCase();
		String fieldname = dataGridMore.getFieldname().toUpperCase();
		String fieldsql = dataGridMore.getFieldsql();
		List<String> fieldNameList = new ArrayList<String>();
		List<String> fieldSqlList = new ArrayList<String>();
		if(fieldname!=null&&!fieldname.equals("")&&fieldsql!=null&&!fieldsql.equals("")){
			fieldNameList = Arrays.asList(fieldname.split(":"));
			fieldSqlList = Arrays.asList(fieldsql.split(":"));
		}
		// 截取sql中select和from之间的字段信息
		sqlstr = sqlstr.substring(sqlstr.indexOf("SELECT") + 6,
				sqlstr.indexOf("FROM"));
		String[] split = sqlstr.split(",");
		for (int i = 0; i < split.length; i++) {
			String[] split2 = split[i].split("AS");
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("field", split2[0].trim());
			if(split2.length==2){
				String trim1 = split2[1].trim();
				putMap(fieldNameList, fieldSqlList, map, trim1);
			}else{
				map.put("title", split2[0].trim());
				map.put("style", "string");
			}
			list.add(map);
		}
		return list;
	}

	private void putMap(List<String> fieldNameList, List<String> fieldSqlList,
						Map<String, Object> map, String trim1) {
		if(fieldNameList.size()>0&&fieldNameList.contains(trim1)){
			int index = fieldNameList.indexOf(trim1);
			JdbcTemplate jdbcTemplate = dataGridDao.getJdbcTemplate();
			String sqlstr = fieldSqlList.get(index);
			//此处的sqlstr可能是select...语句  ，也可能是字符串
			List<String> queryForList = new ArrayList<String>();
			if(sqlstr.toUpperCase().trim().startsWith("SELECT")){
				queryForList = jdbcTemplate.queryForList(sqlstr, String.class);
			}else{
				String[] split = sqlstr.split(",");
				for (int i = 0; i < split.length; i++) {
					queryForList.add(split[i]);
				}
			}
			map.put("title",trim1);
			map.put("value", queryForList);
			map.put("style", "list");
		}else{
			map.put("title",trim1);
		}
	}

	/**
	 * 加载数据(多表查询)
	 * 
	 * @param dataGrid
	 * @param map2
	 * @param pageVo
	 * @return
	 */
	public String loadDataMore(DataGridMore dataGridMore,
							   Map<String, String> map2, PageVo pageVo) {
		List<Map<String, Object>> list = getDataGridMore(dataGridMore, map2,
				pageVo);
		// 获取总记录数
		int count = getDataGridCountMore(dataGridMore, map2);
		StringBuffer sb = new StringBuffer();
		sb.append("{\"total\":");
		sb.append("" + count);
		sb.append(",\"rows\":[");
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			if (i == 0) {
				sb.append("{");
			} else {
				sb.append(",{");
			}
			Iterator<String> iterator = map.keySet().iterator();
			int index = 0;
			while (iterator.hasNext()) {
				String key = iterator.next();
				if (index == 0) {
					sb.append("\"" + key + "\":\"" + map.get(key) + "\"");
				} else {
					sb.append(",\"" + key + "\":\"" + map.get(key) + "\"");
				}
				index++;
			}
			sb.append("}");
		}
		sb.append("]}");
		return sb.toString();
	}

	/**
	 * 查询数据(多表查询)
	 * 
	 * @param dataGridMore
	 * @param map2
	 * @param pageVo
	 * @return
	 */
	public List<Map<String, Object>> getDataGridMore(DataGridMore dataGridMore,
													 Map<String, String> map2, PageVo pageVo) {
		// 计算分页
		// 当前页
		int intPage = pageVo.getPage() == 0 ? 1 : pageVo.getPage();
		// 每页显示条数
		int number = pageVo.getRows() == 0 ? 10 : pageVo.getRows();
		// 每页的开始记录 第一页为1 第二页为number +1
		int start = (intPage - 1) * number;

		JdbcTemplate jdbc = dataGridDao.getJdbcTemplate();
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT * FROM (" + dataGridMore.getSqlstr().toUpperCase());
		sbSql.append(" ) AS TMP WHERE 1=1 ");

		Iterator<Entry<String, String>> it = map2.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String> next = it.next();
			sbSql.append(" AND TMP." + next.getKey().trim() + " like '%"
					+ next.getValue() + "%' ");
		}
		
		sbSql.append(" LIMIT " + start + "," + number);
		List<Map<String, Object>> list = jdbc.queryForList(sbSql.toString());
		return list;
	}

	/**
	 * 获取记录总数(多表查询)
	 * 
	 * @param dataGrid
	 * @param map2
	 * @return
	 */
	private int getDataGridCountMore(DataGridMore dataGridMore,
			Map<String, String> map2) {
		JdbcTemplate jdbc = dataGridDao.getJdbcTemplate();
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT COUNT(*) FROM (");
		sb.append("SELECT * FROM (" + dataGridMore.getSqlstr());
		sb.append(" ) AS TMP WHERE 1=1 ");
		Iterator<Entry<String, String>> it = map2.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String> next = it.next();
			sb.append(" AND TMP." + next.getKey().trim() + " like '%"
					+ next.getValue() + "%' ");
		}
		sb.append(") AS TMP2");
		int queryForInt = jdbc.queryForInt(sb.toString());
		return queryForInt;
	}

	/**
	 * 多表查询配置的保存方法
	 * 
	 * @param dataGridConfig
	 */
	public void saveMore(DataGridMore dataGridMoreConfig) {
		dataGridDao.saveOrUpdate(dataGridMoreConfig);
	}

}

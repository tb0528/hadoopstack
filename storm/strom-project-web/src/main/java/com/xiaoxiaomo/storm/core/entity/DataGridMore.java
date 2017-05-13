package com.xiaoxiaomo.storm.core.entity;

import java.io.Serializable;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 动态表单实体类（针对多表）
 * @author xiaoxiaomo
 *
 */
@Entity
@Table(name="data_grid_more")
public class DataGridMore implements Idable,Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * id 主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	/**
	 * 存储多表查询的sql select a.name ,b.value from a ,b where a.id=b.id
	 */
	@Column
	private String sqlstr;

	/**
	 * 名称 ：sql查询结果的意义简称
	 */
	private String name;
	/**
	 * 对应的字段名称
	 */
	private String fieldname;

	/**
	 * 对应的字段的数据集
	 */
	private String fieldsql;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getSqlstr() {
		return sqlstr;
	}
	public void setSqlstr(String sqlstr) {
		this.sqlstr = sqlstr;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFieldname() {
		return fieldname;
	}
	public void setFieldname(String fieldname) {
		this.fieldname = fieldname;
	}
	public String getFieldsql() {
		return fieldsql;
	}
	public void setFieldsql(String fieldsql) {
		this.fieldsql = fieldsql;
	}
	@Override
	public String toString() {
		return "DataGridMore [id=" + id + ", sqlstr=" + sqlstr + ", name="
				+ name + ", fieldname=" + fieldname + ", fieldsql=" + fieldsql
				+ "]";
	}
	
}

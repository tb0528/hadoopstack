package com.xiaoxiaomo.storm.core.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 自动图形部分，前端用户输入的具体配置信息
 *
 * @author XXO
 *
 */
@Entity
@Table(name="chart_config")
public class ChartConfig implements Idable, Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	/**
	 * 图表类型
	 */
	@Column(nullable=false)
	private String style;
	/**
	 * 标题
	 */
	@Column(nullable=false)
	private String text;
	/**
	 * x轴的查询语句
	 */
	@Column
	private String xsql;
	/**
	 * y轴的值
	 * 注意：用户输入的内容包括多个“标题”和“sql”
	 * 存储方式，例如：[{title:'',sql:''},{title:'',sql:''}]
	 */
	@Column
	private String ysql;
	/**
	 * y轴单位
	 */
	@Column
	private String ylabel;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	public String getText() {
		return text;
	}
	public void setText(String title) {
		this.text = title;
	}
	public String getXsql() {
		return xsql;
	}
	public String getYsql() {
		return ysql;
	}
	public void setYsql(String ysql) {
		this.ysql = ysql;
	}
	public void setXsql(String xstring) {
		this.xsql = xstring;
	}

	public String getYlabel() {
		return ylabel;
	}
	public void setYlabel(String ylable) {
		this.ylabel = ylable;
	}
	@Override
	public String toString() {
		return "ChartConfig [style=" + style + ", text=" + text + ", xsql=" + xsql + ", ysql=" + ysql + ", ylabel="
				+ ylabel + "]";
	}
	
	
}

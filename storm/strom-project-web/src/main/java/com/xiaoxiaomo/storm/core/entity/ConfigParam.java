package com.xiaoxiaomo.storm.core.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 配置项
 * @author XXO
 *
 */
@Entity
@Table(name="config_param")
public class ConfigParam implements Idable, Serializable {
	private static final long serialVersionUID = 1L;
	public static final String STYLE_INCOME = "收入";
	public static final String STYLE_COST = "支出";
	
	@Id  
	@GeneratedValue(strategy = GenerationType.AUTO) 
	private Integer id;
	
	@Column
	private String style;
	
	@Column(nullable=false)
	private String name;
	
	@Column
	private String value;
	
	@Column(name="show_order")
	private Integer showOrder;
	
	@Column
	private String remark;
	
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Integer getShowOrder() {
		return showOrder;
	}
	public void setShowOrder(Integer showOrder) {
		this.showOrder = showOrder;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}

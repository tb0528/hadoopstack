<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>图表</title>
<%@include file="../crm/common.jsp" %>
<style type="text/css"></style>
<script  type="text/javascript">
	var root = "${crm}";
	$(function(){
		$("#chart").append("<div id=\"w\" class=\"easyui-window\"  data-options=\"minimizable:false,maximizable:false,title:' '\" style=\"width:600px;height:400px;\"><div id=\"main\" style=\"height:350px;width:600\"></div></div>");
	});
</script>	
</head>
<body class="easyui-layout">
	<div data-options="region:'west',split:true,title:' '" style="width:200px;">
		<div style="width:195px;height:30px;border: 1px solid #ACD6FF">
			<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="loadConfig()">新增</a>|
			<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true" onclick="updateConfig()">修改</a>|
			<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="deleteConfig()">删除</a>
		</div>
		<div class="easyui-panel" style="width:195px;height:auto;">
			<ul id="tt"  class="easyui-tree" data-options="method:'get',animate:true,checkbox:true,onlyLeafCheck:true"></ul>
		</div>
		<br/>
		<br/>
		<div style="width:195px;height:30px;border: 1px solid #ACD6FF">
			<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="loadDataGridConfig()">新增</a>|
			<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true" onclick="updateDataGridConfig()">修改</a>|
			<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="deleteDataGridConfig()">删除</a>
		</div>
		<div class="easyui-panel" style="width:195px;height:auto;">
			<ul id="tt1"  class="easyui-tree" data-options="method:'get',animate:true,checkbox:true,onlyLeafCheck:true"></ul>
		</div>
		<br/>
		<br/>
		<div style="width:195px;height:30px;border: 1px solid #ACD6FF">
			<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="loadDataGridMoreConfig()">新增</a>|
			<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true" onclick="updateDataGridMoreConfig()">修改</a>|
			<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="deleteDataGridMoreConfig()">删除</a>
		</div>
		<div class="easyui-panel" style="width:195px;height:auto;">
			<ul id="tt2"  class="easyui-tree" data-options="method:'get',animate:true,checkbox:true,onlyLeafCheck:true"></ul>
		</div>
	</div>
	<div id="tabs" data-options="region:'center'">
		<div id="chart">
		</div>
		<div id="config">
			
		</div>
	</div>	
</body>
<script type="text/javascript">
		$('#tt').tree({
			url:'${crm}/chart/loadTree',
			onClick: function(node){
				if(node.id!=0){
					loadChart(node.id,node.attributes.style);
				}
			}
		});
		$('#tt1').tree({
			url:'${crm}/dataGrid/loadTree',
			onClick: function(node){
				if(node.id!=0){
					loadDataGrid(node.id);
				}
			}
		});
		$('#tt2').tree({
			url:'${crm}/dataGrid/loadTreeMore',
			onClick: function(node){
				if(node.id!=0){
					loadDataGridMore(node.id);
				}
			}
		});
</script>
</html>	
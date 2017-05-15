<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>新增数据</title>
</head>
<body>
<div style="text-align:center">
	<form id="form1" method="post">
		<input type="hidden" name="configid" value="${configid}"/>
		<table style="width: 70%;margin:auto">
			<c:forEach items="${columnList}" var="map">
				<tr>
					<td width="100px;">${map['title']}：</td><td><input class="easyui-textbox" required="true" type="text" name="${map['field']}" style="width:180px;" /></td>
				</tr>
			</c:forEach> 
			<tr>
			</tr>
			<tr>
				<td></td>
				<td><a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-save'"  onclick="saveDialogData();">保存</a>&nbsp;&nbsp;&nbsp;<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-no'"  onclick="closeDialog();">关闭</a></td>
			</tr>
		</table>
	</form>
</div>
<body>
</html>	
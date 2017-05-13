<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>图表</title>
</head>
<body>
	<form id="form1" method="post">
		<input type="hidden" name="id" value="${chartConfig.id }">
				<div>
					<table>
						<tr><td>图表类型：</td><td><select class="easyui-combobox" id="style" name="style"  style="width:180px;"><option value="line" ${chartConfig.style=='line'?'selected':''}>折线图</option><option value="bar" ${chartConfig.style=='bar'?'selected':''}>柱状图</option></select></td></tr>
						<tr><td>标题：</td><td><input class="easyui-textbox" required="true" type="text" id="text" name="text" style="width:180px;" value="${chartConfig.text}"></td></tr>
						<tr><td>横轴SQL：</td><td><input class="easyui-textbox" required="true" type="text" id="xsql" name="xsql" style="width:180px;" value="${chartConfig.xsql}"></td>
						<td>纵轴单位：</td><td><input class="easyui-textbox" required="true"  type="text" id="ylabel" name="ylabel" style="width:180px;" value="${chartConfig.ylabel}"></td></tr>
					</table>
					<br/><strong>以下是序列信息</strong>
					<table id="tab1">
						<c:forEach var="map" items="${list}" varStatus="stat">  
							<c:if test="${stat.first}">
								<tr id="0">
									<td>序列名称：</td><td><input class="easyui-textbox" required="true" type="text" name="ytext" style="width:180px;" value="${map['title'] }"></td>
									<td>SQL：</td><td><input class="easyui-textbox" required="true" type="text" name="ysql" style="width:380px;" value="${map['sql'] }"><a href="#"  class="easyui-linkbutton" data-options="iconCls:'icon-add'" onclick="addRow();" style="width:55px;">add</a></td>
								</tr>
							</c:if>
							<c:if test="${!stat.first}">
								<tr id="${stat.index}">
									<td>序列名称：</td><td><input class="easyui-textbox" required="true" type="text" name="ytext" style="width:180px;" value="${map['title'] }"></td>
									<td>SQL：</td><td><input class="easyui-textbox"  required="true" type="text" id= name="ysql" style="width:380px;" value="${map['sql'] }"><a href="#"  class="easyui-linkbutton" data-options="iconCls:'icon-remove'" onclick="delRow(${stat.index});" style="width:55px;">del</a></td>
								</tr>
							</c:if>
						 </c:forEach>
					</table>
					<div>
					 <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-save'" style="width:80px;margin-left:100px;margin-top:10px;" onclick="subDialog();">修改</a>
					 <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-no'" style="width:80px;margin-left:100px;margin-top:10px;" onclick="closeDialog();">关闭</a>
					</div>
				</div>
	</form>
</body>
</html>	
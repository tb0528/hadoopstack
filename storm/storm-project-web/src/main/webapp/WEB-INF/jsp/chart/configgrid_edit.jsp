<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>grid配置修改页面</title>
<%@include file="../crm/common.jsp" %>	
</head>
<body>
	<form id="form1" method="post">
	<input type="hidden" name="id" value="${dataGrid.id }">
		<div>
			<table>
				<tr>
					<td>数据库表名：</td><td><input  class="easyui-textbox" required="true"  type="text" name="tablename" style="width:120px;" value="${dataGrid.tablename} "/></td>
					<td>数据库表中文名称：</td><td><input  class="easyui-textbox" required="true"  type="text" name="name" style="width:120px;"  value="${dataGrid.name}"/></td>
				</tr>
			</table>
			<table id="tab1">
				<c:forEach var="map" items="${list}" varStatus="stat">  
					<c:if test="${stat.first}">
						<tr id="0">
							<td>数据库字段：</td><td><input class="easyui-textbox" required="true" type="text" name="field" style="width:120px;" value='${map["field"] }'></td>
							<td>页面显示名：</td><td><input class="easyui-textbox" required="true" type="text" name="title" style="width:120px;" value='${map["title"] }'><input type="radio" name="prikey" value="0" <c:if test="${map['checkbox']==true}">checked='checked'</c:if>/>主键<input type="checkbox" name="searchkey" value="0" <c:if test="${fn:contains(dataGrid.checkvalues, 0)}">checked='checked'</c:if>/>作为查询条件<select name="style"><option value="int" <c:if test="${map['style']=='int' }">selected='selected'</c:if>>int</option><option value="string" <c:if test="${map['style']=='string' }">selected='selected'</c:if>>string</option></select><a href="#"  class="easyui-linkbutton" data-options="iconCls:'icon-add'" onclick="addDataGridRow();" style="width:55px;">add</a></td>
						</tr>
					</c:if>
					<c:if test="${!stat.first}">
						<tr id="${stat.index}">
							<td>数据库字段：</td><td><input class="easyui-textbox" required="true" type="text" name="field" style="width:120px;" value='${map["field"] }'></td>
							<td>页面显示名：</td><td><input class="easyui-textbox" required="true" type="text" name="title" style="width:120px;" value='${map["title"] }'><input type="radio" name="prikey" value="${stat.index}" <c:if test="${map['checkbox']==true}">checked='checked'</c:if>/>主键<input type="checkbox" name="searchkey" value="${stat.index }" <c:if test="${fn:contains(dataGrid.checkvalues, stat.index)}">checked='checked'</c:if>/>作为查询条件<select name="style"><option value="int" <c:if test="${map['style']=='int' }">selected='selected'</c:if>>int</option><option value="string" <c:if test="${map['style']=='string' }">selected='selected'</c:if>>string</option></select><a href="#"  class="easyui-linkbutton" data-options="iconCls:'icon-remove'" onclick="delRow(${stat.index});" style="width:55px;">del</a></td>
						</tr>
					</c:if>
				 </c:forEach>
			</table>
				<div>
				 <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-save'" style="width:80px;margin-left:100px;margin-top:10px;" onclick="subDataGridDialog();">修改</a>
				 <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-no'" style="width:80px;margin-left:100px;margin-top:10px;" onclick="closeDialog();">关闭</a>
				</div>
		</div>
	</form>
</body>
</html>	
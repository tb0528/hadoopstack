<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>grid配置页面</title>
<%@include file="../crm/common.jsp" %>	
</head>
<body>
	<form id="form1" method="post">
	<input type="hidden" name="id" value="${dataGridMore.id }"/>
		<div>
			<table>
				<tr>
					<td>表单名称：</td>
					<td><input  class="easyui-textbox" required="true"  type="text" name="name" style="width:120px;" value="${dataGridMore.name }"/></td>
				</tr>
			</table>
			<br/>
			<br/>
			<strong>给字段配置数据集SQL</strong>
			<table id="tab1">
				<c:choose>
					<c:when test="${fn:length(list)<=0}">
							<tr id="0">
								<td>字段名称：</td><td><input class="easyui-textbox"  type="text" id="sqlstr" name="fieldname" style="width:120px;" value=""></td>
								<td>数据集SQL：</td><td><input class="easyui-textbox"  type="text" id="sqlstr" name="fieldsql" style="width:200px;" value=""><a href="#"  class="easyui-linkbutton" data-options="iconCls:'icon-add'" onclick="addRowForMore();" style="width:55px;">add</a>&nbsp;&nbsp;格式：select name from tablename</td>
							</tr>
					</c:when>
					<c:otherwise>
						<c:forEach var="map" items="${list}" varStatus="stat">
							<c:choose>
								<c:when test="${stat.index==0}">
									<tr id="${stat.index }">
										<td>字段名称：</td><td><input class="easyui-textbox"  type="text" id="sqlstr" name="fieldname" style="width:120px;" value="${map['fieldname'] }"></td>
										<td>数据集SQL：</td><td><input class="easyui-textbox"  type="text" id="sqlstr" name="fieldsql" style="width:200px;" value="${map['fieldsql'] }"><a href="#"  class="easyui-linkbutton" data-options="iconCls:'icon-add'" onclick="addRowForMore();" style="width:55px;">add</a>&nbsp;&nbsp;格式：select name from tablename</td>
									</tr>
								</c:when>
								<c:otherwise>
									<tr id="${stat.index }">
										<td>字段名称：</td><td><input class="easyui-textbox"  type="text" id="sqlstr" name="fieldname" style="width:120px;" value="${map['fieldname'] }"></td>
										<td>数据集SQL：</td><td><input class="easyui-textbox"  type="text" id="sqlstr" name="fieldsql" style="width:200px;" value="${map['fieldsql'] }"><a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-remove'" onclick="delRow(${stat.index });" style="width:55px;">del</a></td>
									</tr>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</c:otherwise>
				</c:choose>
			</table>
			<br/>
			<br/>
			<table>
					<tr>
						<td>SQL语句：</td><td><input class="easyui-textbox" required="true" type="text" id="sqlstr" name="sqlstr" style="width:600px;height: 50px;" value="${dataGridMore.sqlstr }"></td>
					</tr>
			</table>
				<div>
				 <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-save'" style="width:80px;margin-left:100px;margin-top:10px;" onclick="subDataGridMoreDialog();">提交</a>
				 <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-no'" style="width:80px;margin-left:100px;margin-top:10px;" onclick="closeDialog();">关闭</a>
				</div>
		</div>
	</form>
</body>
</html>	
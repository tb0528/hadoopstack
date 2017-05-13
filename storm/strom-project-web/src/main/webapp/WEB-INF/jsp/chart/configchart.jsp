<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>图表</title>
<%@include file="../crm/common.jsp" %>
</head>
<body>
	<form id="form1" method="post"  >
				<div>
					<table>
						<tr><td>图表类型：</td><td><select class="easyui-combobox" id="style" name="style"  style="width:180px;"><option value="line">折线图</option><option value="bar">柱状图</option></select></td></tr>
						<tr><td>标题：</td><td><input class="easyui-textbox" required="true" type="text" id="text" name="text" style="width:180px;"></td></tr>
						<tr><td>横轴SQL：</td><td><input class="easyui-textbox" required="true" type="text" id="xsql" name="xsql" style="width:180px;"></td>
						<td>纵轴单位：</td><td><input class="easyui-textbox" required="true"  type="text" id="ylabel" name="ylabel" style="width:180px;"></td></tr>
					</table>
					<br/><strong>以下是序列信息</strong>
					<table id="tab1">
						<tr id="0">
							<td>序列名称：</td><td><input class="easyui-textbox" required="true" type="text" name="ytext" style="width:180px;"></td>
							<td>SQL：</td><td><input class="easyui-textbox" required="true" type="text" name="ysql" style="width:180px;"><a href="#"  class="easyui-linkbutton" data-options="iconCls:'icon-add'" onclick="addRow();" style="width:55px;">add</a>
						</td>
						
						</tr>
					</table>
					<div>
					 <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-save'" style="width:80px;margin-left:100px;margin-top:10px;" onclick="subDialog();">提交</a>
					 <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-no'" style="width:80px;margin-left:100px;margin-top:10px;" onclick="closeDialog();">关闭</a>
					</div>
				</div>
	</form>
</body>
</html>	
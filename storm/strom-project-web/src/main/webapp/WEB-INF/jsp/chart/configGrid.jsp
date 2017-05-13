<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>grid配置页面</title>
</head>
<body>
	<form id="form1" method="post">
		<div>
			<table>
				<tr>
					<td>数据库表名：</td><td><input  class="easyui-textbox" required="true"  type="text" name="tablename" style="width:120px;"/></td>
					<td>数据库表中文名称：</td><td><input  class="easyui-textbox" required="true"  type="text" name="name" style="width:120px;"/></td>
				</tr>
			</table>
			<table id="tab1">
					<tr id="0">
						<td>数据库字段：</td><td><input class="easyui-textbox" required="true" type="text" id="field" name="field" style="width:120px;"></td>
						<td>页面显示名：</td><td><input class="easyui-textbox" required="true" type="text" id="title" name="title" style="width:120px;"><input type="radio" name="prikey" value="0"/>主键<input type="checkbox" name="searchkey" value="0"/>作为查询条件<select name="style"><option value="int">int</option><option value="string">string</option></select><a href="#"  class="easyui-linkbutton" data-options="iconCls:'icon-add'" onclick="addDataGridRow();" style="width:55px;">add</a></td>
					</tr>
			</table>
				<div>
				 <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-save'" style="width:80px;margin-left:100px;margin-top:10px;" onclick="subDataGridDialog();">提交</a>
				 <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-no'" style="width:80px;margin-left:100px;margin-top:10px;" onclick="closeDialog();">关闭</a>
				</div>
		</div>
	</form>
</body>
</html>	
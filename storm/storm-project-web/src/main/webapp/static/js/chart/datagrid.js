function loadDataGrid(id) {
	if (id) {
		window.open(root + "/dataGrid/index/" + id);
	} else {
		$.messager.alert("操作提示", "没有获取到ID！");
	}
}

function loadDataGridConfig() {
	$('#config').dialog({
		title : '新增配置',
		width : 800,
		height : 300,
		closed : false,
		cache : false,
		resizable : true,
		href : root + '/dataGrid/toConfigGridPage',
		modal : true
	});
}

function updateDataGridConfig() {
	var nodes = $('#tt1').tree('getChecked');
	var num = nodes.length;
	if (num == 0) {
		$.messager.alert("操作提示", "请选择一个表单");
		return;
	}
	if (num > 1) {
		$.messager.alert("操作提示", "只能选择一个表单");
		return;
	}
	$('#config').dialog({
		title : '修改配置',
		width : 800,
		height : 300,
		closed : false,
		cache : false,
		resizable : true,
		href : root + '/dataGrid/getDataGridConfig/' + nodes[0].id,
		modal : true
	});
}

function addDataGridRow() {
	var trid = $("#tab1 tr").length;
	var targetObj = $(
			"<tr id=\""
					+ trid
					+ "\"><td>数据库字段：</td><td><input class=\"easyui-textbox\" required=\"true\" type=\"text\" name=\"field\" style=\"width:120px;\"></td><td>页面显示名：</td><td><input class=\"easyui-textbox\" required=\"true\" type=\"text\" name=\"title\" style=\"width:120px;\"><input type=\"radio\" name=\"prikey\" value=\""
					+ trid
					+ "\"/>主键<input type=\"checkbox\" name=\"searchkey\" value=\""
					+ trid
					+ "\"/>作为查询条件<select name=\"style\"><option value=\"int\">int</option><option value=\"string\">string</option></select><a href=\"#\" class=\"easyui-linkbutton\" data-options=\"iconCls:'icon-remove'\" onclick=\"delRow("
					+ trid + ");\" style=\"width:55px;\">del</a></td></tr>")
			.appendTo("#tab1");
	$.parser.parse(targetObj);
}
function subDataGridDialog() {
	if ($('input:radio:checked').val()) {
		$('#form1').form('submit', {
			url : root + '/dataGrid/saveGridConfig',
			success : function(data) {
				$.messager.alert("操作提示", "配置保存成功！");
				$("#tt1").tree('reload');
			}
		});
	} else {
		$.messager.alert("操作提示", "请选择一个字段作为主键");
	}
}
// datagrid数据界面的相关function start--------------------------------------------
var editIndex = undefined;
function endEditing() {
	if (editIndex == undefined) {
		return true
	}
	if ($('#dg').datagrid('validateRow', editIndex)) {
		$('#dg').datagrid('endEdit', editIndex);
		editIndex = undefined;
		return true;
	} else {
		return false;
	}
}
function onClickCell(index, field) {
	if (endEditing()) {
		$('#dg').datagrid('selectRow', index).datagrid('editCell', {
			index : index,
			field : field
		});
		editIndex = index;
	}
}
// 保存数据
function saveData() {
	getPrikey();
	if (endEditing()) {
		if ($("#dg").datagrid('getChanges').length) {
			// var inserted = $("#dg").datagrid('getChanges', "inserted");
			// var deleted = $("#dg").datagrid('getChanges', "deleted");
			var updated = $("#dg").datagrid('getChanges', "updated");
			// 把新增，删除，更新的数据放到对象传递到后台
			var effectRow = new Object();
			if (updated.length) {
				effectRow.update = JSON.stringify(updated);
				var jsonResult = JSON.stringify(effectRow);
				$.ajax({
					url : root + "/dataGrid/saveData",
					type : "post",
					data : {
						configId : $("#id").val(),
						jsonResult : jsonResult
					},
					dataType : 'text',
					error : function(msg) {
						$.messager.alert("error", "执行出错！");
					},
					success : function(text) {
						$.messager.alert("操作提示", text);
					}
				});

			}
		} else {
			$.messager.alert("操作提示", "没有需要保存的数据！");
		}
	}
}
// 获取 主键
function getPrikey() {
	var opts = $('#dg').datagrid('options');
	for (var i = 0; i < opts.columns[0].length; i++) {
		if (opts.columns[0][i].checkbox) {
			this.prikey = opts.columns[0][i].field;
		}
	}
	if (prikey == '') {
		$.messager.alert("操作提示", "没有设置主键，不能对数据进行新增、修改、删除、等操作！");
		return;
	}
}
// 跳到新增界面
function append() {
	$('#config').dialog({
		title : '新增数据',
		width : 500,
		height : 300,
		closed : false,
		cache : false,
		resizable : true,
		href : root + '/dataGrid/toAddData/' + $("#id").val(),
		modal : true
	});
}
// 重置
function reset() {
	$("#dg").datagrid('reload');
}
// 删除
function deleteData() {
	var rows = $("#dg").datagrid("getSelections");
	if (rows.length == 0) {
		$.messager.alert("error", "请至少选择一个！");
		return;
	}
	var rowsResult = JSON.stringify(rows);
	$.ajax({
		url : root + "/dataGrid/deleteData",
		type : "post",
		data : {
			configId : $("#id").val(),
			rowsResult : rowsResult
		},
		dataType : 'text',
		error : function(msg) {
			$.messager.alert("error", "执行出错！");
		},
		success : function(text) {
			$.messager.alert("操作提示", text);
			$("#dg").datagrid('reload');
		}
	});
}

// 保存新增数据
function saveDialogData() {
	$('#form1').form('submit', {
		url : root + '/dataGrid/saveDialogData',
		success : function(data) {
			$.messager.alert("操作提示", data);
			$("#dg").datagrid('reload');
		}
	});
}

// datagrid数据界面的相关function end--------------------------------------------


//多表查询grid相关func strat-------------------------------------------------
function loadDataGridMore(id) {
	if (id) {
		window.open(root + "/dataGrid/indexMore/" + id);
	} else {
		$.messager.alert("操作提示", "没有获取到ID！");
	}
}
function loadDataGridMoreConfig() {
	$('#config').dialog({
		title : '新增配置',
		width : 800,
		height : 300,
		closed : false,
		cache : false,
		resizable : true,
		href : root + '/dataGrid/toConfigGridMorePage',
		modal : true
	});
}


function subDataGridMoreDialog() {
	$('#form1').form('submit', {
		url : root + '/dataGrid/saveGridConfigMore',
		success : function(data) {
			$.messager.alert("操作提示", "配置保存成功！");
			$("#tt2").tree('reload');
		}
	});
}


function updateDataGridMoreConfig() {
	var nodes = $('#tt2').tree('getChecked');
	var num = nodes.length;
	if (num == 0) {
		$.messager.alert("操作提示", "请选择一个表单");
		return;
	}
	if (num > 1) {
		$.messager.alert("操作提示", "只能选择一个表单");
		return;
	}
	$('#config').dialog({
		title : '修改配置',
		width : 800,
		height : 300,
		closed : false,
		cache : false,
		resizable : true,
		href : root + '/dataGrid/getDataGridConfigMore/' + nodes[0].id,
		modal : true
	});
}

function addRowForMore(){
	var trid = $("#tab1 tr").length;
	var targetObj = $("<tr id=\""+trid+"\"><td>字段名称：</td><td><input class=\"easyui-textbox\"  type=\"text\" id=\"sqlstr\" name=\"fieldname\" style=\"width:120px;\"></td><td>数据集SQL：</td><td><input class=\"easyui-textbox\"  type=\"text\" id=\"sqlstr\" name=\"fieldsql\" style=\"width:200px;\"><a href=\"#\" class=\"easyui-linkbutton\" data-options=\"iconCls:'icon-remove'\" onclick=\"delRow("+trid+");\" style=\"width:55px;\">del</a></td></tr>").appendTo("#tab1");
	$.parser.parse(targetObj);
	
}

//多表查询grid相关func end-------------------------------------------------


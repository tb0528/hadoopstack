//加载chart
	function loadChart(id,style){
		 //模块化包引入
		 require.config({
            packages: [
                {
                    name: 'echarts',
                    location: root+'/js/echarts/src',
                    main: 'echarts'
                },
                {
                    name: 'zrender',
                    location: root+'/js/zrender/src',
                    main: 'zrender'
                }
            ]
        });
		//通过异步请求后台获取option		
		 $.ajax({
           url:root+"/chart/"+style+"Chart/"+id,
           type:"get",
           data:{},
           dataType:'text',
           error: function(msg){  
        	   $.messager.alert("操作提示", "请求chart配置信息出错！");
           },
           success: function(text) { 
        	   var option = eval('('+text+')')
	        	//使用
	       		require(
	       			[
	       				'echarts',
	       				'echarts/chart/bar',
	       				'echarts/chart/line',
	       				'echarts/chart/pie',
	       				'echarts/chart/gauge'
	       			],
	       			function(ec){
	       				var myChart = ec.init(document.getElementById('main'));
	       				//为echart加载数据
			           	myChart.setOption(option);
	       			}
	       		);
           }
       });
		//渲染整个文档，主要针对动态添加的div标签无法自动加载easyui样式
		$.parser.parse();
	}
	
	//加载配置图表页面
	function loadConfig(){
		$('#config').dialog({
		    title: '新增配置',
		    width: 800,
		    height: 300,
		    closed: false,
		    cache: false,
		    resizable:true,
		    href: root+'/chart/loadDialog',
		    modal: true
		});
	}
	//submit form
	function subDialog(){
		$('#form1').form('submit', {
			url:root+'/chartConfig/saveLineBarChartConfig',
		    success: function(data){
		    	$.messager.alert("操作提示", "配置保存成功！");
		        $("#tt").tree('reload');
		    }
		});
	}
	function closeDialog(){
		$('#config').dialog('close');
	}
	
	
	//添加一行
	function addRow(){
		var trid = $("#tab1 tr").length;
		var targetObj = $("<tr id=\""+trid+"\"><td>标题：</td><td><input class=\"easyui-textbox\" required=\"true\" type=\"text\" name=\"ytext\" style=\"width:180px;\"></td><td>SQL：</td><td><input class=\"easyui-textbox\" required=\"true\" type=\"text\" name=\"ysql\" style=\"width:180px;\"><a href=\"#\" class=\"easyui-linkbutton\" data-options=\"iconCls:'icon-remove'\" onclick=\"delRow("+trid+");\" style=\"width:55px;\">del</a></td></tr>").appendTo("#tab1");
		$.parser.parse(targetObj);
	}
	//删除最后一行
	function removeRow(){
		$("#tab1 tr:last").remove();
	}
	//删除指定行
	function delRow(trId){
		$("#"+trId+"").remove();
	}
	//修改图表配置
	function updateConfig(){
		var nodes = $('#tt').tree('getChecked');
		var num = nodes.length;
		if(num==0){
			 $.messager.alert("操作提示", "请选择一个图表");
			 return;
		}
		if(num>1){
			 $.messager.alert("操作提示", "只能选择一个图表");
			 return;
		}
		$('#config').dialog({
		    title: '修改配置',
		    width: 800,
		    height: 300,
		    closed: false,
		    cache: false,
		    resizable:true,
		    href: root+'/chartConfig/getLineBarChartConfig/'+nodes[0].id,
		    modal: true
		});
	}
	//删除图表配置
	function deleteConfig(){
		var nodes = $('#tt').tree('getChecked');
		var num = nodes.length;
		if(num==0){
			 $.messager.alert("操作提示", "请选择一个图表");
			 return;
		}
		var ids = new Array(nodes.length);
		for(var i=0;i<nodes.length;i++){
			ids[i] = nodes[i].id;
		}
		$.ajax({
            url:root+"/chart/deleteChartConfig",
            type:"post",
            data:{ids:JSON.stringify(ids)},
            dataType:'text',
            error: function(msg){
         	   $.messager.alert("error", "执行出错！");
            },
            success: function(text) { 
           	 $.messager.alert("操作提示", text);
           	 $("#tt").tree('reload');
            }
        });
	}
	//删除表单配置
	function deleteDataGridConfig(){
		var nodes = $('#tt1').tree('getChecked');
		var num = nodes.length;
		if(num==0){
			 $.messager.alert("操作提示", "请选择一个图表");
			 return;
		}
		var ids = new Array(nodes.length);
		for(var i=0;i<nodes.length;i++){
			ids[i] = nodes[i].id;
		}
		$.ajax({
            url:root+"/dataGrid/deleteGridConfig",
            type:"post",
            data:{ids:JSON.stringify(ids),tablename:'data_grid'},
            dataType:'text',
            error: function(msg){
         	   $.messager.alert("error", "执行出错！");
            },
            success: function(text) { 
           	 $.messager.alert("操作提示", text);
           	 $("#tt1").tree('reload');
            }
        });
	}
	
	//支持多表查询的datagrid------------------------------start
	//删除表单配置
	function deleteDataGridMoreConfig(){
		var nodes = $('#tt2').tree('getChecked');
		var num = nodes.length;
		if(num==0){
			 $.messager.alert("操作提示", "请选择一个图表");
			 return;
		}
		var ids = new Array(nodes.length);
		for(var i=0;i<nodes.length;i++){
			ids[i] = nodes[i].id;
		}
		$.ajax({
            url:root+"/dataGrid/deleteGridConfig",
            type:"post",
            data:{ids:JSON.stringify(ids),tablename:'data_grid_more'},
            dataType:'text',
            error: function(msg){
         	   $.messager.alert("error", "执行出错！");
            },
            success: function(text) { 
           	 $.messager.alert("操作提示", text);
           	 $("#tt2").tree('reload');
            }
        });
	}
	
	
	
	
	//支持多表查询的datagrid------------------------------start
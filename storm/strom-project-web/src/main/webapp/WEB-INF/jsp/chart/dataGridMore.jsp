<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>grid查询</title>
<%@include file="../crm/common.jsp" %>	
<script type="text/javascript">
//全局变量保存表的主键字段
var prikey = '';
var root = "${crm}";
$(function(){
	//列
	var columns = ${column};
	$("body").css({visibility:"visible"});
	$('#dg').datagrid({
		    url:'${crm}/dataGrid/loadDataMore',
		    columns:columns,
		    pageList: [10,20,50],
			pagination : true,
			rownumbers : true,
			singleSelect: false,
			onClickCell: onClickCell,
		    queryParams:{
    	        id:$("#id").val()
    		}
		});
	$("#searchBtn").click(function(){
		var inputSearch = $("#toolbar input");
		var selectSearch = $("#toolbar select");
		var parameters = '';
		for(var i=0;i<inputSearch.length;i++){
			if(i==0){
				parameters+=inputSearch[i].name+':'+inputSearch[i].value
			}else{
				parameters+=','+inputSearch[i].name+':'+inputSearch[i].value
			}
		}
		for(var i=0;i<selectSearch.length;i++){
			if(parameters==''){
				parameters+=selectSearch[i].name+':'+selectSearch[i].value
			}else{
				parameters+=','+selectSearch[i].name+':'+selectSearch[i].value
			}
		}
		 $('#dg').datagrid('load',{
			 id:$("#id").val(),
			 parameters:parameters
		}); 
	});
});
</script>
</head>
<body class="easyui-layout" style="visibility:hidden;">
	<input type="hidden" name="id" id="id" value="${id}"/>
	<div region="north" style="padding:5px;height:80px" border="true" title="数据明细页面">
		<div id="toolbar" style="padding:5px;">
             <c:forEach items="${columnames}" var="map">
				<c:choose>
				    <c:when test="${map['style']=='list'}">
				    	${map['title']}:
				    	<select name="${map['title']}">
				    			<option value="">-请选择-</option>
				    		<c:forEach items="${map['value']}" var="value">
					    		<option value="${value }">${value }</option>
				    		</c:forEach>
				    	</select>
				    </c:when>
				   <c:otherwise>  
	             		${map['title']}：<input   type="text" name="${map['title']}" style="width:100px;"/>
				   </c:otherwise>
			  </c:choose>
             </c:forEach>          
            <a href="#" class="easyui-linkbutton" iconCls="icon-search" id="searchBtn">开始查询</a>
        </div>
	</div>
	<div region="center" border="false">
    	<table id="dg"></table>
    </div>
    <div id="config">
			
	</div>
<body>
<script type="text/javascript">
$.extend($.fn.datagrid.methods, {
    editCell: function(jq,param){
        return jq.each(function(){
            var opts = $(this).datagrid('options');
            var fields = $(this).datagrid('getColumnFields',true).concat($(this).datagrid('getColumnFields'));
            for(var i=0; i<fields.length; i++){
                var col = $(this).datagrid('getColumnOption', fields[i]);
                col.editor1 = col.editor;
                if (fields[i] != param.field){
                    col.editor = null;
                }
            }
            $(this).datagrid('beginEdit', param.index);
            for(var i=0; i<fields.length; i++){
                var col = $(this).datagrid('getColumnOption', fields[i]);
                col.editor = col.editor1;
            }
        });
    }
});
</script>
</html>	
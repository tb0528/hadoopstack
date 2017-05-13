<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<c:set var="crm" value="${pageContext.request.contextPath}" />
<c:set var="fw" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!-- 导入jquery核心类库 -->
<script type="text/javascript" src="${crm}/js/jquery-1.8.3.js"></script>
<!-- 导入easyui类库 -->
<link id="easyuiTheme" rel="stylesheet" type="text/css" href="${crm}/js/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${crm}/js/easyui/themes/icon.css">
<link rel="stylesheet" type="text/css" href="${crm}/css/default.css">
<script type="text/javascript" src="${crm}/js/easyui/jquery.easyui.min.js"></script>	
<script src="${crm}/js/easyui/locale/easyui-lang-zh_CN.js" type="text/javascript"></script>
	
<link rel="stylesheet" type="text/css" href="${crm}/js/ztree/zTreeStyle.css" type="text/css">
<script src="${crm}/js/ztree/jquery.ztree.all-3.5.js" type="text/javascript"></script>

<script src="${crm}/js/My97DatePicker/WdatePicker.js" type="text/javascript"></script>

<script src="${crm}/js/util.js" type="text/javascript"></script>

<script src="${crm}/js/echarts/esl.js"></script>

<script src="${crm}/js/uploadify/jquery.uploadify.min.js" type="text/javascript"></script>

<!-- 导入chart和datagrid的js文件 -->
<script src="${crm}/js/chart/chart.js" type="text/javascript"></script>
<script src="${crm}/js/chart/datagrid.js" type="text/javascript"></script>
<!--

//-->
</script>

<link rel="stylesheet" type="text/css" href="${crm}/js/uploadify/uploadify.css">

<script type="text/javascript">
$(function() {
	var msg = '${OperationMsg}';
	if(msg){
		popMsg(msg);
	}
});
</script>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 导入jquery核心类库 -->
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.8.3.js"></script>
<link id="easyuiTheme" rel="stylesheet" type="text/css" href="${static_cms}/css/style.css">
<script type="text/javascript" src="${static_cms}/js/KinSlideshow.js"></script>	
<script type="text/javascript" src="${static_cms}/js/slide.js"></script> 
<script type="text/javascript" src="${static_cms}/js/jsAddress.js"></script>

<script type="text/javascript">
$(function() {
	var msg = '${OperationMsg}';
	if(msg){
		popMsg(msg);
	}
});
</script>
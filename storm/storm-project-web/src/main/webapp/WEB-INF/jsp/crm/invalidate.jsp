<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% 
	// 销毁session
	session.invalidate();

	// 重定向login.jsp
	response.sendRedirect(request.getContextPath() + "/login.jsp");
%>
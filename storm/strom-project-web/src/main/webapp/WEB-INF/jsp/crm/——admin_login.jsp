<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>登陆页面</title>
<script src="${pageContext.request.contextPath }/js/jquery-1.8.3.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/style.css" />
<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath }/css/style_grey.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/login/styles/clicki.web.css?V=20120501" media="screen" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/login/styles/clicki.loginandreg.css?V=20120501" media="screen" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/login/styles/clicki.webkitanimation.css?V=20120501" media="screen" />

<title>小小默CRM管理系统</title>
<!--[if lte IE 9 ]><link rel="stylesheet" href="${pageContext.request.contextPath }/login/styles/clicki.iehotfix.css?V=20120501" /><![endif]-->
<!--[if lte IE 9]>
<style>
body {background:#2f7fb2 url(${pageContext.request.contextPath }/login/styles/images/topbg_01.png?V=20120501) top center no-repeat;}
html{height:100%;overflow:hidden;background:#2f7fb2 url(${pageContext.request.contextPath }/login/styles/images/topbg_01.png?V=20120501) top center no-repeat;}
</style>
<![endif]-->
</head>
<body>

<div class="theCenterBox" style="">
  <div class="theLoginBox">
  
  <div class="loginTxt"><font color=#FF0000 >${message}</font></div>
    <div class="loginTxt">小小默CRM——管理后台登陆</div>
    <div class="loginTxt">【本系统目前只支持chrome、firefox浏览器】</div>
    <div class="theLoginArea" id="loginBox">
      <form id="leftForm" action="${pageContext.request.contextPath }/login/adminlogining" method="post">
        <p style="position: relative;">
          <label for="LoginForm_email">账号：</label>
          <input placeholder="请输入您的手机号码" name="username" type="text"/>
          <span>请输入您的账号</span> </p>
        <p style="position: relative;">
          <label for="LoginForm_password">密码：</label>
          <input name="password" type="password"/>
          <span>请输入您的密码</span> </p>
        <div class="loginSubmitBnt fixPadding">
          <div>
            <div class="login_submit">
              <input class="theSubmitButton" value="" type="submit" />
              <!-- 
              <span style="float:clear"><a class="openid-connect" style="border-bottom:0px;" href="javascript:void(0);" ref="https://graph.qq.com/oauth2.0/authorize?client_id=101027227&amp;redirect_uri=http%3A%2F%2Fwww.yunpian.com%2Fcomponent%2Fopenidlogin%3Ftype%3DQQ&amp;response_type=code&amp;state=qqlogin&amp;scope=get_user_info"><img border="0" src="${pageContext.request.contextPath }/images/qqlogin-btn.png"></a></span>
               -->
            </div>
          </div>
        </div>
      </form>
    </div>
  </div>
</div>

</body>
</html>

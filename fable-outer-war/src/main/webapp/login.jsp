<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<!-- fable logo -->
	<link rel="icon" href="${pageContext.request.contextPath}/style/images/fable_logo.gif" type="image/x-icon" /> 
	<link rel="shortcut icon" href="${pageContext.request.contextPath}/style/images/fable_logo.gif" type="image/x-icon" />
	
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>登录</title>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/style/css/fableframe/global.css" type="text/css" />
	<link rel="stylesheet" href="${pageContext.request.contextPath}/style/css/fableframe/login.css" type="text/css" />
	<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery-1.10.2.min.js"></script>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/default/easyui.css" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/icon.css" />
	<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.easyui.min.js"></script>
	<!--jquery.cookie  -->
	<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.cookie.js"></script>
</head>

<body>
<!-- begin #main -->
<div id="main" > 
  
	<!-- begin #header -->
	<div id="header">
		<!-- begin #logo -->
        <img src="${pageContext.request.contextPath}/style/images/login/logo.jpg"  />
        <!-- end #logo -->
	</div>
	<!-- begin #content -->
	<div id="content">
		
        <!-- begin #loginArea -->
        <div id="loginArea">
            
            <!-- begin #inputArea -->
            <div id="inputArea">
                <!-- begin #welcomeHere -->
                <div id="welcomeHere">您好，欢迎登录！</div>
                <!-- end #welcomeHere -->
                   
                <!-- begin #myForm -->
                <form id="loginForm" name="loginForm" action="fable_security" method="post">
                
					<!-- begin #reallyHere -->
                    <div id="reallyHere">
                    	<p class="tips">${sessionScope['SPRING_SECURITY_LAST_EX_MSG']}</p> 
                        <ul>
                            <li class="keyWords"><span>用户名：</span><input type="text" class="inputs" id="uname" name="username" value="${sessionScope['SPRING_SECURITY_LAST_USERNAME']}" />
							</li>
							<li class="keyWords"><span>密&nbsp;&nbsp;码：</span><input class="inputs" type="password" id="pwd" name="password" />
							</li>
                           <%--  <li class="keyWords"><span>验证码：</span><input type="text"  class="inputsShort"  id="checkCode" name="checkCode" maxlength="40"/>
                            	<img id="code" onclick="changeCode()" border=0 src="${pageContext.request.contextPath}/commons/checkCode.jsp">
                            	<a href="javascript:changeCode()" class="change">看不清，换一张</a>
                            </li> --%>
                           <!--  <li><input type="checkbox" class="checks" id="checkPassword"/> <label for="checkPassword">记住密码</label> <a href="#" >证书登录</a>
                            </li> -->
                            <li class="buttons">
								<input type="submit" onmouseover="doChange(this);" onmouseout="doBack(this);" value="登录" /> 
								<input type="reset" onmouseover="doChange(this);" onmouseout="doBack(this);" value="重置" />
							</li>                                                                                                                  
                        </ul>
                        
                    </div>
                    <!-- end #reallyHere -->
                    
                </form>
                 <!-- end #myForm -->
                
            </div>      
            <!-- end #inputArea -->
        </div>      
        <!-- end #loginArea -->
        
              
	</div>
	<!-- end #content -->  
  
  
	<!-- begin #footer -->
	<div id="footer">
		版权所有&nbsp;&copy;&nbsp;&nbsp;2013-2014&nbsp;&nbsp;&nbsp;江苏飞搏软件技术有限公司       
	</div>
	<!-- end #footer -->  
  
</div>
<!-- end #main -->
</body>
<script type="text/javascript">
	function doChange(obj) {
		obj.style.backgroundColor = "";
		obj.style.backgroundColor = "#87CEEB";
	}
	function doBack(obj) {
		obj.style.backgroundColor = "";
		obj.style.backgroundColor = "#707885";
	}
	$(function() {
		//表示存在COOKIE中的数据使用原生数据,不进行加密,这样存放的数据会更小一点,且后台解析时不会报错
		$.cookie.raw = true;
		$.cookie.json = true;
		$.removeCookie('selectMenu', { path: '${pageContext.request.contextPath}' });
		$.removeCookie('systemMenus', {path : '${pageContext.request.contextPath}'});
		$('#uname').validatebox({
			required : true,
			missingMessage : '请输入用户名!'
		});

		$('#pwd').validatebox({
			required : true,
			missingMessage : '请输入密码!'
		});
		//授权验证
		if ("${sessionScope.STRING_SECURITY_SYSTEM_AUTH}" == "false") {
			$.messager.defaults={ok:"确定",cancel:"取消"};
			$.messager.confirm('授权验证','系统尚未授权或者授权已经过期，是否为您跳转到系统授权页面？',function(r) {
				if (r) {
					location.href = "${pageContext.request.contextPath}/sysauth/maintain";
				}
			});
		}
	});

	function submitForm() {
		$('#pwd').validatebox('validate');
		if ($('#uname').validatebox('isValid')) {
			if ($('#pwd').validatebox('isValid')) {
				$('#loginForm').submit();
			} else {
				$('#pwd').validatebox("validate");
			}
		} else {
			$('#uname').validatebox("validate");
		}
	}
	
	function changeCode()
	{
		now = new Date(); 
		$('#code').attr("src","${pageContext.request.contextPath}/commons/checkCode.jsp?code="+now.getTime());
	} 
</script>
</html>
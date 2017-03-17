<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp" %>
<%
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<!-- fable logo -->
	<link rel="icon" href="${pageContext.request.contextPath}/style/images/fable_logo.gif" type="image/x-icon" /> 
	<link rel="shortcut icon" href="${pageContext.request.contextPath}/style/images/fable_logo.gif" type="image/x-icon" />
	<%@ include file="/commons/meta.jsp" %>
	<base href="<%=basePath%>">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/style/css/fableframe/global.css" type="text/css" />
	<link rel="stylesheet" href="${pageContext.request.contextPath}/style/css/fableframe/main.css" type="text/css" />
	<title><decorator:title default="数据交换平台" /></title>
        <decorator:head />

</head>

<body class="easyui-layout" >
	<%--北 --%>
	<div data-options="region:'north',border:false">
		<div class="header">
			<div class="userMsgAll">
				<div class="logo"></div>
				<div class="userMsg">
					<div class="userMsgSec" style="margin-top:35px;">
						<ul>
							<li style="margin-right:10px;"><a href="${ctx}/fableLogout" title="登陆" class="easyui-tooltip"><img src="${pageContext.request.contextPath}/style/images/main/home.gif" align="absmiddle" />返回登陆</a></li>
						</ul>
					</div>
				</div>
			</div>
		</div>
		<!-- end .header -->
	</div>

	<%--中 --%>
	<div data-options="region:'center'" style="padding:0px;">
		<decorator:body />
	</div>
</body>
</html>

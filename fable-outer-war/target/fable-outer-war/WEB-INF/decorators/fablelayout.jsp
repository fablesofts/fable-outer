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
<body id="mian" >
     


  <!-- begin .container -->
<div class="container">
       
        <!-- begin .header -->
        <div class="header">
        	
            <!-- begin .userMsg -->
        	<div class="userMsgAll">	
            	
                <!-- begin .logo -->
            	<div class="logo"></div>
                <!-- end .logo -->
                
                <!-- begin .userMsg -->
                <div class="userMsg">

                		<div class="userMsgFir"><a href="javascript:void(0)">欢迎您: ${sessionScope.SESSION_DATA.userName}</a>&nbsp;&nbsp;&nbsp;<span id="dateArea"></span>
                        	
                        </div>
                		<div class="userMsgSec">
                        	<ul>
                        	  <li><a href="javascript:void(0)" id="modifyPwd_tooltip"><img src="${pageContext.request.contextPath}/style/images/main/img_2.gif"  align="absmiddle" />&nbsp;修改密码</a></li>
                              <li><a href="javascript:void(0)" title="点击查看个人信息" class="easyui-tooltip"><img src="${pageContext.request.contextPath}/style/images/main/img_1.gif"  align="absmiddle" />&nbsp;查看资料</a></li>
                              <li><a href="${ctx}/fableLogout" title="注销用户" class="easyui-tooltip"><img src="${pageContext.request.contextPath}/style/images/main/exit.gif"  align="absmiddle" />&nbsp;退出</a></li>
                            </ul>
                         </div>
                </div>
                <!-- end .userMsg -->
            </div>
                
        </div>
        <!-- end .header -->
       
        <!-- begin .main --> 
        <div class="main">
        <!-- begin .sidebar1 --> 
        <div class="sidebar1" id="frmTitle">
    
              <ul class="nav">
                    	
                    <li class="firLi"  ><a href="javascript:void(0)"><img src="${pageContext.request.contextPath}/style/images/main/icon3.gif"   align="middle" />&nbsp;&nbsp;系统服务</a></li>
                    	<ul  class="hdUl" >
							<li class="hdLi"><a href="${ctx}/userInfo/maintain">用户管理</a></li>
						</ul>
						<ul  class="hdUl" >
							<li class="hdLi"><a href="${ctx}/roleInfo/maintain">角色管理</a></li>
						</ul>
						<ul  class="hdUl" >
							<li class="hdLi"><a href="${ctx}/menuInfo/maintain">菜单管理</a></li>
						</ul>
                    <li class="firLi"  ><a href="javascript:void(0)"><img src="${pageContext.request.contextPath}/style/images/main/icon12.gif"   align="middle" />&nbsp;&nbsp;帮助</a></li>                      	
                      	<ul  class="hdUl" >
                        	<li class="hdLi"><a href="${ctx}/interest/maintain">DEMO示例</a></li>                                                                                    
                        </ul>                          
              </ul>
              
         </div>
         <!-- end .sidebar1 -->
          
			<!-- begin .picBox -->             
           <div class="picBox"  id="switchPoint" ></div>
           <!-- end .picBox -->   
          
        <!-- begin .content -->   
        <div class="content">
                <decorator:body/>
         </div>
        <!-- end .content -->

        </div>
        <!-- end .main --> 
        
         <!-- begin .footer -->
        <div class="footer">
        	版权所有&nbsp;&copy;&nbsp;&nbsp;2013-2014&nbsp;&nbsp;&nbsp;江苏飞搏软件技术有限公司
        </div>
         <!-- end .footer -->
    </div>
    <!-- end .container -->
    
    
</body>
<script>
$(function(){

	$('#modifyPwd_tooltip').tooltip({
			content : $('<div></div>'),
			showEvent : 'click',
			onUpdate : function(content) {
				content.panel({
					width : 240,
					height : 180,
					border : false,
					title : '修改个人密码',
					href : 'userInfo/href/updatepassword'
				});
			},
			onShow : function() {
				var t = $(this);
				t.tooltip('tip').unbind().bind('mouseenter', function() {
					t.tooltip('show');
				}).bind('mouseleave', function() {
					t.tooltip('hide');
					$('#modifyPwdForm').form('clear');
					$('#usernameTooltip').val("${sessionScope.SESSION_DATA.userName}");
				});
			},
			onPosition : function() {
				$('#modifyPwdForm').form('clear');
				$('#usernameTooltip').val("${sessionScope.SESSION_DATA.userName}");
			}
		});
		/*
		 *左侧导航两种手风琴效果
		 */

		//点击效果
		/* $(".nav .firLi").click(function(){
			$(this).next(".hdUl").slideToggle(300).siblings(".hdUl").slideUp("slow");
		});	 */
		//滑动效果
		/*
		$(".nav .firLi").mouseover(function(){
			$(this).next(".hdUl").slideDown(500).siblings(".hdUl").slideUp("slow");
		});
		 */

		//左侧导航菜单选中效果
		$(".hdUl a ").click(
				function() {
					//移除其他选中状态
					$(".hdUl a").removeClass("visited_left").css(
							"text-decoration", "none");
					//改变选中后状态
					$(this).addClass("visited_left");
					//链接地址
					//alert($(this).attr("href"));
					//菜单内容
					//alert($(this).html());
				});

		//折叠JS begin
		/*  	var status = 1;
		 $("#switchPoint").click(function(){
		 if (1 == window.status){
		 window.status = 0;
		 $("#switchPoint").css("background-image","url(${pageContext.request.contextPath}/style/images/main/left.gif)");
		 //$("#frmTitle").css("display","none");
		 $("#frmTitle").animate({width:'hide'},"slow");
		
		 }else{
		 window.status = 1;
		 $("#switchPoint").css("background-image","url(${pageContext.request.contextPath}/style/images/main/right.gif)");
		 //$("#frmTitle").css("display","block");
		 $("#frmTitle").animate({width:'show'},"slow");
		 }
		 }); */

	});
</script>
</html>
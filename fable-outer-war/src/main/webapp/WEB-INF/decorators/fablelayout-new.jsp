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
<%--load --%>
</head>

<body class="easyui-layout" >
	<div id='loading' style="position:absolute;z-index:1000;top:80px;left:180px;width:100%;height:100%;
		background:#fdfdfd;text-align:center;padding-top: 20%;">
		<image src='style/images/loading.gif'/><h3>页面正在加载中......</h3>
	</div>

	<%--修改密码窗体sysUpdatePasswordWindow --%>
	<div id="sysUpdatePasswordWindow" style="padding: 15px;overflow: visible;display: none;" >
		<form id="sysUpdatePasswordForm">
			<table>
				<tr>
					<td>用户名：</td>
					<td>${sessionScope.SESSION_DATA.userName}</td>
				</tr>
				<tr>
					<td>旧密码：</td>
					<td><input id="sysOldPassword" name="sysOldPassword" class="easyui-validatebox" data-options="required:true" type="password">&nbsp;<font color="red">*</font></td>
				</tr>
				<tr>
					<td>新密码：</td>
					<td><input name="sysNewPassword" id="sysNewPassword" class="easyui-validatebox" data-options="required:true" type="password">&nbsp;<font color="red">*</font></font></td>
				</tr>
				<tr>
					<td>密码确认：</td>
					<td><input id="sysConfirmPassword" class="easyui-validatebox" data-options="required:true" type="password" validType="equals['#sysNewPassword']">&nbsp;<font color="red">*</font></td>
				</tr>
			</table>
		</form>
		<div id="updateLoginButton" style="text-align: center; padding: 5px;">
			<a href="javascript:void(0)" onclick="SysUpdatePassword.submit()" id="updateLoginButton-save"
				icon="oper_but_save">保存</a> <a href="javascript:void(0)"
				onclick="SysUpdatePassword.cancel()" id="updateLoginButton-cancel" icon="oper_but_cancel">取消</a>
		</div>
	</div>

	<%--北 --%>
	<div data-options="region:'north',border:false">
		<div class="header">
			<div class="userMsgAll">
				<div class="logo"></div>
				<div class="userMsg">
					<div class="userMsgFir">
						<a href="javascript:void(0)">欢迎您:${sessionScope.SESSION_DATA.userName}</a>&nbsp;&nbsp;&nbsp;<span id="dateArea"></span>
					</div>
					<div class="userMsgSec">
						<ul>
							<li><a href="javascript:void(0)" onclick="SysUpdatePassword.open()" id="modifyPwd_tooltip"><img src="${pageContext.request.contextPath}/style/images/main/img_2.gif" align="absmiddle" />&nbsp;修改密码</a></li>
							<li><a href="javascript:void(0)" id="showSystemInfo"><img src="${pageContext.request.contextPath}/style/images/main/img_1.gif" align="absmiddle" />&nbsp;查看资料</a></li>
							<li><a href="${ctx}/fableLogout" title="注销用户" class="easyui-tooltip"><img src="${pageContext.request.contextPath}/style/images/main/exit.gif" align="absmiddle" />&nbsp;退出</a></li>
						</ul>
					</div>
				</div>
			</div>
		</div>
		<!-- end .header -->
	</div>
	<%--南 --%>
	<div data-options="region:'south',border:false,split:false" style="height: 36px;">
		<div class="footer">
			版权所有&nbsp;&copy;&nbsp;&nbsp;2013-2014&nbsp;&nbsp;&nbsp;江苏飞搏软件技术有限公司 
			<!-- <img src="${pageContext.request.contextPath}/sysauth/viewLicence">-->
		</div>
	</div>
	<%--西--%>
	<div data-options="region:'west',split:true,iconCls : 'menu_nav'" title="导航菜单" style="width: 180px;">
		<div id="accordionMenus"></div>
	</div>
	<%--中 --%>
	<div data-options="region:'center',split:false" >
		<div class="easyui-tabs" data-options="border:false,fit:true" id="tabsPannel">
			<div title="<decorator:title default="飞搏数据交换平台"></decorator:title>" data-options="closable:false,iconCls:'icon-tabs'">
				<decorator:body />
			</div>
		</div>
	</div>
	<script type="text/javascript">
	<%--菜单显示 --%>
		function closes(){  
			$('#loading').fadeOut("normal",function(){  
				$(this).remove();  
			});  
		}  
		var delayTime;  
		$.parser.onComplete = function(){
			if(delayTime) clearTimeout(delayTime);  
			delayTime = setTimeout(closes, 1000);  
		};

		function MenuViews(){};
		MenuViews.createNavgator=function(){
		     $('#accordionMenus').accordion({
		                animate : false,
		                fit : true,
		                border : false,
		                onSelect : function(title,index) {
		                        //把选择展示的菜单的名称放入COOKIE
		                        $.cookie('selectMenu', title, {path : '${pageContext.request.contextPath}'});
		                }
		        });
		};
        MenuViews.initCookies=function(){
        	var data=null;
        	$.get('${pageContext.request.contextPath}/system/findMenuInfoListByRoleId?roleId=${sessionScope.SESSION_DATA.roleId}&time='+new Date().getMilliseconds(),null, function(data) {
                        //把系统菜单的相关信息存入cookie
                        data=eval('(' + data+')');
                        $.cookie('systemMenus',data, {path : '${pageContext.request.contextPath}'});
                        MenuViews.buildMenus(data);
        		});
           return data;
        };

        MenuViews.buildMenus=function(dataArr){
            //创建二级菜单
            function createSysMenus(menuArray){
            	menuArray=menuArray||[]
            	for(var i=0;i<menuArray.length;i++){
            		var parent=menuArray[i];
            		if(parent&&parent.children){
            			for(var j=0;j<parent.children.length;j++){
            				var menu=parent.children[j];
            				 $('#accordionMenus').accordion('add',{
                                 iconCls : selectMenuIcon(menu.iconCls),
                                 title : menu.text,
                                 content : createChildrenMenus(menu.children),
                                 selected : isMenuSelected(menu.text)
                         });
            			}
            		}
            	}
            }

            //构造三级菜单
            function createChildrenMenus(childrenArray) {
                    var menulist = '<ul>';
                    for (var i = 0; i < childrenArray.length; i++) {
                            var url = childrenArray[i].url + '';
                            menulist += '<li><a href="javascript:void(0)"  onclick="MenuViews.f5(\''
                                            + url+ '\',\''+childrenArray[i].iconCls
                                            + '\')" class="easyui-linkbutton" data-options="plain:true,iconCls:\''+selectMenuIcon(childrenArray[i].iconCls)+'\'">'
                                            + childrenArray[i].text + '</a></li>';
                    }
                    menulist += '</ul>';
                    return menulist;
            }

            //判断当前的菜单是否是展开的
            function isMenuSelected(menuTitle){
                    //如果两者都有值
                    if($.cookie('selectMenu') && menuTitle){
                            return menuTitle.getValue()===$.cookie('selectMenu').getValue();
                    }else{
                            return false;
                    }
            }

            //判断当前的菜单是否是展开的
            function selectMenuIcon(menuIcon){
                    //设置默认的ICON,注意这里的-1和数据表中的默认值对应
                    if(menuIcon && '-1' == menuIcon){
                            menuIcon = 'icon-ok';
                    }
                    return menuIcon;
            }
            createSysMenus(dataArr);
    };
    //给String的原型添加一个去掉双引号的方法
    String.prototype.getValue=function(){
              return this.replace(/\"/g,'');
          };
    MenuViews.f5=function(url,icon){
    		//alert('点击前的cookie'+$.cookie('iconCls'));
    		$.removeCookie('iconCls',{ path: '${pageContext.request.contextPath}' });
    		$.cookie('iconCls',icon,{ path: '${pageContext.request.contextPath}' });
    		//alert('点击后的cookie'+$.cookie('iconCls'));
            location.href = '${pageContext.request.contextPath}' + url;
    };
    MenuViews.init=function(){
    	//表示存在COOKIE中的数据使用原生数据,不进行加密,这样存放的数据会小很多(最大4K限制),但后台解析时,不支持中文报错(不影响正常使用)
	    // $.cookie.raw = true;
      	$.cookie.json = true;
    	var data=$.cookie('systemMenus')||MenuViews.initCookies();
      	MenuViews.createNavgator();
    	MenuViews.buildMenus(data);
    };
<%--修改密码静态类用来控制显示修改密码窗体 --%>
	function SysUpdatePassword() {
	}
	SysUpdatePassword.submit=function(){
		if ($('#sysNewPassword').val() !== $('#sysConfirmPassword').val()) {
			$.messager.show({
				title : '提示',
				msg : '两次输入的密码不匹配',
				showType : 'slide',
				timeout : 2000
			});
			return;
		}
		$.ajax({type : "POST",
				url : '${pageContext.request.contextPath}/system/updatePassword',
				data : $('#sysUpdatePasswordForm').serializeArray(),
				async : false,
				dataType : 'json',
				success : function(jsonMap) {
								if (jsonMap.flag) {
									$('#sysUpdatePasswordWindow').dialog('close');
								}
								$(":password").val('');
								$.messager.show({
									title : '提示',
									msg : jsonMap.msg,
									showType : 'slide',
									timeout : 2000
								});
							}
		});
		$('#sysUpdatePasswordWindow').dialog('close');
	};
	SysUpdatePassword.cancel=function(){
		$('#sysUpdatePasswordWindow').dialog('close');
	};
	SysUpdatePassword.init = function() {
		$('#updateLoginButton-save,#updateLoginButton-cancel').linkbutton();
		$.extend($.fn.validatebox.defaults.rules, {
			equals : {
				validator : function(value, param) {
					return value == $(param[0]).val();
				},
				message : '密码不匹配.'
			}
		});
	}; 
	SysUpdatePassword.open = function() {
		$('#sysUpdatePasswordWindow').css("display","block");
		$('#sysUpdatePasswordWindow').dialog({
			title : '修改密码',
			width : 420,
			height : 220,
			modal : true,
			closed : false,
			draggable : false,
			resizable : false,
			buttons : '#updateLoginButton'
		});
		$(":password").val('');
	};
	
    <%--SystemInfo静态类用来控制显示详细信息窗口的操作 --%>
	function SystemInfo() {
	}
	SystemInfo.init = function() {
		$('#showSystemInfo').tooltip(
						{
							position : 'bottom',
							content : function() {
								var div = $('<div></div>');
								var ul = $('<ul></ul>');
								ul.append($('<li> 用户编号：${sessionScope.SESSION_DATA.userId} </li>'));
								ul.append($('<li> 用户名：${sessionScope.SESSION_DATA.userName} </li>'));
								ul.append($('<li> 角色名：${sessionScope.SESSION_DATA.roleName} </li>'));
								ul.appendTo(div);
								return div;
							}
						});
	};
		window.setTimeout(function(){
	    	MenuViews.init();
    	},1);
		window.setTimeout(function(){
	        SysUpdatePassword.init();
	    },500);
        window.setTimeout(function(){
         	SystemInfo.init();
        },650);
        window.setTimeout(function(){
        	$('.header').parent().css('overflow','hidden');
            $("div[class='panel-icon icon-save']").hide();
        	$('.icon-tabs').addClass($.cookie('iconCls'));
        },600);
</script>
</body>

</html>

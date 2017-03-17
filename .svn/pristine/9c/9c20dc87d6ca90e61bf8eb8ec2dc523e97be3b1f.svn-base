<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<%@ include file="/commons/meta.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>飞搏数据交换平台</title>
<!-- fable logo -->
<link rel="icon"
	href="${pageContext.request.contextPath}/style/images/fable_logo.gif"
	type="image/x-icon" />
<link rel="shortcut icon"
	href="${pageContext.request.contextPath}/style/images/fable_logo.gif"
	type="image/x-icon" />
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/style/css/fableframe/global.css"
	type="text/css" />
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/style/css/fableframe/main.css"
	type="text/css" />
<%--load --%>
<script type="text/javascript">
	$(function() {
		SysUpdatePassword.init();
		SystemInfo.init();
		$('.header').parent().css('overflow','hidden');
	});
</script>
</head>

<body class="easyui-layout" >

	<%--修改密码窗体sysUpdatePasswordWindow --%>
	<div id="sysUpdatePasswordWindow" class="easyui-dialog" title="修改密码"
		style="width: 300px; height: 200px; padding: 15px">
		<form id="sysUpdatePasswordForm">
			<table>
				<tr>
					<td>旧密码：</td>
					<td><input id="sysOldPassword" name="sysOldPassword"
						class="easyui-validatebox" data-options="required:true"
						type="password"></td>
				</tr>
				<tr>
					<td>新密码：</td>
					<td><input name="sysNewPassword" id="sysNewPassword"
						class="easyui-validatebox" data-options="required:true"
						type="password"></font></td>
				</tr>
				<tr>
					<td>密码确认：</td>
					<td><input id="sysConfirmPassword" class="easyui-validatebox"
						data-options="required:true" type="password"
						validType="equals['#sysNewPassword']"></td>
				</tr>
			</table>
		</form>
	</div>
	<%--北 --%>
	<div data-options="region:'north'">
		<div class="header">
			<div class="userMsgAll">
				<div class="logo"></div>
				<div class="userMsg">
					<div class="userMsgFir">
						<a href="javascript:void(0)">欢迎您:
							${sessionScope.SESSION_DATA.userName}</a>&nbsp;&nbsp;&nbsp;<span
							id="dateArea"></span>
					</div>
					<div class="userMsgSec">
						<ul>
							<li><a href="javascript:void(0)"
								onclick="SysUpdatePassword.open()" id="modifyPwd_tooltip"><img
									src="${pageContext.request.contextPath}/style/images/main/img_2.gif"
									align="absmiddle" />&nbsp;修改密码</a></li>
							<li><a href="javascript:void(0)" id="showSystemInfo"><img
									src="${pageContext.request.contextPath}/style/images/main/img_1.gif"
									align="absmiddle" />&nbsp;查看资料</a></li>
							<li><a href="${ctx}/fableLogout" title="注销用户"
								class="easyui-tooltip"><img
									src="${pageContext.request.contextPath}/style/images/main/exit.gif"
									align="absmiddle" />&nbsp;退出</a></li>
						</ul>
					</div>
				</div>

			</div>

		</div>
		<!-- end .header -->
	</div>
	<%--南 --%>
	<div data-options="region:'south'" style="height: 50px;">
		<div class="footer">
			版权所有&nbsp;&copy;&nbsp;&nbsp;2013-2014&nbsp;&nbsp;&nbsp;江苏飞搏软件技术有限公司</div>
	</div>
	<%--西--%>
	<div data-options="region:'west',split:true" title="导航菜单"
		style="width: 150px;">
		<div id="accordionMenus"></div>
	</div>
	<%--中 --%>
	<div data-options="region:'center'">
			<iframe id='frm' width="100%" height="100%" frameborder="0" >
			</iframe>
	</div>
</body>
<%--菜单显示 --%>
<script>
	$(function() {
		//定义手风琴样式菜单
		$('#accordionMenus').accordion({
			animate : false,
			fit : true,
			border : false
		});
		//初始化菜单
		$.get('${pageContext.request.contextPath}/system/findMenuInfoListByRoleId',
			function(data, status) {
				eval('menuArray=' + data);
				$(menuArray).each(function(index, parent) {
					if (typeof (parent.children) == "object") {
						$(parent.children).each(function(i,menu) {
							//alert('TREE:' + menu.text); 
						$('#accordionMenus').accordion('add',{
							iconCls : 'icon-sum',
							title : menu.text,
							content : createChildrenMenus(menu.children),
							selected : false,
							onSelect : function(title,index) {
											alert(title+index); // alert node text property when clicked
										}
							});
						});
					} else {
						alert('error!');
					}
				});
		});
	});
	function createChildrenMenus(childrenArray) {
		var menulist = '<ul>';
		for (var i = 0; i < childrenArray.length; i++) {
			var url = childrenArray[i].url + '';
			menulist += '<li><a href="#"  onclick="addTab(\''
					+ url
					+ '\',\''
					+ childrenArray[i].text
					+ '\')" class="easyui-linkbutton" data-options="plain:true,iconCls:\'icon-ok\'">'
					+ childrenArray[i].text + '</a></li>';
		}
		menulist += '</ul>';
		return menulist;
	}
	function addTab(url, tabTitle) {
		url = '${pageContext.request.contextPath}' + url;
		$('#frm').attr('src',url);
	}
</script>

<%--修改密码静态类用来控制显示修改密码窗体 --%>
<script type="text/javascript">
	function SysUpdatePassword() {
	}
	SysUpdatePassword.init = function() {
		$(":password").val('');
		$('#sysUpdatePasswordWindow').dialog(
				{
					iconCls : 'icon-save',
					buttons : [{text : '确认',
								iconCls : 'icon-ok',	
								handler : function() {
									if ($('#sysNewPassword').val() !== $('#sysConfirmPassword').val()) {
												alert('两次输入的密码不匹配');
												return;
									}
									$.ajax({type : "POST",
											url : '${pageContext.request.contextPath}/system/updatePassword',
											data : $('#sysUpdatePasswordForm').serializeArray(),
											async : false,
											dataType : 'json',
											success : function(jsonMap) {
															if (jsonMap.flag) {
																$(sysUpdatePasswordWindow).dialog('close');
															}
															alert(jsonMap.msg);
															$(":password").val('');
														}
									});
									$('#sysUpdatePasswordWindow').dialog('close');
								}},
								{text : '取消',
								iconCls : 'icon-cancel',
								handler : function() {
											$(":password").val('');
											$('#sysUpdatePasswordWindow')
													.dialog('close');
										}
					} ]
		});
		$('#sysUpdatePasswordWindow').dialog('close');
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
		$('#sysUpdatePasswordWindow').dialog('open');
	};
	SysUpdatePassword.close = function() {
		$('#sysUpdatePasswordWindow').dialog('close');
	};
</script>

<%--SystemInfo静态类用来控制显示详细信息窗口的操作 --%>
<script type="text/javascript">
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
</script>
</html>

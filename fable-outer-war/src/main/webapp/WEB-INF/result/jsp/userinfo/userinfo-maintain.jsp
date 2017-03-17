<%@ page language="java" pageEncoding="utf-8"%>
<html>
<head>
<title>用户管理</title>
<!-- 重写easyui validatebox样式 -->
<style type="text/css">
.white {
	background: #ffffff;
	border: 1px solid #7EBDD9;
	min-height: 20px;
}

.validatebox-invalid {
	background: #ffffff;
	border: 1px solid #7EBDD9;
	min-height: 20px;
}
</style>
<!-- 验证脚本 -->
<script type="text/javascript">
	$(function() {
		$('#username,#editusername').validatebox({
			required : true,
			missingMessage : '请输入用户名!'
		});
		$('#realname,#editrealname').validatebox({
			required : true,
			missingMessage : '请输入真实姓名!'
		});
		$('#renewpwd').validatebox({
			required : true,
			missingMessage : '请输入确认密码!'
		});

		$('#password,#newpwd').validatebox({
			required : true,
			missingMessage : '请输入密码!'
		});
		$('#renewpwdforup').validatebox({
			required : true,
			missingMessage : '请输入确认密码!'
		});

		//初始化用户信息列表数据
		grid = $('#user-table')
				.datagrid(
						{
							url : '${pageContext.request.contextPath}/userInfo/findUserInfoList',
							//title : '用户信息列表',
							border : false,
							singleSelect : true,
							fitColumns : true,
							autoRowHeight : true,
							loadMsg : '用户资料正在加载,请稍后 !',
							striped : true,
							nowrap : false,
							pagination : true,
							rownumbers : true,
							fit : true,
							resizeHandle : 'both',
							pagePosition : 'bottom',
							scrollbarSize : 10,
							onRowContextMenu : onRowContextMenu,
							frozenColumns : [ [ {
								field : 'id',
								hidden : true
							}, ] ],
							columns : [ [ {
								field : 'username',
								title : '用户名',
								width : 80,
								align : 'center'
							}, {
								field : 'description',
								title : '描述',
								width : 80,
								align : 'center'
							}, {
								field : 'roles',
								title : '权限',
								width : 80,
								align : 'center',
								formatter : function(value, rec) {
									return value[0].roleName;
								}
							} ] ],
							toolbar : [ {
								text : '新增',
								iconCls : 'oper_add',
								handler : add
							}, '-', {
								text : '编辑',
								iconCls : 'icon-edit',
								handler : edit
							}, '-', {
								text : '删除',
								iconCls : 'oper_remove',
								handler : del
							}, '-', {
								text : '修改密码',
								iconCls : 'oper_edit_password',
								handler : updatepd
							} ]
						});
		//初始化按钮
		$('#btn-save,#btn-cancel').linkbutton();
		
		//定义修改密码对话框
		updatepdWin = $('#updatepd-window').dialog({
			title : '修改密码',
			width : 420,
			height : 200,
			modal : true,
			closed : true,
			draggable : false,
			buttons : '#updatepd-buttons'
		});
		
		//初始化form
		updatepdForm = updatepdWin.find('form');

		//设置分页控件  
		var p = $('#user-table').datagrid('getPager');
		$(p).pagination({
			pageSize : 10,//每页显示的记录条数，默认为10  
			pageList : [ 5, 10, 15 ],//可以设置每页记录条数的列表  
			beforePageText : '第',//页数文本框前显示的汉字  
			afterPageText : '页    共 {pages} 页',
			displayMsg : '当前显示 {from} - {to} 条记录   共 {total} 条记录'
		});
	});
	//设置需要的全局变量
	var grid;
	var newUserWin;//新增用户窗口
	var editUserWin;//编辑用户窗口
	var updatepdWin;//修改密码窗口
	var updatepdForm;//修改密码表单
	//显示用户详细信息的方法
	function view() {
		var row = grid.datagrid('getSelected');
		if (row != null) {
			$('#detail-window').css("display","block");
			//定义显示用户详细信息对话框
			$('#detail-window').dialog({
				title : '用户详细信息',
				width : 420,
				height : 180,
				modal : true,
				closed : false,
				draggable : false
			});
			//回显选中用户的详细信息数据
			$('#detail-window').find('form').form('load', {
				username : row.username,
				realname : row.realname,
				description : row.description
			});
			$('#detailqx').val(row.roles[0].roleName);
		} else {
			$.messager.show({
				title : '警告',
				msg : '请先选择用户。'
			});
		}
	}

	//打开新增窗口 清空form内容 设置form提交的url
	function add() {
		$('#newuser-window').css("display","block");
		//定义新增用户对话框
		newUserWin = $('#newuser-window').dialog({
			title : '新增用户',
			width : 420,
			height : 320,
			modal : true,
			closed : false,
			draggable : false,
			buttons : '#newuser-buttons'
		});
		$('#newuser-window').find('form').form('clear');
		//默认选择下拉框第一个
		var data = $('#qx').combobox('getData');
		if (data.length > 0) {
			$('#qx').combobox('setValue', data[0].id);
		}
	}
	//提交新增用户的方法
	function save() {
		$('#newuser-window').find('form').form('submit', {
			url : '${pageContext.request.contextPath}/userInfo/addUserInfo',
			success : function(data) {
				eval('data=' + data);
				if (data.dealFlag) {
					grid.datagrid('reload');
					$('#newuser-window').dialog('close');
					$.messager.show({
						title : '提示',
						msg : data.msg
					});
				} else {
					$.messager.show({
						title : '提示',
						msg : data.msg,
						showType : 'slide',
						timeout : 2000
					});
				}
			}
		});
	}
	
	//打开修改窗口给form加载对应的回显数据内容 设置form提交的url
	function edit() {
		var row = grid.datagrid('getSelected');
		if (row != null) {
			//检查是否拥有编辑用户信息的权限
			var flag = checkAuth(row.id);
			if(!flag){
				$.messager.show({
					title : '警告',
					msg : '你的权限不足，不能进行该操作'
				});
				return;
			}
			$('#edituser-window').css("display","block");
			//定义修改用户对话框
			editUserWin = $('#edituser-window').dialog({
				title : '修改用户',
				width : 420,
				height : 280,
				modal : true,
				closed : false,
				draggable : false,
				buttons : '#edituser-buttons'
			});
			$('#edituser-window').find('form').form('load', {
				id : row.id,
				username : row.username,
				realname : row.realname,
				description : row.description
			});
			//获得下拉框的内容 
			var data = $('#xgqx').combobox('getData');
			var val = "";
			//与数据库中的权限值作比较 
			for (var i = 0; i < data.length; i++) {
				if (data[i].roleName == row.roles[0].roleName) {
					val = data[i].id;
				}
			}
			//选中应该回写的权限
			$('#xgqx').combobox('setValue', val);
		} else {
			$.messager.show({
				title : '警告',
				msg : '请先选择用户。'
			});
		}
	}
	
	//提交修改用户信息的方法
	function update() {
		$('#edituser-window').find('form').form('submit', {
			url : '${pageContext.request.contextPath}/userInfo/updateUserInfo',
			success : function(data) {
				eval('data=' + data);
				if (data.dealFlag) {
					grid.datagrid('reload');
					$('#edituser-window').dialog('close');
					$.messager.show({
						title : '提示',
						msg : data.msg
					});
				} else {
					{
						$.messager.show({
							title : '提示',
							msg : data.msg,
							showType : 'slide',
							timeout : 2000
						});
					}
				}
			}
		});
	}
	//提交删除用户的方法
	function del() {
		var selected = grid.datagrid('getSelected');
		if (selected) {
			//检查是否拥有删除用户信息的权限
			var flag = checkAuth(selected.id);
			if(!flag){
				$.messager.show({
					title : '警告',
					msg : '你的权限不足，不能进行该操作'
				});
				return;
			}
			$.messager
					.confirm(
							'警告',
							'确认删除么?',
							function(id) {
								if (id) {
									id = selected.id;
									$
											.ajax({
												type : "POST",
												url : "${pageContext.request.contextPath}/userInfo/deleteUserInfo",
												data : "id=" + id,
												dataType : "json",
												success : function callback(
														data) {
													if (!data.dealFlag) {
														$.messager.show({
															title : '警告',
															msg : data.msg
														});
													} else {
														$.messager.show({
															title : '提示',
															msg : data.msg
														});
													}
													grid.datagrid('reload');
												}
											});
								}
							});
		} else {
			$.messager.show({
				title : '警告',
				msg : '请先选择用户。'
			});
		}
	}

	/* 判断当前用户是否有权限进行编辑、修改操作 */
	function checkAuth(id){
		var flag = false;
		$.ajax({
			async : false,
			type : "post",
			url : "${pageContext.request.contextPath}/userInfo/checkAuth",
			data : "id=" + id,
			dataType : "json",
			success : function callback(data) {
				if (!data) {
					flag = false;
				} else {
					flag = true;
				}
			}
		});
		return flag;
	}
	
	//关闭修改密码窗体
	function closeWindowforup() {
		updatepdWin.window('close');
	}

	//修改密码的方法
	function updatepd() {
		var row = grid.datagrid('getSelected');
		if (row) {
			//检查是否拥有删除用户信息的权限
			var flag = checkAuth(row.id);
			if(!flag){
				$.messager.show({
					title : '警告',
					msg : '你的权限不足，不能进行该操作'
				});
				return;
			}
			updatepdWin.window('open');
			updatepdForm.form('clear');
			var selected = grid.datagrid('getSelected');
			document.getElementById("usernameforup").value = selected.username;
			updatepdForm.url = '${pageContext.request.contextPath}/userInfo/updatepasswordbyadmin';
		} else {
			$.messager.show({
				title : '警告',
				msg : '请先选择用户。'
			});
		}

	}

	//设置修改密码提交方法 显示返回消息
	function uppd() {
		updatepdForm.form('submit', {
			url : updatepdForm.url,
			success : function(data) {
				eval('data=' + data);
				if (data.dealFlag) {
					updatepdWin.window('close');
					$.messager.show({
						title : '提示',
						msg : data.msg
					});
				} else {
					{
						$.messager.show({
							title : '提示',
							msg : data.msg,
							showType : 'slide',
							timeout : 2000
						});
					}
				}
			}
		});
	}

	//重写easyui 验证方法 新增密码确认认证
	$
			.extend(
					$.fn.validatebox.defaults.rules,
					{
						equals : {
							validator : function(value, param) {
								return value == $(param[0]).val();
							},
							message : '两次密码不匹配'
						},

						Accounts : {
							validator : function(value, param) {
								var flag = "";
								$
										.ajax({
											async : false,
											type : "POST",
											url : "${pageContext.request.contextPath}/userInfo/isSameName",
											data : {
												username : value
											},
											dataType : "json",
											success : function(data) {
												if (data.dealFlag) {
													flag = true;
												} else {
													flag = false;
												}
											}
										});
								return flag;
							},
							message : '用户名已存在'
						},

						name : {
							validator : function(value, param) {
								var reg = /^[\u4e00-\u9fa5]+$/;
								return reg.exec(value);
							},
							message : '请输入汉字姓名'
						},
						length : {
							validator : function(value, param) {
								return value.length < 50;
							},
							message : '请输入少于50字描述'
						},
						username : {
							validator : function(value, param) {
								var reg = /^[a-zA-Z]\w*$/;
								return reg.test(value);
							},
							message : '用户名中只能出现字母、数字和下划线，且首字符必须为字母'
						}
					});

	//添加右击菜单内容
	function onRowContextMenu(e, rowIndex, rowData) {
		e.preventDefault();
		//先选中一行
		grid.datagrid('selectRow', rowIndex);
		$('#mm').menu('show', {
			left : e.pageX,
			top : e.pageY
		});
	}
</script>
</head>
<body>

	<!-- 展示页gird -->
	<table id="user-table" style="height: 500px"></table>

	<!-- 显示用户详细信息的div -->
	<div id="detail-window" title="用户详细信息" style="display : none;">
		<div style="padding: 10px 10px 10px 10px;">
			<form onkeydown="if(event.keyCode==8)return false;">
				<table>
					<tr>
						<td style="text-align: right;" width="60">用户名：</td>
						<td><input name="username" style="border: 0;"
							readonly="readonly"></input>&nbsp;</td>
					</tr>
					<tr>
						<td style="text-align: right;">姓名：</td>
						<td><input name="realname" style="border: 0;"
							readonly="readonly"></input>&nbsp;</td>
					</tr>
					<tr>
						<td style="text-align: right;">权限：</td>
						<td><input id="detailqx" name="roleId" readonly="readonly"
							style="border: 0;">&nbsp;</td>
					</tr>
					<tr>
						<td valign="top" style="text-align: right; padding-top: 3px;">描述：</td>
						<td><textarea name="description" readonly="readonly"
								style="resize: none; width: 180px; FONT-SIZE: 13px; height: 50px; border: 0; overflow: hidden"></textarea></td>
					</tr>
				</table>
			</form>
		</div>
	</div>

	<!-- 新增用户window  -->
	<div id="newuser-window" title="新增用户" style="display : none;">
		<div style="padding: 10px 10px 10px 10px;">
			<form method="post">
				<table>
					<tr>
						<td style="text-align: right;">用户名：</td>
						<td><input name="username" class="white" id="username"
							data-options="validType:['username[]','Accounts[]']"></input>&nbsp;<font
							color="red">*</font></td>
					</tr>
					<tr>
						<td style="text-align: right;">密码：</td>
						<td><input name="password" class="white" id="password"
							type="password"></input>&nbsp;<font color="red">*</font></td>
					</tr>
					<tr>
						<td style="text-align: right;">密码确认：</td>
						<td><input id="renewpwd" class="white" type="password"
							validType="equals['#password']">&nbsp;<font color="red">*</font></td>
					</tr>
					<tr>
						<td style="text-align: right;">姓名：</td>
						<td><input name="realname" class="white" id="realname"
							validType="name[]"></input>&nbsp;<font color="red">*</font></td>
					</tr>
					<tr>
						<td style="text-align: right;">权限：</td>
						<td><input id="qx" name="roleId" class="easyui-combobox"
							required="true"
							style="background: #ffffff; border: 1px solid #7EBDD9; min-height: 20px;"
							data-options="url:'${pageContext.request.contextPath}/userInfo/findRoleInfo',valueField:'id', textField:'roleName', panelHeight:'auto',panelWidth:182,editable:false," />&nbsp;<font
							color="red">*</font></td>
					</tr>
					<tr>
						<td valign="top" style="text-align: right;">描述：</td>
						<td><textarea class="easyui-validatebox" id="description"
								style="resize: none; width: 148px; FONT-SIZE: 13px; height: 65px;"
								name="description" class="white" validType="length[]"></textarea></td>
					</tr>
				</table>
			</form>
		</div>
	</div>
	<div id="newuser-buttons" style="text-align: center; padding: 5px;">
		<a href="javascript:void(0)" onclick="save()" id="btn-save"
			icon="oper_but_save">保存</a> <a href="javascript:void(0)"
			onclick="newUserWin.window('close');" id="btn-cancel" icon="oper_but_cancel">取消</a>
	</div>
	<!-- 修改的div -->
	<div id="edituser-window" title="修改用户" style="display : none;">
		<div style="padding: 10px 10px 10px 10px;">
			<form method="post">
				<table>
					<input type="hidden" name="id"/>  <!-- 用户ID -->
					<tr>
						<td style="text-align: right;">用户名：</td>
						<td><input name="username" class="white" id="editusername"
							onkeydown="if(event.keyCode==8)return false;" readonly="readonly"></input>&nbsp;<font
							color="red">*</font></td>
					</tr>
					<tr>
						<td style="text-align: right;">姓名：</td>
						<td><input name="realname" class="white" id="editrealname"
							validType="name[]"></input>&nbsp;<font color="red">*</font></td>
					</tr>
					<tr>
						<td style="text-align: right;">权限：</td>
						<td><input id="xgqx" name="roleId" class="easyui-combobox"
							required="true" 
							style="background: #ffffff; border: 1px solid #7EBDD9; min-height: 20px;"
							data-options="url:'${pageContext.request.contextPath}/userInfo/findRoleInfo',valueField:'id', textField:'roleName', panelHeight:'auto',panelWidth:182,editable:false">&nbsp;<font
							color="red">*</font></td>
					</tr>
					<tr>
						<td valign="top" style="text-align: right;">描述：</td>
						<td><textarea name="description"
								class="easyui-validatebox white"
								style="resize: none; width: 148px; FONT-SIZE: 13px; height: 65px;"
								validType="length[]"></textarea></td>
					</tr>
				</table>
			</form>
		</div>
	</div>
	<div id="edituser-buttons" style="text-align: center; padding: 5px;">
		<a href="javascript:void(0)" onclick="update()" id="btn-save"
			icon="oper_but_save">保存</a> <a href="javascript:void(0)"
			onclick="editUserWin.window('close');" id="btn-cancel" icon="oper_but_cancel">取消</a>
	</div>
	<!-- 修改密码div -->
	<!-- class="easyui-window"  -->
	<div id="updatepd-window" title="修改密码" class="easyui-dialog">
		<div style="padding: 10px 10px 10px 10px;">
			<form method="post">
				<table>
					<tr>
						<td>账号：</td>
						<td><input id="usernameforup" name="usernameforup"
							onkeydown="if(event.keyCode==8)return false;" class="white"
							style="min-height: 20px;" readonly="readonly"></input></td>
					</tr>
					<tr>
						<td>密码：</td>
						<td><input id="newpwd" class="white" name="newpassword"
							type="password">&nbsp;<font color="red">*</font></td>
					</tr>
					<tr>
						<td>密码确认：</td>
						<td><input id="renewpwdforup" class="white" type="password"
							validType="equals['#newpwd']">&nbsp;<font color="red">*</font></td>
					</tr>
				</table>
			</form>
		</div>
	</div>
	<div id="updatepd-buttons" style="text-align: center; padding: 5px;">
		<a href="javascript:void(0)" onclick="uppd()" id="btn-save"
			icon="oper_but_save">保存</a> <a href="javascript:void(0)"
			onclick="closeWindowforup()" id="btn-cancel" icon="oper_but_cancel">取消</a>
	</div>

	<!-- 右键菜单 -->
	<div id="mm" class="easyui-menu" style="width: 120px;">
		<div onClick="view()" data-options="iconCls:'icon-search'">详细信息</div>
		<div class="menu-sep"></div>
		<div onClick="add()" data-options="iconCls:'oper_add'">新增</div>
		<div onClick="edit()" data-options="iconCls:'icon-edit'">编辑</div>
		<div onClick="del()" data-options="iconCls:'oper_remove'">删除</div>
		<div class="menu-sep"></div>
		<div onClick="updatepd()" data-options="iconCls:'oper_edit_password'">修改密码</div>
	</div>
</body>
</html>
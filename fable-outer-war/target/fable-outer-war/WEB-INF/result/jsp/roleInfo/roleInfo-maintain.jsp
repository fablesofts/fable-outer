<%@ page language="java" pageEncoding="utf-8"%>
<html>
<head>
<title>角色管理</title>
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
<script type="text/javascript">
	$(function() {
		//验证
		$('#roleName,#editRoleName').validatebox({
			required : true,
			missingMessage : '请输入角色名!'
		});

		//初始化角色信息列表数据
		grid = $('#role-table')
				.datagrid(
						{
							url : '${pageContext.request.contextPath}/roleInfo/findRoleInfoList',
							//title : '角色信息列表',
							fit : true,
							border : false,
							singleSelect : true,
							fitColumns : true,
							autoRowHeight : true,
							loadMsg : '角色资料正在加载,请稍后 !',
							striped : true,
							nowrap : false,
							pagination : true,
							rownumbers : true,
							resizeHandle : 'both',
							pagePosition : 'bottom',
							scrollbarSize : 10,
							onRowContextMenu : onRowContextMenu,
							frozenColumns : [ [ {
								field : 'id',
								hidden : true
							}, ] ],
							columns : [ [ {
								field : 'roleName',
								title : '角色名',
								width : 80,
								align : 'center'
							}, {
								field : 'description',
								title : '描述',
								width : 80,
								align : 'center'
							}, {
								field : 'menuNames',
								title : '菜单',
								width : 80,
								align : 'center'
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
								text : '配置菜单',
								iconCls : 'icon-edit',
								handler : configMenus
							} ]
						});
		//初始化按钮
		$('#btn-save,#btn-cancel').linkbutton();
		//定义新增角色对话框
		win = $('#new-window').dialog({
			title : '新增角色',
			width : 420,
			height : 220,
			modal : true,
			closed : true,
			draggable : false,
			buttons : '#new-buttons'
		});
		//定义修改角色对话框
		editWin = $('#edit-window').dialog({
			title : '修改角色',
			width : 420,
			height : 220,
			modal : true,
			closed : true,
			draggable : false,
			buttons : '#edit-buttons'
		});
		//定义显示角色详细信息对话框
		detailWin = $('#detail-window').dialog({
			title : '角色详细信息',
			width : 420,
			height : 180,
			modal : true,
			closed : true,
			draggable : false
		});
		//为角色配置菜单的窗口 
		configMenuWin = $('#configMenu-window').dialog({
			title : '配置菜单',
			width : 420,
			height : 340,
			modal : true,
			closed : true,
			draggable : false,
			buttons : '#config-buttons'
		});
		//初始化form
		form = win.find('form');
		editForm = editWin.find('form');
		configMenuForm = configMenuWin.find('form');
		//设置分页控件  
		var p = $('#role-table').datagrid('getPager');
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
	var win;
	var form;
	var editWin;
	var editForm;
	var detailWin;
	var configMenuWin;
	var configMenuForm;
	/*为角色配置菜单的方法
	//打开配置菜单窗口，初始化菜单选项,回显角色数据，设置提交的url
	//获取选中角色可以访问的菜单数组，遍历菜单数组，选中可以访问的菜单*/
	function configMenus() {
		var row = grid.datagrid('getSelected');
		if (row != null) {
			configMenuWin.window('open');
			//初始化菜单选项
			$('#tt')
					.tree(
							{
								url : '${pageContext.request.contextPath}/roleInfo/findMenuInfoList?time='
										+ new Date().getMilliseconds(),
								animate : true,
								checkbox : true,
								method : 'get',
								onlyLeafCheck : true,
								onLoadSuccess : function() {
									//获取选中角色可以访问的菜单数组，遍历菜单数组，选中可以访问的菜单
									var menus = row.menus;
									for (var i = 0; i < menus.length; i++) {
										var node = $('#tt').tree('find',
												menus[i].id);
										$('#tt').tree('check', node.target);
									}
								}
							});
			//回显角色信息
			configMenuForm.form('load', {
				id : row.id,
				roleName : row.roleName
			});
			configMenuForm.url = '${pageContext.request.contextPath}/roleInfo/configMenus';
		} else {
			$.messager.show({
				title : '警告',
				msg : '请先选择角色。'
			});
		}
	}
	//提交配置菜单的方法
	//获取选中的菜单节点，连接菜单的ID作为参数，提交表单
	function updateMenu() {
		var nodes = $('#tt').tree('getChecked', 'checked');
		var menuIds = "";
		for (var i = 0; i < nodes.length; i++) {
			menuIds += nodes[i].id + ",";
		}
		$('#menus').val(menuIds.substring(0, menuIds.length - 1));
		configMenuForm.form('submit', {
			url : configMenuForm.url,
			success : function(data) {
				eval('data=' + data);
				if (data.dealFlag) {
					grid.datagrid('reload');
					configMenuWin.window('close');
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

	//打开显示角色详细信息窗口，加载角色的详细信息
	function view() {
		var row = grid.datagrid('getSelected');
		if (row != null) {
			detailWin.window('open');
			//回显选中角色的详细信息数据      
			$('#detail-window').find('form').form('load', {
				roleName : row.roleName,
				menuNames : row.menuNames,
				description : row.description
			});
		} else {
			$.messager.show({
				title : '警告',
				msg : '请先选择角色。'
			});
		}
	}

	//添加角色信息的方法
	//打开新增角色窗口，清空表单内容，设置表单提交的url
	function add() {
		win.window('open');
		form.form('clear');
		form.url = '${pageContext.request.contextPath}/roleInfo/addRoleInfo';
	}
	//编辑角色信息的方法
	//打开修改角色窗口，回显选中角色的数据，设置表单提交的url
	function edit() {
		var row = grid.datagrid('getSelected');
		if (row != null) {
			editWin.window('open');
			editForm.form('load', {
				id : row.id,
				roleName : row.roleName,
				description : row.description,
			});
			editForm.url = '${pageContext.request.contextPath}/roleInfo/updateRoleInfo';
		} else {
			$.messager.show({
				title : '警告',
				msg : '请先选择角色。'
			});
		}
	}
	//提交新增角色的方法
	function save() {
		form.form('submit', {
			url : form.url,
			success : function(data) {
				eval('data=' + data);
				if (data.dealFlag) {
					grid.datagrid('reload');
					win.window('close');
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
	//提交修改角色信息的方法
	function update() {
		editForm.form('submit', {
			url : editForm.url,
			success : function(data) {
				eval('data=' + data);
				if (data.dealFlag) {
					grid.datagrid('reload');
					editWin.window('close');
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
	//提交删除角色的方法
	function del() {
		var row = grid.datagrid('getSelected');
		if (row != null) {
			$.messager
					.confirm(
							'警告',
							'确认删除么?',
							function(id) {
								if (id) {
									id = row.id;
									$
											.ajax({
												type : "POST",
												url : "${pageContext.request.contextPath}/roleInfo/deleteRoleInfo",
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
				msg : '请先选择角色。'
			});
		}
	}
	//关闭window方法
	function closeWindow() {
		win.window('close');//关闭添加角色窗口
		editWin.window('close');//关闭编辑角色信息窗口
		detailWin.window('close');//关闭角色详细信息窗口
		configMenuWin.window('close');//关闭配置菜单窗口
	}

	//重写easyui 验证方法 新增密码确认认证
	$
			.extend(
					$.fn.validatebox.defaults.rules,
					{
						sameName : {
							validator : function(value, param) {
								var flag = "";
								$
										.ajax({
											async : false,
											type : "POST",
											url : "${pageContext.request.contextPath}/roleInfo/isSameName",
											data : {
												roleName : value
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
							message : '角色名已存在'
						},

						name : {
							validator : function(value, param) {
								var reg = /^[\u4e00-\u9fa5]+$/;
								return reg.exec(value);
							},
							message : '请输入汉字角色名'
						},
						length : {
							validator : function(value, param) {
								return value.length < 50;
							},
							message : '请输入少于50字的描述'
						},
						roleName : {
							validator : function(value, param) {
								var reg = /^[a-zA-Z]\w*$/;
								return reg.test(value);
							},
							message : '角色名中只能出现字母、数字和下划线，且首字符必须为字母'
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
	<table id="role-table" style="height: 500px"></table>

	<!-- 显示角色详细信息的div -->
	<div id="detail-window" title="角色详细信息" class="easyui-dialog">
		<div style="padding: 10px 10px 10px 10px;">
			<form onkeydown="if(event.keyCode==8)return false;">
				<table>
					<tr>
						<td style="text-align: right;">角色名：</td>
						<td><input name="roleName" readonly="readonly"
							style="border: 0;" /></td>
					</tr>
					<tr>
						<td style="text-align: right;">描述：</td>
						<td><input name="description" readonly="readonly"
							style="border: 0;" /></td>
					</tr>
					<tr>
						<td valign="top" style="text-align: right;">菜单：</td>
						<td><textarea name="menuNames" readonly="readonly"
								style="resize: none; width: 180px; FONT-SIZE: 13px; height: 80px; border: 0; overflow: hidden"></textarea></td>
					</tr>
				</table>
			</form>
		</div>
	</div>

	<!-- 新增角色的div  -->
	<div id="new-window" title="新增角色" class="easyui-dialog">
		<div style="padding: 10px 10px 10px 10px;">
			<form method="post">
				<table>
					<tr>
						<td style="text-align: right;">角色名：</td>
						<td><input id="roleName" name="roleName" class="white"
						data-options="validType:['name[]','sameName[]']" />&nbsp;<font color="red">*</font></td>
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
	<div id="new-buttons" style="text-align: center; padding: 5px;">
		<a href="javascript:void(0)" onclick="save()" id="btn-save"
			icon="oper_but_save">保存</a> <a href="javascript:void(0)"
			onclick="closeWindow()" id="btn-cancel" icon="oper_but_cancel">取消</a>
	</div>
	<!-- 修改角色信息的div -->
	<div id="edit-window" title="修改角色" class="easyui-dialog">
		<div style="padding: 10px 10px 10px 10px;">
			<form method="post">
				<table>
					<input name="id" type="hidden" readonly="readonly" />
					<tr>
						<td style="text-align: right;">角色名：</td>
						<td><input id="editRoleName" name="roleName" class="white"
							onkeydown="if(event.keyCode==8)return false;" readonly="readonly" />&nbsp;<font
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
	<div id="edit-buttons" style="text-align: center; padding: 5px;">
		<a href="javascript:void(0)" onclick="update()" id="btn-save"
			icon="oper_but_save">保存</a> <a href="javascript:void(0)"
			onclick="closeWindow()" id="btn-cancel" icon="oper_but_cancel">取消</a>
	</div>

	<!-- 配置角色可以访问的菜单的div -->
	<div id="configMenu-window" title="配置菜单" class="easyui-dialog">
		<div style="padding: 10px 10px 10px 10px;">
			<form method="post">
				<table>
					<input name="id" type="hidden" readonly="readonly" />
					<tr>
						<td style="text-align: right;">角色名：</td>
						<td><input name="roleName" class="white" readonly="readonly"
							style="border: 0;" onkeydown="if(event.keyCode==8)return false;" /></td>
					</tr>
					<tr>
						<td valign="top" style="text-align: right;">菜单：</td>
						<td><ul id="tt"></ul></td>
					</tr>
					<input id="menus" name="menuIds" type="hidden" />
				</table>
			</form>
		</div>
	</div>
	<div id="config-buttons" style="text-align: center; padding: 5px;">
		<a href="javascript:void(0)" onclick="updateMenu()" id="btn-save"
			icon="oper_but_save">保存</a> <a href="javascript:void(0)"
			onclick="closeWindow()" id="btn-cancel" icon="oper_but_cancel">取消</a>
	</div>

	<!-- 右键菜单 -->
	<div id="mm" class="easyui-menu" style="width: 120px;">
		<div onClick="view()" data-options="iconCls:'icon-search'">详细信息</div>
		<div class="menu-sep"></div>
		<div onClick="add()" data-options="iconCls:'oper_add'">新增</div>
		<div onClick="edit()" data-options="iconCls:'icon-edit'">编辑</div>
		<div onClick="del()" data-options="iconCls:'oper_remove'">删除</div>
		<div class="menu-sep"></div>
		<div onClick="configMenus()" data-options="iconCls:'icon-edit'">配置菜单</div>
	</div>
</body>
</html>
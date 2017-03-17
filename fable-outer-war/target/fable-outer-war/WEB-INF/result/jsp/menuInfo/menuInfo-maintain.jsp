<%@ page language="java" pageEncoding="utf-8"%>
<html>
<head>
<title>菜单管理</title>
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
		$('#menuName,#editMenuName').validatebox({
			required : true,
			missingMessage : '请输入菜单名!'
		});
		//
		$('#resName').validatebox({
			required : true,
			missingMessage : '请输入资源名!'
		});
		$('#URL,#editURL').validatebox({
			required : true,
			missingMessage : '请输入资源路径!'
		});
		$('#sortOrder').validatebox({
			required : true,
			missingMessage : '请输入一个整数!'
		});
		//初始化菜单信息列表数据
		grid = $('#menu-table')
				.treegrid(
						{
							url : '${pageContext.request.contextPath}/menuInfo/findMenuInfoList',
							//title : '菜单信息列表',
							idField : 'id',
							treeField : 'text',
							animate : true,
							collapsiable : true,
							singleSelect : true,
							fitColumns : true,
							autoRowHeight : true,
							fit : true,
							border : false,
							loadMsg : '菜单资料正在加载,请稍后 !',
							striped : true,
							nowrap : false,
							rownumbers : true,
							resizeHandle : 'both',
							scrollbarSize : 10,
							frozenColumns : [ [ {
								field : 'id',
								hidden : true
							}, ] ],
							columns : [ [ {
								field : 'text',
								title : '菜单名',
								width : 80,
							//align : 'center'
							}, {
								field : 'description',
								title : '描述',
								width : 80,
								align : 'center'
							}, {
								field : 'url',
								title : '访问路径',
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
							} ],
							onContextMenu : onContextMenu,
							onDblClickRow : findResourceInfo
						});

		//初始化显示资源信息的数据表格
		$('#resource-table').datagrid({
			singleSelect : true,
			fitColumns : true,
			autoRowHeight : true,
			loadMsg : '资源数据正在加载,请稍后 !',
			striped : true,
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
				field : 'resourceName',
				title : '资源名',
				width : 80,
				align : 'center',
			}, {
				field : 'url',
				title : '资源路径',
				width : 100,
				align : 'center',
			}, {
				field : 'type',
				title : '资源类型',
				width : 80,
				align : 'center',
			}, {
				field : 'description',
				title : '描述',
				width : 100,
				align : 'center',
			} ] ],
			toolbar : [ {
				text : '新增',
				iconCls : 'oper_add',
				handler : addRes
			}, '-', {
				text : '编辑',
				iconCls : 'icon-edit',
				handler : editRes
			}, '-', {
				text : '删除',
				iconCls : 'oper_remove',
				handler : delRes
			} ],
			onDblClickRow : viewRes
		});
		//设置资源信息数据表格的分页控件  
		var p = $('#resource-table').datagrid('getPager');
		$(p).pagination({
			pageSize : 10,//每页显示的记录条数，默认为10  
			pageList : [ 5, 10, 15 ],//可以设置每页记录条数的列表  
			beforePageText : '第',//页数文本框前显示的汉字  
			afterPageText : '页    共 {pages} 页',
			displayMsg : '当前显示 {from} - {to} 条记录   共 {total} 条记录'
		});

		//初始化按钮
		$('#btn-save,#btn-cancel').linkbutton();
		//定义新增菜单对话框
		win = $('#new-window').dialog({
			title : '新增菜单',
			width : 420,
			height : 300,
			modal : true,
			closed : true,
			draggable : false,
			buttons : '#new-buttons'
		});
		//定义修改菜单对话框
		editWin = $('#edit-window').dialog({
			title : '修改菜单',
			width : 420,
			height : 300,
			modal : true,
			closed : true,
			draggable : false,
			buttons : '#edit-buttons'
		});
		
		//显示菜单对应资源的对话框
		resourceWin = $('#resource-window').dialog({
			title : '资源信息列表',
			width : 600,
			height : 400,
			modal : true,
			closed : true,
			draggable : false
		});
		//初始化新增和编辑的form
		form = win.find('form');//新增菜单表单
		editForm = editWin.find('form');//编辑菜单表单
	});
	//设置需要的全局变量
	var grid;//显示菜单信息的树形表格
	var win;//新增菜单的窗口
	var form;//新增菜单的表单
	var editWin;//编辑菜单的窗口
	var editForm;//编辑菜单的表单
	var resourceWin;//资源管理窗口
	var newResWin;//新增资源窗口
	var editResWin;//编辑资源窗口
	var menuId;//用于在资源管理模块中记录资源关联的菜单ID

	//双击事件触发的操作，查询选中的叶子菜单拥有的资源
	function findResourceInfo() {
		var row = grid.treegrid('getSelected');
		if (row != null) {
			//判断选中的菜单是不是叶子菜单，如果是叶子菜单就显示其对应的资源，否则不显示
			if (row.children.length != 0)
				return;
			resourceWin.window('open');
			//设置新增资源对应的菜单ID
			menuId = row.id;
			//获取options对象，设置url，然后重新加载数据
			var opts = $('#resource-table').datagrid('options');
			opts.url = '${pageContext.request.contextPath}/menuInfo/findResourceInfoList?menuId='
					+ row.id;
			$('#resource-table').datagrid('reload');
		}
	}

	//添加资源的方法
	function addRes() {
		$('#newRes-window').css("display","block");
		//添加资源的对话框
		newResWin = $('#newRes-window').dialog({
			title : '新增资源',
			width : 420,
			height : 260,
			modal : true,
			closed : false,
			draggable : false,
			buttons : '#newRes-buttons'
		});
		$('#newRes-window').find('form').form('clear');
		//设置新增资源对应的菜单ID
		$('#menuId').val(menuId);
	}
	//提交新增资源的方法
	function saveRes() {
		$('#newRes-window').find('form').form('submit', {
			url : '${pageContext.request.contextPath}/menuInfo/addResourceInfo',
			success : function(data) {
				eval('data=' + data);
				if (data.dealFlag) {
					$('#resource-table').datagrid('reload');
					$('#newRes-window').dialog('close');
					$.messager.show({
						title : '提示',
						msg : data.msg,
						showType : 'slide',
						timeout : 2000
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
	//编辑资源的方法
	function editRes() {
		var row = $('#resource-table').datagrid('getSelected');
		if (row != null) {
			$('#editRes-window').css("display","block");
			//编辑资源信息的对话框 
			editResWin = $('#editRes-window').dialog({
				title : '修改资源',
				width : 420,
				height : 260,
				modal : true,
				closed : false,
				draggable : false,
				buttons : '#editRes-buttons'
			});
			$('#editRes-window').find('form').form('clear');
			$('#editRes-window').find('form').form('load', {
				id : row.id,
				resourceName : row.resourceName,
				url : row.url,
				type : row.type,
				description : row.description
			});
		} else {
			$.messager.show({
				title : '警告',
				msg : '请先选择资源。'
			});
		}
	}
	//提交编辑资源信息的方法
	function updateRes() {
		$('#editRes-window').find('form').form('submit', {
			url : '${pageContext.request.contextPath}/menuInfo/updateResourceInfo',
			success : function(data) {
				eval('data=' + data);
				if (data.dealFlag) {
					$('#resource-table').datagrid('reload');
					$('#editRes-window').dialog('close');
					$.messager.show({
						title : '提示',
						msg : data.msg,
						showType : 'slide',
						timeout : 2000
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

	//删除资源的方法
	function delRes() {
		var row = $('#resource-table').datagrid('getSelected');
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
												url : "${pageContext.request.contextPath}/menuInfo/deleteResourceInfo",
												data : {
													"id" : id,
													"menuId" : menuId
												},
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
													$('#resource-table')
															.datagrid('reload');
												}
											});
								}
							});
		} else {
			$.messager.show({
				title : '警告',
				msg : '请先选择资源。'
			});
		}
	}

	//显示资源详细信息的方法
	function viewRes() {
		var row = $('#resource-table').datagrid('getSelected');
		if (row != null) {
			$('#detailRes-window').css("display","block");
			//定义显示资源详细信息的对话框
			$('#detailRes-window').dialog({
				title : '资源详细信息',
				width : 420,
				height : 180,
				modal : true,
				closed : false,
				draggable : false
			});
			$('#detailRes-window').find('form').form('load', {
				resourceName : row.resourceName,
				url : row.url,
				type : row.type,
				description : row.description
			});
		} else {
			$.messager.show({
				title : '警告',
				msg : '请先选择资源。'
			});
		}
	}

	//获取菜单图标
	function getIcons(id) {
		//清空combotree之前加载的数据，防止加载的数据重复
		$('#' + id).combotree('loadData', {
			data : [ {} ]
		});
		//设置下拉面板的宽度和默认图标
		$('#' + id).combotree({
			//value : 'icon-ok',
			panelWidth : 182,
		// 	panelHeight : 'auto'
		});
		for (var i = 0; i < document.styleSheets.length; i++) {
			var styleSheet = document.styleSheets[i];
			if (styleSheet.href.indexOf("icon.css") != -1) {
				var rules;
				if (styleSheet.cssRules) {
					rules = styleSheet.cssRules;
				} else {
					rules = styleSheet.rules;//IE
				}
				//追加菜单默认图标
				$('#' + id).combotree('tree').tree('append', {
					data : [ {
						id : 'icon-ok',
						text : 'icon-ok',
						iconCls : 'icon-ok'
					} ]
				});
				for (var j = 0; j < rules.length; j++) {
					if (rules[j].selectorText.substr(0, 5) == ".menu")
						$('#' + id).combotree('tree').tree('append', {
							data : [ {
								id : rules[j].selectorText.substr(1),
								text : rules[j].selectorText.substr(1),
								iconCls : rules[j].selectorText.substr(1)
							} ]
						});
				}
			}
		}
	}

	//打开显示菜单详细信息窗口，加载菜单的详细信息
	function view() {
		var row = grid.treegrid('getSelected');
		if (row != null) {
			$('#detail-window').css("display","block");
			//定义显示菜单详细信息对话框
			$('#detail-window').dialog({
				title : '菜单详细信息',
				width : 420,
				height : 200,
				modal : true,
				closed : false,
				draggable : false
			});
			//回显选中菜单的详细信息数据      
			$('#detail-window').find('form').form('load', {
				menuName : row.text,
				url : row.url,
				iconUrl : row.iconCls,
				sortOrder : row.sortOrder,
				description : row.description
			});
		} else {
			$.messager.show({
				title : '警告',
				msg : '请先选择菜单。'
			});
		}
	}
	//添加菜单信息的方法
	//打开新增菜单窗口，清空表单内容，设置表单提交的url
	function add() {
		win.window('open');
		form.form('clear');
		//在新增菜单时显示所有的菜单信息，用于选择菜单的父菜单
		$('#pmd1').combotree({
							url : '${pageContext.request.contextPath}/menuInfo/findMenuInfoList',
							required : true,
							missingMessage : '请选择父菜单!',
							//panelHeight : 'auto',
							panelWidth : 182
						});
		// 设置新增菜单排位的默认值为1
		$('#sortOrder').val("1");
		//设置默认图标
		$('#icon1').combotree({
			value : 'icon-ok'
		});
		//加载图标
		getIcons("icon1");
	}
	//提交新增菜单的方法
	function save() {
		//获取选中的父菜单的id
		var t = $('#pmd1').combotree('tree'); // get the tree object
		var node = t.tree('getSelected'); // get selected node
		if (node != null) {
			$('#parentMenuId1').val(node.id);
		} else {
			$('#parentMenuId1').val('');
		}
		form.form('submit', {
			url : '${pageContext.request.contextPath}/menuInfo/addMenuInfo',
			success : function(data) {
				eval('data=' + data);
				if (data.dealFlag) {
					grid.treegrid('reload');
					win.window('close');
					$.messager.show({
						title : '提示',
						msg : data.msg,
						showType : 'slide',
						timeout : 2000
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

	//编辑菜单信息的方法
	//打开修改菜单窗口，回显选中菜单的数据，设置表单提交的url
	function edit() {
		var row = grid.treegrid('getSelected');
		if (row != null) {
			editWin.window('open');
			//在编辑菜单时显示所有的菜单信息，用于选择菜单的父菜单
			$('#pmd2').combotree({
								url : '${pageContext.request.contextPath}/menuInfo/findMenuInfoList',
								//panelHeight : 'auto',
								panelWidth : 182
							});
			editForm.form('load', {
				id : row.id,
				menuName : row.text,
				url : row.url,
				iconUrl : row.iconCls,
				sortOrder : row.sortOrder,
				description : row.description
			});
			//最高级别菜单不能修改其父菜单名，如果修改的是最高级别菜单就禁用父菜单选项
			if (row.menuLevel != 1) {
				$('#pmd2').combotree({
					disabled : false
				});
			} else {
				$('#pmd2').combotree({
					disabled : true
				});
			}
			//回显父菜单名。首先获取父菜单节点，然后再设置父菜单
			var parentNode = grid.treegrid('getParent', row.id);
			if (parentNode) {
				$('#pmd2').combotree('setValue', parentNode.id);
				//如果菜单不是最低级菜单，就禁用菜单的访问路径
				if (parentNode.menuLevel == 1) {
					//父菜单为一级，要修改的菜单必定为二级菜单，禁用菜单的访问路径
					$('#editUrl').get(0).disabled = true;
				} else if (parentNode.menuLevel == 2) {
					//父菜单为二级菜单，要修改的菜单必定为三级菜单，启用菜单的访问路径
					$('#editUrl').get(0).disabled = false;
				}
			} else {
				//parentNode为null，说明修改的菜单是最高级菜单，禁用菜单的访问路径
				$('#editUrl').get(0).disabled = true;
			}
			//回显图标
			$('#icon2').combotree({
				value : row.iconCls
			});
			//加载图标
			getIcons("icon2");
		} else {
			$.messager.show({
				title : '警告',
				msg : '请先选择菜单。'
			});
		}
	}

	//提交修改菜单信息的方法
	function update() {
		//获取选中的父菜单的id
		var t = $('#pmd2').combotree('tree'); // get the tree object
		var node = t.tree('getSelected'); // get selected node
		if (node != null) {
			$('#parentMenuId2').val(node.id);
		} else {
			$('#parentMenuId2').val('');
		}
		editForm.form('submit', {
			url : '${pageContext.request.contextPath}/menuInfo/updateMenuInfo',
			success : function(data) {
				eval('data=' + data);
				if (data.dealFlag) {
					grid.treegrid('reload');
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

	//提交删除菜单的方法
	function del() {
		var row = grid.treegrid('getSelected');
		if (row != null) {
			$.messager
					.confirm(
							'警告',
							'确认删除么?',
							function(id) {
								if (id) {
									if (row.menuLevel == 1) {
										$.messager.show({
											title : '警告',
											msg : '不能删除最高级菜单!'
										});
										return;
									}
									id = row.id;
									$
											.ajax({
												type : "POST",
												url : "${pageContext.request.contextPath}/menuInfo/deleteMenuInfo",
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
													grid.treegrid('reload');
												}
											});
								}
							});
		} else {
			$.messager.show({
				title : '警告',
				msg : '请先选择菜单。'
			});
		}
	}

	//关闭菜单window方法
	function closeWindow() {
		win.window('close');//关闭新增菜单窗口
		editWin.window('close');//关闭编辑菜单窗口
// 		editResWin.window('close');//关闭编辑资源窗口
// 		newResWin.window('close');//关闭新增资源窗口
	}

	//重写easyui 验证方法 新增密码确认认证
	$
			.extend(
					$.fn.validatebox.defaults.rules,
					{
						//验证菜单名是否重复
						sameName : {
							validator : function(value, param) {
								//获取要修改菜单的ID
								var menuId = $('#editMenuId').val();
								var flag = "";
								$
										.ajax({
											async : false,
											type : "POST",
											url : "${pageContext.request.contextPath}/menuInfo/isSameMenuName",
											data : {
												menuName : value,
												menuId : menuId
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
							message : '菜单名已存在'
						},
						//验证资源名是否重复
						sameResName : {
							validator : function(value, param) {
								//获取要修改资源的ID
								var resourceId = $('#resourceId').val();
								var flag = "";
								$
										.ajax({
											async : false,
											type : "POST",
											url : "${pageContext.request.contextPath}/menuInfo/isSameResName",
											data : {
												name : value,
												resourceId : resourceId
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
							message : '资源名已存在'
						},
						//验证资源url是否重复
						isSameURL : {
							validator : function(value, param) {
								var resourceId = $(param[0]).val();
								var flag = "";
								$
										.ajax({
											async : false,
											type : "POST",
											url : "${pageContext.request.contextPath}/menuInfo/isSameURL",
											data : {
												url : value,
												resourceId : resourceId
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
							message : '资源路径已存在'
						},

						name : {
							validator : function(value, param) {
								var reg = /^[\u4e00-\u9fa5a-zA-Z]+$/;
								return reg.exec(value);
							},
							message : '请输入名字，可以是中文或字母'
						},
						length : {
							validator : function(value, param) {
								return value.length < 50;
							},
							message : '请输入少于50字的描述'
						},
						text : {
							validator : function(value, param) {
								var reg = /^[a-zA-Z]\w*$/;
								return reg.test(value);
							},
							message : '菜单名中只能出现字母、数字和下划线，且首字符必须为字母'
						},
						isInteger : {
							validator : function(value, param) {
								var reg = /^[1-9]\d*$/;
								return reg.exec(value);
							},
							message : '请输入整数'
						}
					});

	//菜单右击事件的方法
	function onContextMenu(e, row) {
		e.preventDefault();
		//先选中一行
		grid.treegrid('select', row.id);
		$('#mm').menu('show', {
			left : e.pageX,
			top : e.pageY
		});
	}

	//资源右击事件的方法
	function onRowContextMenu(e, rowIndex, rowData) {
		e.preventDefault();
		//先选中一行
		$('#resource-table').datagrid('selectRow', rowIndex);
		$('#mmRes').menu('show', {
			left : e.pageX,
			top : e.pageY
		});
	}
</script>
</head>
<body>

	<!-- 展示页gird -->
	<table id="menu-table"></table>

	<!-- 显示菜单详细信息的div -->
	<div id="detail-window" title="菜单详细信息" style="display: none;">
		<div style="padding: 10px 10px 10px 10px;">
			<form onkeydown="if(event.keyCode==8) return false;">
				<table>
					<tr>
						<td style="text-align: right;">菜单名：</td>
						<td><input name="menuName" readonly="readonly"
							style="border: 0;" /></td>
					</tr>
					<tr>
						<td style="text-align: right;">访问路径：</td>
						<td><input name="url" readonly="readonly" style="border: 0;" /></td>
					</tr>
					<tr>
						<td style="text-align: right;">菜单图标：</td>
						<td><input id="detailIconUrl" name="iconUrl"
							readonly="readonly" style="border: 0;" /></td>
					</tr>
					<tr>
						<td style="text-align: right;">菜单排位：</td>
						<td><input name="sortOrder" readonly="readonly"
							style="border: 0;" /></td>
					</tr>
					<tr>
						<td valign="top" style="text-align: right;">描述：</td>
						<td><textarea name="description" readonly="readonly"
								style="resize: none; width: 148px; FONT-SIZE: 13px; height: 50px; border: 0; overflow: hidden;"></textarea></td>
					</tr>
				</table>
			</form>
		</div>
	</div>

	<!-- 新增菜单的div  -->
	<div id="new-window" title="新增菜单" >
		<div style="padding: 10px 10px 10px 10px;">
			<form method="post">
				<table>
					<tr>
						<td style="text-align: right;">菜单名：</td>
						<td><input id="menuName" name="menuName" class="white"
							data-options="validType:['name[]','sameName[]']" />&nbsp;<font
							color="red">*</font></td>
					</tr>
					<tr>
						<td style="text-align: right;">父菜单名：</td>
						<td><input id="pmd1"
							style="background: #ffffff; border: 1px solid #7EBDD9; min-height: 20px; width: 150px;" />&nbsp;<font
							color="red">*</font>
						</td>
					</tr>
					<tr>
						<td style="text-align: right;">访问路径：</td>
						<td><input id="url" name="url" class="white" /></td>
					</tr>
					<tr>
						<td style="text-align: right;">菜单图标：</td>
						<td><input id="icon1" class="easyui-combotree"
							name="iconUrl"
							style="background: #ffffff; border: 1px solid #7EBDD9; min-height: 20px; width: 150px;" /></td>
					</tr>
					<tr>
						<td style="text-align: right;">菜单排位：</td>
						<td><input id="sortOrder" name="sortOrder"
							class="easyui-validatebox white"
							data-options="required: true,validType:'isInteger[]'" />&nbsp;<font
							color="red">*</font>
						</td>
					</tr>
					<tr>
						<td valign="top" style="text-align: right;">描述：</td>
						<td><textarea name="description"
								class="easyui-validatebox white"
								style="resize: none; width: 148px; FONT-SIZE: 13px; height: 50px;"
								validType="length[]"></textarea></td>
					</tr>
					<input id="parentMenuId1" name="PID" type="hidden" />   <!-- 父菜单ID -->
				</table>
			</form>
		</div>
	</div>
	<div id="new-buttons" style="text-align: center; padding: 5px;">
		<a href="javascript:void(0)" onclick="save()" id="btn-save"
			icon="oper_but_save">保存</a> <a href="javascript:void(0)"
			onclick="closeWindow()" id="btn-cancel" icon="oper_but_cancel">取消</a>
	</div>
	<!-- 修改菜单信息的div -->
	<div id="edit-window" title="修改菜单" >
		<div style="padding: 10px 10px 10px 10px;">
			<form method="post">
				<table>
					<input id="editMenuId" name="id" type="hidden" />
					<tr>
						<td style="text-align: right;">菜单名：</td>
						<td><input id="editMenuName" name="menuName" class="white"
							data-options="validType:['name[]','sameName[]']" />&nbsp;<font
							color="red">*</font></td>
					</tr>
					<tr>
						<td style="text-align: right;">父菜单名：</td>
						<td><input id="pmd2"
							style="background: #ffffff; border: 1px solid #7EBDD9; min-height: 20px; width: 150px;" />
						</td>
					</tr>
					<tr>
						<td style="text-align: right;">访问路径：</td>
						<td><input id="editUrl" name="url" class="white" /></td>
					</tr>
					<tr>
						<td style="text-align: right;">菜单图标：</td>
						<td><input id="icon2" class="easyui-combotree"
							name="iconUrl"
							style="background: #ffffff; border: 1px solid #7EBDD9; min-height: 20px; width: 150px;" /></td>
					</tr>
					<tr>
						<td style="text-align: right;">菜单排位：</td>
						<td><input id="editSortOrder" name="sortOrder"
							class="easyui-validatebox white"
							data-options="required: true,validType:'isInteger[]'" /></td>
					</tr>
					<tr>
						<td valign="top" style="text-align: right;">描述：</td>
						<td><textarea name="description"
								class="easyui-validatebox white"
								style="resize: none; width: 148px; FONT-SIZE: 13px; height: 50px;"
								validType="length[]"></textarea></td>
					</tr>
					<input id="parentMenuId2" name="PID" type="hidden" />
				</table>
			</form>
		</div>
	</div>
	<div id="edit-buttons" style="text-align: center; padding: 5px;">
		<a href="javascript:void(0)" onclick="update()" id="btn-save"
			icon="oper_but_save">保存</a> <a href="javascript:void(0)"
			onclick="closeWindow()" id="btn-cancel" icon="oper_but_cancel">取消</a>
	</div>
	<!-- 菜单页面的右键菜单 -->
	<div id="mm" class="easyui-menu" style="width: 120px;">
		<div onClick="view()" data-options="iconCls:'icon-search'">详细信息</div>
		<div class="menu-sep"></div>
		<div onClick="add()" data-options="iconCls:'oper_add'">新增</div>
		<div onClick="edit()" data-options="iconCls:'icon-edit'">编辑</div>
		<div onClick="del()" data-options="iconCls:'oper_remove'">删除</div>
		<div class="menu-sep"></div>
		<div onClick="findResourceInfo()" data-options="iconCls:'icon-search'">查看资源</div>
	</div>

	<!-- **************************************************************************************** -->

	<!-- 显示菜单对应资源的div -->
	<div id="resource-window">
		<!-- 显示菜单对应资源的表格 -->
		<table id="resource-table"></table>
	</div>
	<!-- 添加资源的div -->
	<div id="newRes-window" style="display: none;">
		<div style="padding: 10px 10px 10px 10px;">
			<form method="post">
				<table>
					<!-- 当前菜单的ID -->
					<input id="menuId" name="menuId" type="hidden" />
					<tr>
						<td style="text-align: right;">资源名：</td>
						<td><input id="resName" name="resourceName" class="white"
							data-options="validType:['name[]','sameResName[]']" />&nbsp;<font
							color="red">*</font></td>
					</tr>
					<tr>
						<td style="text-align: right;">资源路径：</td>
						<td><input id="URL" name="url" class="white"
							validType="isSameURL[]" />&nbsp;<font color="red">*</font></td>
					</tr>
					<tr>
						<td style="text-align: right;">资源类型：</td>
						<td><input name="type" class="white" /></td>
					</tr>
					<tr>
						<td valign="top" style="text-align: right;">描述：</td>
						<td><textarea name="description"
								class="easyui-validatebox white"
								style="resize: none; width: 148px; FONT-SIZE: 13px; height: 50px;"
								validType="length[]"></textarea></td>
					</tr>
				</table>
			</form>
		</div>
		<div id="newRes-buttons" style="text-align: center; padding: 5px;">
			<a href="javascript:void(0)" onclick="saveRes()" id="btn-save"
				icon="oper_but_save">保存</a> <a href="javascript:void(0)"
				onclick="newResWin.window('close');" id="btn-cancel" icon="oper_but_cancel">取消</a>
		</div>
	</div>
	

	<!-- 修改资源的div -->
	<div id="editRes-window" style="display: none;">
		<div style="padding: 10px 10px 10px 10px;">
			<form method="post">
				<table>
					<input id="resourceId" name="id" type="hidden" />
					<tr>
						<td style="text-align: right;">资源名：</td>
						<td><input id="editResName" name="resourceName" class="white"
							data-options="validType:['name[]','sameResName[]']" />&nbsp;<font
							color="red">*</font></td>
					</tr>
					<tr>
						<td style="text-align: right;">资源路径：</td>
						<td><input id="editURL" name="url" class="white"
							validType="isSameURL['#resourceId']" />&nbsp;<font color="red">*</font></td>
					</tr>
					<tr>
						<td style="text-align: right;">资源类型：</td>
						<td><input name="type" class="white" /></td>
					</tr>
					<tr>
						<td valign="top" style="text-align: right;">描述：</td>
						<td><textarea name="description"
								class="easyui-validatebox white"
								style="resize: none; width: 148px; FONT-SIZE: 13px; height: 50px;"
								validType="length[]"></textarea></td>
					</tr>
				</table>
			</form>
		</div>
		<div id="editRes-buttons" style="text-align: center; padding: 5px;">
			<a href="javascript:void(0)" onclick="updateRes()" id="btn-save"
				icon="oper_but_save">保存</a> <a href="javascript:void(0)"
				onclick="editResWin.window('close');" id="btn-cancel" icon="oper_but_cancel">取消</a>
		</div>
	</div>
	

	<!-- 查看资源详细信息的div -->
	<div id="detailRes-window" style="display: none;">
		<div style="padding: 10px 10px 10px 10px;">
			<form onkeydown="if(event.keyCode==8) return false;">
				<table>
					<input name="id" type="hidden" />
					<tr>
						<td style="text-align: right;">资源名：</td>
						<td><input name="resourceName" readonly="readonly"
							style="border: 0;" /></td>
					</tr>
					<tr>
						<td style="text-align: right;">资源路径：</td>
						<td><input name="url" readonly="readonly" style="border: 0;" /></td>
					</tr>
					<tr>
						<td style="text-align: right;">资源类型：</td>
						<td><input name="type" readonly="readonly" style="border: 0;" /></td>
					</tr>
					<tr>
						<td valign="top" style="text-align: right;">描述：</td>
						<td><textarea name="description" readonly="readonly"
								style="resize: none; width: 148px; FONT-SIZE: 13px; height: 50px; border: 0; overflow: hidden;"></textarea></td>
					</tr>
				</table>
			</form>
		</div>
	</div>

	<!-- 资源页面的右键菜单 -->
	<div id="mmRes" class="easyui-menu" style="width: 120px;">
		<div onClick="viewRes()" data-options="iconCls:'icon-search'">详细信息</div>
		<div class="menu-sep"></div>
		<div onClick="addRes()" data-options="iconCls:'oper_add'">新增</div>
		<div onClick="editRes()" data-options="iconCls:'icon-edit'">编辑</div>
		<div onClick="delRes()" data-options="iconCls:'oper_remove'">删除</div>
	</div>


</body>
</html>
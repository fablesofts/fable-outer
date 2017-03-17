<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<%@ include file="/commons/meta.jsp"%>
<html>
<head>
<style type="text/css">
/* 重写 easyui的验证样式*/
.white{
	   background:  #ffffff;
	   border: 1px solid #7EBDD9;
	    min-height: 20px;
}
</style>
<script type="text/javascript">
//设置验证信息
$(function() {
	$('#btn-save,#btn-cancel').linkbutton();
	winupdatepd = $('#updatepd-window').window({
		closed : true,
		modal :true
	});
	formupdatepd= winupdatepd.find('form')
	
	$('#oldpwd').validatebox({
		required : true,
		missingMessage : '请输入旧密码!'
	});

	$('#newpwd').validatebox({
		required : true,
		missingMessage : '请输入新密码!'
	});
	$('#renewpwd').validatebox({
		required : true,
		missingMessage : '请再次输入新密码!'
	});
});
//定义全局变量
var winupdatepd;
var formupdatepd;

//打开修改密码window 情况内容 设置url
function updatepassword(){
	winupdatepd.window('open');
	formupdatepd.form('clear');
	document.getElementById("username").value='${sessionScope.SESSION_DATA.userName}';
	formupdatepd.url = '${pageContext.request.contextPath}/userInfo/updatepassword';
}

//设置修改密码提交方法 显示返回消息
function uppd() {
	formupdatepd.form('submit', {
		url : formupdatepd.url,
		success : function(data) {
			eval('data=' + data);
			if (data.dealFlag) {
				winupdatepd.window('close');
				$.messager.show({
					title : '提示',
					msg : data.msg
				});
			} else {
				{
					$.messager.show({
						title : '提示',
						msg : data.msg,
						showType: 'slide',
				        timeout: 2000
					});
				}
			}
		}
	});
}
//重写easyui 验证方法 新增密码确认认证
$.extend($.fn.validatebox.defaults.rules, {
	equals: {
		validator: function(value,param){
			return value == $(param[0]).val();
		},
	message: '两次密码不匹配'
	}
});

//关闭窗体方法
function closeWindow() {
	winupdatepd.window('close');
}
</script>
</head>
<body>
<a onclick="updatepassword()">修改密码</a>

<!-- 修改密码div -->
<!-- class="easyui-window"  -->
<div id="updatepd-window" title="修改密码" class="easyui-dialog"
		style="width: 420px; height: 300px;">
		<div style="padding: 20px 20px 40px 80px;">
			<form method="post">
				<table>
					<tr>
						<td>账号：</td>
						<td><input id="username" style=" min-height: 20px;" class="white" readonly="readonly" ></input></td>
					</tr>
					<tr>
						<td>旧密码：</td>
						<td><input  id="oldpwd" class="white" name="oldpassword" type="password"></input></td>
					</tr>
					<tr>
						<td>新密码：</td>
						<td><input id="newpwd" class="white" name="newpassword" type="password"></td>
					</tr>
					<tr>
						<td>密码确认：</td>
    					<td><input id="renewpwd" class="white" type="password"  validType="equals['#newpwd']"></td>
					</tr>
				</table>
			</form>
		</div>
		<div style="text-align: center; padding: 5px;">
			<a href="javascript:void(0)" onclick="uppd()" id="btn-save"
				icon="icon-save">保存</a> <a href="javascript:void(0)"
				onclick="closeWindow()" id="btn-cancel" icon="icon-cancel">取消</a>
		</div>
	</div>
</body>
</html>
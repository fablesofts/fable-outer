<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<%@ include file="/commons/meta.jsp"%>
<html>
<head>

</head>
<body>

<!-- 修改密码div -->
<div style="padding: 5px 10px 10px 0px;">
		<form id = "modifyPwdForm" method="post" >
			<table>
				<tr>
					<td style="text-align: right;">用户名：</td>
					<td><input id="usernameTooltip" name="usernameTooltip" style=" min-height: 20px;" class="white" readonly="readonly" value="${sessionScope.SESSION_DATA.userName}"/></td>
				</tr>
				<tr>
					<td style="text-align: right;">密码：</td>
					<td><input id="oldpwdTooltip" name="oldpwdTooltip" class="white" name="oldpasswordTooltip" type="password"></input></td>
				</tr>
				<tr>
					<td style="text-align: right;">新密码：</td>
					<td><input id="newpwdTooltip" name="newpwdTooltip" class="white" name="newpasswordTooltip" type="password"></td>
				</tr>
				<tr>
					<td style="text-align: right;">确认密码：</td>
   					<td><input id="renewpwdTooltip" name="renewpwdTooltip" class="white" type="password"  validType="equals['#newpwdTooltip']"></td>
				</tr>
			</table>
		</form>
		<div style="text-align: center; padding: 5px;">
			<a href="javascript:void(0)" onclick="modifyPwd()" id="btn-save-modifypwd"
				icon="icon-save">保存</a> <a href="javascript:void(0)"
				onclick="closeTooltip()" id="btn-cancel-modifypwd" icon="icon-cancel">取消</a>
		</div>
	</div>
	<style type="text/css">
/* 重写 easyui的验证样式*/
.white{
	   background:  #ffffff;
	   border: 1px solid #7EBDD9;
	   min-height: 20px;
	   width:150px;
}
</style>
<script type="text/javascript">

//重写easyui 验证方法 新增密码确认认证
$.extend($.fn.validatebox.defaults.rules, {
	equals: {
		validator: function(value,param){
			return value == $(param[0]).val();
		},
	message: '两次密码不匹配'
	}
});
//设置验证信息
$(function() {
	$('#btn-save-modifypwd,#btn-cancel-modifypwd').linkbutton();
	$('#oldpwdTooltip').validatebox({
		required : true,   
		missingMessage : '请输入旧密码!'
	});

	$('#newpwdTooltip').validatebox({
		required : true,
		missingMessage : '请输入新密码!'
	});
	$('#renewpwdTooltip').validatebox({
		required : true,
		missingMessage : '请再次输入新密码!'
	});
});

//设置修改密码提交方法 显示返回消息
function modifyPwd() {
	$('#modifyPwdForm').form('submit', {
		url : '${pageContext.request.contextPath}/userInfo/updatepassword',
		success : function(data) {
			eval('data=' + data);
			if (data.dealFlag) {
				closeTooltip();
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

//关闭窗体方法
function closeTooltip() {
	$('#modifyPwdForm').form('clear');
	$('#usernameTooltip').val("${sessionScope.SESSION_DATA.userName}");
	$('#modifyPwd_tooltip').tooltip('hide');
}
</script>
</body>
</html>
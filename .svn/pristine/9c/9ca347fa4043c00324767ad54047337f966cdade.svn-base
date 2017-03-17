package com.fable.outer.controller.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fable.dsp.common.constants.CommonConstants;
import com.fable.dsp.common.util.UtilMD5;
import com.fable.dsp.core.session.SessionData;
import com.fable.outer.dmo.system.resource.MenuInfo;
import com.fable.outer.dmo.system.resource.UserInfo;
import com.fable.outer.dto.MenuInfoDto;
import com.fable.outer.service.system.resource.intf.MenuInfoService;
import com.fable.outer.service.system.resource.intf.RoleInfoService;
import com.fable.outer.service.system.resource.intf.UserInfoService;

/**
 * 系统控制
 * 
 * @author 汪朝
 * 
 */
@Controller
@RequestMapping("/system")
public class SystemController {

	@Autowired
	MenuInfoService menuInfoService;
	@Autowired
	RoleInfoService roleInfoService;
	@Autowired
	UserInfoService userInfoService;

	/**
	 * @author 马健原
	 * @param request
	 *            获取密码
	 * @return
	 */
	@ResponseBody
	@RequestMapping("updatePassword")
	public Map<String, Object> updatePassword(HttpServletRequest request) {
		UserInfo userInfo = null;
		boolean flag = false;
		String msg = "修改密码失败";
		Map<String, Object> rt = new HashMap<String, Object>();
		String newPassword = request.getParameter("sysNewPassword");
		String oldPassword = request.getParameter("sysOldPassword");
		if (newPassword.equals(oldPassword)) {
			msg = "新密码和旧密码不能重复";
			rt.put("flag", flag);
			rt.put("msg", msg);
		} else {
			try {
				SessionData user = (SessionData) request.getSession()
						.getAttribute(CommonConstants.SESSION_DATA);
				userInfo = userInfoService.findUserInfoById(Long.valueOf(user
						.getUserId().trim()));
				if (!userInfo.getPassword().equals(
						UtilMD5.String2Md5(oldPassword))) {
					msg = "旧密码输入不正确";
					return rt;
				}
				userInfo.setPassword(UtilMD5.String2Md5(newPassword));
				userInfoService.update(userInfo);
				flag = true;
				msg = "修改成功";
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} finally {
				rt.put("flag", flag);
				rt.put("msg", msg);
			}
		}
		return rt;

	}

	/**
	 * 查询菜单信息
	 * 
	 * @return 返回菜单列表
	 * @throws Exception
	 */
	@RequestMapping("findMenuInfoList")
	@ResponseBody
	public List<MenuInfo> findMenuInfoList() throws Exception {
		List<MenuInfo> list = this.menuInfoService.findMenuInfoList();
		return list;
	}

	/**
	 * 获取用户可以访问的菜单列表
	 * 
	 * @param session
	 *            会话
	 * @return 返回用户可以访问的菜单列表
	 */
	@RequestMapping("findMenuInfoListByRoleId")
	@ResponseBody
	public List<MenuInfoDto> findMenuInfoListByRoleId(long roleId) {
		// 从Session中获取登陆用户的角色ID，然后根据角色ID查询出该用户可以访问的菜单列表
		return this.menuInfoService.findMenuInfoListByRoleId(roleId);

	}

}

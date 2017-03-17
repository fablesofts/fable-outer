package com.fable.outer.controller.system;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fable.dsp.common.constants.CommonConstants;
import com.fable.dsp.common.util.UtilMD5;
import com.fable.dsp.core.datagrid.DataGridModel;
import com.fable.dsp.core.page.Page;
import com.fable.dsp.core.page.PageList;
import com.fable.dsp.core.session.SessionData;
import com.fable.outer.dmo.system.resource.RoleInfo;
import com.fable.outer.dmo.system.resource.UserInfo;
import com.fable.outer.service.system.resource.intf.RoleInfoService;
import com.fable.outer.service.system.resource.intf.UserInfoService;

/**
 * 用户信息.
 */
@Controller
@RequestMapping("/userInfo")
public class UserInfoController extends UtilMD5 {

	/**
	 * 修改成功返回信息.
	 */
	private static final String COL_UPDATE_SUCCESS = "修改成功";
	/**
	 * 修改失败返回信息.
	 */
	private static final String COL_UPDATE_FAILURE = "更新失败";
	/**
	 * 密码错误提示信息.
	 */
	private static final String COL_PASSWORD_ERROR = "密码错误";

	@Autowired
	UserInfoService userInfoService;
	@Autowired
	RoleInfoService roleInfoService;

	/**
	 * @return 返回用户管理首页 地址
	 */
	@RequestMapping("/maintain")
	public String maintain() {
		return "userinfo/userinfo-maintain";
	}

	/**
	 * @return 返回修改密码功能 地址
	 */
	@RequestMapping("/updatepasswordmaintain")
	public String updatepasswordmaintain() {
		return "userinfo/updatepassword-maintain";
	}

	/**
	 * @return 主页修改密码
	 */
	@RequestMapping("/href/updatepassword")
	public String updatepassword() {
		return "userinfo/updatepassword";
	}

	/**
	 * 分页查询用户信息
	 * 
	 * @param dgm
	 *            分页模型实体
	 * @param userInfo
	 *            用户参数
	 * @return 返回一个分页后的用户对象集合
	 * @throws Exception
	 *             查询用户列表发生异常
	 */
	@RequestMapping("findUserInfoList")
	@ResponseBody
	public Map<String, Object> findUserInfoList(final DataGridModel dgm,
			final UserInfo userInfo) throws Exception {
		// 分页属性
		final Page pager = new Page();

		// 当前页,对应分页中的pageNumber参数;
		final int currentPage = (dgm.getPage() == 0) ? 1 : dgm.getPage();
		// 每页显示条数,对应EASYUI分页中的pageSize参数;
		final int pageSize = dgm.getRows() == 0 ? CommonConstants.COL_PAGESIZE
				: dgm.getRows();
		// 每页的开始记录 第一页为1 第二页为number +1
		final int index = (currentPage - 1) * pageSize;
		// 设置分页查询参数
		pager.setCurrentPage(currentPage);
		pager.setPageSize(pageSize);
		pager.setIndex(index);
		// 查询分页结果
		final PageList<UserInfo> pageUserInfo = this.userInfoService
				.findPageUserInfo(pager, new UserInfo());
		/**
		 * json数据容器
		 */
		final Map<String, Object> rootJson = new HashMap<String, Object>();

		rootJson.put("rows", pageUserInfo.getList());
		rootJson.put("total", pageUserInfo.getPage().getCount());
		return rootJson;

	}

	/**
	 * 添加用户
	 * 
	 * @param userInfo
	 *            需要添加的用户实体
	 * @param roleId
	 *            用户实体对应的权限id
	 * @param session
	 *            会话
	 * @return 添加成功或失败信息
	 */
	@RequestMapping("addUserInfo")
	@ResponseBody
	public Map<String, Object> addUserInfo(final UserInfo userInfo,
			final Long roleId, final HttpSession session) {
		// json数据容器
		final Map<String, Object> rootJson = new HashMap<String, Object>();
		try {
			// 检查用户名是否重复，如果重复则不能添加，否则可以添加
			UserInfo ui = this.userInfoService.findUserInfoByName(userInfo
					.getUsername());
			if (ui != null) {
				rootJson.put(CommonConstants.COL_DEALFLAG, false);
				rootJson.put(CommonConstants.COL_MSG, "添加用户失败，用户名已经存在");
				return rootJson;
			}
			final List<RoleInfo> list = new ArrayList<RoleInfo>();
			final RoleInfo ri = this.roleInfoService.getById(roleId);
			list.add(ri);
			userInfo.setRoles(list);
			userInfo.setDelFlag(CommonConstants.DEL_FLAG_NO);
			final SessionData user = (SessionData) session
					.getAttribute(CommonConstants.SESSION_DATA);
			userInfo.setCreateUser(user.getUserId());
			userInfo.setCreateTime(new Date());
			this.userInfoService.insert(userInfo);
			rootJson.put(CommonConstants.COL_DEALFLAG, true);
			rootJson.put(CommonConstants.COL_MSG, "添加用户成功");
		} catch (final Exception e) {
			e.printStackTrace();
			rootJson.put(CommonConstants.COL_DEALFLAG, false);
			rootJson.put(CommonConstants.COL_MSG, "添加用户失败");
		}
		return rootJson;
	}

	/**
	 * 判断用户是否重名.
	 * 
	 * @param userInfo
	 *            用户信息
	 * @return 用户是否重名
	 */
	@RequestMapping("isSameName")
	@ResponseBody
	public Map<String, Object> isSameName(String username) {
		final Map<String, Object> rootJson = new HashMap<String, Object>();
		UserInfo ui = new UserInfo();
		ui = this.userInfoService.findUserInfoByName(username);
		if (ui != null)
			rootJson.put(CommonConstants.COL_DEALFLAG, false);
		else
			rootJson.put(CommonConstants.COL_DEALFLAG, true);
		return rootJson;
	}

	/**
	 * @param userInfo
	 *            需要修改的用户实体
	 * @param roleId
	 *            需要修改的角色id
	 * @param session
	 *            会话
	 * @return 修改成功或失败信息
	 */
	@RequestMapping("updateUserInfo")
	@ResponseBody
	public Map<String, Object> updateUserInfo(final UserInfo userInfo,
			final Long roleId, final HttpSession session) {
		/**
		 * json数据容器
		 */
		final Map<String, Object> rootJson = new HashMap<String, Object>();
		try {
			final List<RoleInfo> list = new ArrayList<RoleInfo>();
			final RoleInfo ri = this.roleInfoService.getById(roleId);
			list.add(ri);
			final UserInfo ui = this.userInfoService.getById(userInfo.getId());
			userInfo.setPassword(ui.getPassword());
			userInfo.setRoles(list);
			userInfo.setDelFlag(CommonConstants.DEL_FLAG_NO);
			final SessionData user = (SessionData) session
					.getAttribute(CommonConstants.SESSION_DATA);
			userInfo.setUpdateUser(user.getUserId());
			userInfo.setUpdateTime(new Date());
			this.userInfoService.merge(userInfo);
			rootJson.put(CommonConstants.COL_DEALFLAG, true);
			rootJson.put(CommonConstants.COL_MSG,
					UserInfoController.COL_UPDATE_SUCCESS);
		} catch (final Exception e) {
			e.printStackTrace();
			rootJson.put(CommonConstants.COL_DEALFLAG, false);
			rootJson.put(CommonConstants.COL_MSG,
					UserInfoController.COL_UPDATE_FAILURE);
		}
		return rootJson;
	}

	/**
	 * @param userInfo
	 *            需要删除的用户实体
	 * @return 删除成功或失败信息
	 */
	@RequestMapping("deleteUserInfo")
	@ResponseBody
	public Map<String, Object> deleteUserInfo(final UserInfo userInfo) {
		/**
		 * json数据容器
		 */
		final Map<String, Object> rootJson = new HashMap<String, Object>();
		UserInfo us = new UserInfo();
		us = this.userInfoService.getById(userInfo.getId());
		/**
		 * admin 用户为系统超级管理员不能删除
		 */
		if (!"admin".equals(us.getUsername())) {
			us.setDelFlag(CommonConstants.DEL_FLAG_YES);
			try {
				this.userInfoService.deleteById(userInfo.getId());
				rootJson.put(CommonConstants.COL_DEALFLAG, true);
				rootJson.put(CommonConstants.COL_MSG, "删除成功");
			} catch (final Exception e) {
				e.printStackTrace();
				rootJson.put(CommonConstants.COL_DEALFLAG, false);
				rootJson.put(CommonConstants.COL_MSG, "删除失败");
			}
		} else {
			rootJson.put(CommonConstants.COL_DEALFLAG, false);
			rootJson.put(CommonConstants.COL_MSG, "admin用户不能删除");
		}
		return rootJson;
	}

	/**
	 * @return 返回查询所有角色对象用于选择
	 * @throws Exception
	 *             查询用户集合失败
	 */
	@RequestMapping("findRoleInfo")
	@ResponseBody
	public List<RoleInfo> findRoleInfo() throws Exception {
		final List<RoleInfo> li = this.roleInfoService.findRoleInfoList();
		return li;
	}

	/**
	 * @param userInfo
	 *            用户实体对象
	 * @return 返回单个用户对象用于修改回写
	 * @throws Exception
	 *             查询单个用户发生失败
	 */
	@RequestMapping("findUserInfo")
	@ResponseBody
	public UserInfo findUserInfo(final UserInfo userInfo) throws Exception {
		return this.userInfoService.getById(userInfo.getId());
	}

	/**
	 * 修改密码.
	 * 
	 * @param oldpassword
	 *            旧密码
	 * @param newpassword
	 *            新密码
	 * @param session
	 *            session会话
	 * @return 返回修改成功或失败
	 */
	@RequestMapping("updatepassword")
	@ResponseBody
	public Map<String, Object> updatepassword(final String oldpwdTooltip,
			final String newpwdTooltip, final HttpSession session) {
		// json数据容器
		final Map<String, Object> rootJson = new HashMap<String, Object>();
		try {
			UserInfo userInfo = new UserInfo();
			final SessionData user = (SessionData) session
					.getAttribute(CommonConstants.SESSION_DATA);
			userInfo.setUsername(user.getUserName());
			userInfo.setPassword(UtilMD5.String2Md5(oldpwdTooltip));
			userInfo = this.userInfoService.findUserInfoByConditions(userInfo);
			if (userInfo != null) {
				userInfo.setUpdateUser(user.getUserId());
				userInfo.setPassword(UtilMD5.String2Md5(newpwdTooltip));
				userInfo.setUpdateTime(new Date());
				this.userInfoService.update(userInfo);
				rootJson.put(CommonConstants.COL_DEALFLAG, true);
				rootJson.put(CommonConstants.COL_MSG,
						UserInfoController.COL_UPDATE_SUCCESS);
			} else {
				rootJson.put(CommonConstants.COL_DEALFLAG, false);
				rootJson.put(CommonConstants.COL_MSG,
						UserInfoController.COL_PASSWORD_ERROR);
			}
		} catch (final Exception e) {
			e.printStackTrace();
			rootJson.put(CommonConstants.COL_DEALFLAG, false);
			rootJson.put(CommonConstants.COL_MSG,
					UserInfoController.COL_UPDATE_FAILURE);
		}
		return rootJson;
	}

	/**
	 * 管理员修改密码.
	 * 
	 * @param id
	 *            需要修改的用户id
	 * @param usernameforup
	 *            需要修改的帐户
	 * @param newpassword
	 *            新的密码
	 * @param session
	 *            session会话
	 * @return 返回修改密码成功或失败
	 */
	@RequestMapping("updatepasswordbyadmin")
	@ResponseBody
	public Map<String, Object> updatepasswordbyadmin(Long id,
			final String usernameforup, final String newpassword,
			final HttpSession session) {
		/**
		 * json数据容器
		 */
		final Map<String, Object> rootJson = new HashMap<String, Object>();
		try {
			UserInfo userInfo = new UserInfo();
			userInfo.setUsername(usernameforup);
			userInfo = this.userInfoService.findUserInfoByConditions(userInfo);
			if (userInfo != null) {
				final SessionData user = (SessionData) session
						.getAttribute(CommonConstants.SESSION_DATA);
				userInfo.setPassword(UtilMD5.String2Md5(newpassword));
				userInfo.setUpdateUser(user.getUserId());
				userInfo.setUpdateTime(new Date());
				this.userInfoService.update(userInfo);
				rootJson.put(CommonConstants.COL_DEALFLAG, true);
				rootJson.put(CommonConstants.COL_MSG,
						UserInfoController.COL_UPDATE_SUCCESS);
			}
		} catch (final Exception e) {
			e.printStackTrace();
			rootJson.put("dealFlag", false);
			rootJson.put("msg", "修改失败");
		}
		return rootJson;
	}

	/**
	 * 检查当前登陆的用户是否有对角色为超级管理员的用户进行编辑、删除和修改密码的权限
	 * 
	 * @param session
	 *            会话
	 * @param id
	 *            要修改的用户ID
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("checkAuth")
	@ResponseBody
	private boolean checkAuth(final HttpSession session, final Long id)
			throws Exception {
		try {
			// 获取当前登陆的用户的角色名
			final SessionData user = (SessionData) session
					.getAttribute(CommonConstants.SESSION_DATA);
			String roleName = user.getRoleName();
			if (roleName == null || roleName.isEmpty()) {
				return false;
			}
			// 如果当前登陆的用户角色为超级管理员，则可以进行任何操作
			if ("超级管理员".equals(roleName.trim())) {
				return true;
			}
			// 获取要修改的用户的角色名
			UserInfo userInfo = this.userInfoService.getById(id);
			String rName = userInfo.getRoles().get(0).getRoleName();
			// 如果当前登陆的用户角色为管理员，而修改的用户角色不是超级管理员，则可以进行任何操作
			// 否则，不能进行以上操作
			// 如果当前登陆的用户角色不是超级管理员或管理员，则不能进行以上操作
			if ("管理员".equals(roleName.trim())) {
				if ("超级管理员".equals(rName.trim())) {
					return false;
				} else {
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	/************************************ 表单域 ************************************/

}

package com.fable.outer.controller.system;

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
import com.fable.dsp.core.datagrid.DataGridModel;
import com.fable.dsp.core.page.Page;
import com.fable.dsp.core.page.PageList;
import com.fable.dsp.core.session.SessionData;
import com.fable.outer.dmo.system.resource.MenuInfo;
import com.fable.outer.dmo.system.resource.RoleInfo;
import com.fable.outer.dmo.system.resource.UserInfo;
import com.fable.outer.security.FableSecurityMetadataSource;
import com.fable.outer.service.system.resource.intf.MenuInfoService;
import com.fable.outer.service.system.resource.intf.RoleInfoService;
import com.fable.outer.service.system.resource.intf.UserInfoService;

/**
 * 角色信息
 * 
 * @author liuz
 * 
 */
@Controller
@RequestMapping("/roleInfo")
public class RoleInfoController {

	@Autowired
	RoleInfoService roleInfoService;
	@Autowired
	UserInfoService userInfoService;
	@Autowired
	MenuInfoService menuInfoService;

	// @Autowired
	// FableSecurityMetadataSource fableSecurityMetadataSource;

	/**
	 * @return 返回角色管理界面地址
	 */
	@RequestMapping("/maintain")
	public String maintain() {
		return "roleInfo/roleInfo-maintain";
	}

	/**
	 * 分页查询角色信息
	 * 
	 * @param dgm
	 *            分页模型对象
	 * @param roleInfo
	 *            角色参数
	 * @return 返回一个分页后的角色信息集合
	 * @throws Exception
	 *             查询角色信息出现异常
	 */
	@RequestMapping("findRoleInfoList")
	@ResponseBody
	public Map<String, Object> findUsersInfoList(final DataGridModel dgm,
			final RoleInfo roleInfo) throws Exception {
		// 分页属性
		final Page pager = new Page();
		// 当前页,对应分页中的pageNumber参数;
		final int currentPage = (dgm.getPage() == 0) ? 1 : dgm.getPage();
		// 每页显示条数,对应EASYUI分页中的pageSize参数;
		final int pageSize = (dgm.getRows() == 0) ? CommonConstants.COL_PAGESIZE
				: dgm.getRows();
		// 每页的开始记录 第一页为1 第二页为number +1
		final int index = (currentPage - 1) * pageSize;
		// 设置分页查询参数
		pager.setCurrentPage(currentPage);
		pager.setPageSize(pageSize);
		pager.setIndex(index);
		// 分页查询角色信息
		final PageList<RoleInfo> pageList = this.roleInfoService
				.findRoleInfoByPage(pager, new RoleInfo());
		/**
		 * json数据容器
		 */
		final Map<String, Object> rootJson = new HashMap<String, Object>();
		rootJson.put("rows", pageList.getList());
		rootJson.put("total", pageList.getPage().getCount());
		return rootJson;
	}

	/**
	 * 添加角色
	 * 
	 * @param roleInfo
	 *            要添加的角色信息对象
	 * @param session
	 *            会话
	 * @return 返回添加角色成功或失败信息
	 */
	@RequestMapping("addRoleInfo")
	@ResponseBody
	public Map<String, Object> addRoleInfo(final RoleInfo roleInfo,
			final HttpSession session) {
		// json数据容器
		final Map<String, Object> rootJson = new HashMap<String, Object>();
		try {
			// 首先检查角色名是否已经存在，如果已存在则不能添加该角色，否则可以添加
			RoleInfo ri = this.roleInfoService.getRoleInfoByName(roleInfo
					.getRoleName());
			if (ri != null) {
				// 角色名已经存在，不能添加，向页面返回提示信息
				rootJson.put(CommonConstants.COL_DEALFLAG, false);
				rootJson.put(CommonConstants.COL_MSG, "添加角色失败，角色名已经存在");
				return rootJson;
			}
			// 从session中获取当前用户信息，设置角色创建人和创建时间
			final SessionData user = (SessionData) session
					.getAttribute(CommonConstants.SESSION_DATA);
			roleInfo.setCreateUser(user.getUserId());
			roleInfo.setCreateTime(new Date());
			// 设置删除标识符
			roleInfo.setDelFlag(CommonConstants.DEL_FLAG_NO);
			this.roleInfoService.insert(roleInfo);
			rootJson.put(CommonConstants.COL_DEALFLAG, true);
			rootJson.put(CommonConstants.COL_MSG, "添加角色成功");
		} catch (Exception e) {
			e.printStackTrace();
			rootJson.put(CommonConstants.COL_DEALFLAG, false);
			rootJson.put(CommonConstants.COL_MSG, "添加角色失败");
		}
		return rootJson;
	}

	/**
	 * 修改角色信息
	 * 
	 * @param roleInfo
	 *            要修改的角色对象
	 * @param session
	 *            会话
	 * @return 返回修改角色成功或失败
	 */
	@RequestMapping("updateRoleInfo")
	@ResponseBody
	public Map<String, Object> updateRoleInfo(final RoleInfo roleInfo,
			final HttpSession session) {
		// json数据容器
		Map<String, Object> rootJson = new HashMap<String, Object>();
		try {
			// 根据ID查询出要修改的角色对象
			RoleInfo ri = this.roleInfoService.getById(roleInfo.getId());
			// 设置修改的属性值
			ri.setRoleName(roleInfo.getRoleName());
			ri.setDescription(roleInfo.getDescription());
			// 从session中获取当前用户信息，设置角色修改人和修改时间
			final SessionData user = (SessionData) session
					.getAttribute(CommonConstants.SESSION_DATA);
			ri.setUpdateUser(user.getUserId());
			ri.setUpdateTime(new Date());
			this.roleInfoService.merge(ri);
			rootJson.put(CommonConstants.COL_DEALFLAG, true);
			rootJson.put(CommonConstants.COL_MSG, "修改角色成功");
		} catch (Exception e) {
			e.printStackTrace();
			rootJson.put(CommonConstants.COL_DEALFLAG, true);
			rootJson.put(CommonConstants.COL_MSG, "修改角色失败");
		}
		return rootJson;
	}

	/**
	 * 删除角色
	 * 
	 * @param roleInfo
	 *            要删除的角色对象
	 * @return 返回删除角色成功或失败
	 */
	@RequestMapping("deleteRoleInfo")
	@ResponseBody
	public Map<String, Object> deleteRoleInfo(final RoleInfo roleInfo) {
		// json数据容器
		Map<String, Object> rootJson = new HashMap<String, Object>();
		try {
			// 判断当前是否有用户关联到该角色，如果有的话则不能删除，否则可以删除
			List<UserInfo> users = this.userInfoService
					.findUserInfoByRoleInfo(roleInfo);
			if (users != null && users.size() > 0) {
				// 有用户关联到该角色，不能删除
				rootJson.put(CommonConstants.COL_DEALFLAG, false);
				rootJson.put(CommonConstants.COL_MSG, "删除角色失败，还有用户关联到该角色");
				return rootJson;
			}
			roleInfo.setDelFlag(CommonConstants.DEL_FLAG_YES);
			// 没有用户关联到该角色，可以删除
			this.roleInfoService.deleteById(roleInfo.getId());
			rootJson.put(CommonConstants.COL_DEALFLAG, true);
			rootJson.put(CommonConstants.COL_MSG, "删除角色成功");
		} catch (Exception e) {
			rootJson.put(CommonConstants.COL_DEALFLAG, false);
			rootJson.put(CommonConstants.COL_MSG, "删除角色失败");
		}
		return rootJson;
	}

	/**
	 * 判断角色名是否重复
	 * 
	 * @param roleName
	 *            要验证的角色名
	 * @return 返回验证后的提示信息
	 */
	@RequestMapping("isSameName")
	@ResponseBody
	public Map<String, Object> isSameName(String roleName) {
		// json数据容器
		Map<String, Object> rootJson = new HashMap<String, Object>();
		RoleInfo ri = this.roleInfoService.getRoleInfoByName(roleName);
		// 如果角色名重复则返回false，否则返回true
		if (ri != null)
			rootJson.put(CommonConstants.COL_DEALFLAG, false);
		else
			rootJson.put(CommonConstants.COL_DEALFLAG, true);
		return rootJson;
	}

	/**
	 * 查询一级菜单信息
	 * 
	 * @return 一级菜单列表
	 * @throws Exception
	 */
	@RequestMapping("findMenuInfoList")
	@ResponseBody
	public List<MenuInfo> findMenuInfoList() throws Exception {
		List<MenuInfo> list = this.menuInfoService.findMenuInfoList();
		return list;
	}

	/**
	 * 为角色配置可以访问的菜单
	 * 
	 * @param roleId
	 *            要配置菜单的角色ID
	 * @param menuIds
	 *            菜单ID组成的字符串
	 * @param session
	 *            会话
	 * @return 返回配置成功或失败
	 */
	@RequestMapping("configMenus")
	@ResponseBody
	public Map<String, Object> configMenus(final RoleInfo roleInfo,
			String menuIds, final HttpSession session) {
		// json数据容器
		Map<String, Object> rootJson = new HashMap<String, Object>();
		try {
			// 为角色配置菜单
			RoleInfo ri = this.roleInfoService.configMenus(roleInfo, menuIds);
			// 从session中获取当前用户信息，设置角色修改人和修改时间
			final SessionData user = (SessionData) session
					.getAttribute(CommonConstants.SESSION_DATA);
			ri.setUpdateUser(user.getUserId());
			ri.setUpdateTime(new Date());
			this.roleInfoService.merge(ri);
			// 配置菜单后，url和角色之间的对应关系发生了变化，此时需要把resourceMap设置为null，
			// 这样再次发送请求时就会重新加载URL与角色之间的对应关系
			FableSecurityMetadataSource.setResourceMap(null);
			rootJson.put(CommonConstants.COL_DEALFLAG, true);
			rootJson.put(CommonConstants.COL_MSG, "配置菜单成功");
		} catch (Exception e) {
			rootJson.put(CommonConstants.COL_DEALFLAG, true);
			rootJson.put(CommonConstants.COL_MSG, "配置菜单失败");
		}
		return rootJson;
	}

}

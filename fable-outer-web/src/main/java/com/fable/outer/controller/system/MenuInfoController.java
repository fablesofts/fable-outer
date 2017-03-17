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
import com.fable.outer.dmo.system.resource.ResourceInfo;
import com.fable.outer.dmo.system.resource.RoleInfo;
import com.fable.outer.security.FableSecurityMetadataSource;
import com.fable.outer.service.system.resource.intf.MenuInfoService;
import com.fable.outer.service.system.resource.intf.ResourceInfoService;
import com.fable.outer.service.system.resource.intf.RoleInfoService;

/**
 * 菜单
 * 
 * @author liuz
 * 
 */
@Controller
@RequestMapping("/menuInfo")
public class MenuInfoController {

	@Autowired
	MenuInfoService menuInfoService;
	@Autowired
	RoleInfoService roleInfoService;
	@Autowired
	ResourceInfoService resourceInfoService;

	/**
	 * @return 返回菜单管理界面地址
	 */
	@RequestMapping("/maintain")
	public String maintain() {
		return "menuInfo/menuInfo-maintain";
	}

	/**
	 * 查询一级菜单信息
	 * 
	 * @return 返回一级菜单列表
	 * @throws Exception
	 */
	@RequestMapping("findMenuInfoList")
	@ResponseBody
	public List<MenuInfo> findMenuInfoList() throws Exception {
		List<MenuInfo> list = this.menuInfoService.findMenuInfoList();
		return list;
	}

	/**
	 * 添加菜单
	 * 
	 * @param menuInfo
	 *            要添加的菜单对象
	 * @param PID
	 *            父菜单ID
	 * @param session
	 *            会话
	 * @return 返回添加成功或失败
	 * @throws Exception
	 */
	@RequestMapping("addMenuInfo")
	@ResponseBody
	public Map<String, Object> addMenuInfo(final MenuInfo menuInfo,
			final String PID, final HttpSession session) throws Exception {
		// json数据容器
		final Map<String, Object> rootJson = new HashMap<String, Object>();
		try {
			// 首先检查菜单名是否已经存在，如果存在则不能添加，否则可以添加菜单
			MenuInfo mi = this.menuInfoService.getMenuInfoByName(menuInfo
					.getMenuName());
			if (mi != null) {
				// 菜单名已经存在，不能添加，向页面返回提示信息
				rootJson.put(CommonConstants.COL_DEALFLAG, false);
				rootJson.put(CommonConstants.COL_MSG, "添加菜单失败，菜单名已经存在");
				return rootJson;
			}
			// 设置要添加的菜单的父菜单ID及菜单级别
			if (PID != null && !"".equals(PID)) {
				MenuInfo parentMenu = this.menuInfoService.getById(Long
						.valueOf(PID));
				int menuLevel = parentMenu.getMenuLevel();
				if (menuLevel >= CommonConstants.MENU_MAX_LEVEL) {
					rootJson.put(CommonConstants.COL_DEALFLAG, false);
					rootJson.put(CommonConstants.COL_MSG,
							"添加菜单失败，父菜单已是最低级菜单，不能再添加子菜单");
					return rootJson;
				}
				menuInfo.setParentMenu(parentMenu);
				menuInfo.setMenuLevel(menuLevel + 1);
			}
			// 从session中获取当前用户信息，设置菜单创建人和创建时间
			final SessionData user = (SessionData) session
					.getAttribute(CommonConstants.SESSION_DATA);
			menuInfo.setCreateUser(user.getUserId());
			menuInfo.setCreateTime(new Date());
			// 设置删除标识符
			menuInfo.setDelFlag(CommonConstants.DEL_FLAG_NO);
			Integer sortOrder = menuInfo.getSortOrder();
			if (sortOrder == null || "".equals(sortOrder.toString()))
				menuInfo.setSortOrder(CommonConstants.MENU_SORT_ORDER);
			this.menuInfoService.insert(menuInfo);
			rootJson.put(CommonConstants.COL_DEALFLAG, true);
			rootJson.put(CommonConstants.COL_MSG, "添加菜单成功");
		} catch (Exception e) {
			e.printStackTrace();
			rootJson.put(CommonConstants.COL_DEALFLAG, false);
			rootJson.put(CommonConstants.COL_MSG, "添加菜单失败");
		}
		return rootJson;
	}

	/**
	 * 修改菜单信息
	 * 
	 * @param menuInfo
	 *            要修改的菜单对象
	 * @param PID
	 *            父菜单ID
	 * @param session
	 *            会话
	 * @return 返回修改成功或失败
	 * @throws Exception
	 */
	@RequestMapping("updateMenuInfo")
	@ResponseBody
	public Map<String, Object> updateMenuInfo(final MenuInfo menuInfo,
			final String PID, final HttpSession session) throws Exception {
		// json数据容器
		final Map<String, Object> rootJson = new HashMap<String, Object>();
		try {
			// 根据ID查询要修改的菜单对象
			final MenuInfo mi = this.menuInfoService.getById(menuInfo.getId());
			// 设置要修改的菜单的父菜单，及其菜单级别
			if (PID != null && !"".equals(PID)) {
				MenuInfo parentMenu = this.menuInfoService.getById(Long
						.valueOf(PID));
				int menuLevel = parentMenu.getMenuLevel();
				// 修改菜单及其子菜单的菜单级别，只有menuLevel为1或2时，可以为父菜单
				if (menuLevel >= CommonConstants.MENU_MAX_LEVEL) {
					rootJson.put(CommonConstants.COL_DEALFLAG, false);
					rootJson.put(CommonConstants.COL_MSG,
							"修改菜单失败，父菜单已是最低级菜单，不能再添加子菜单");
					return rootJson;
				}
				if (mi.getMenuLevel() == 2) {
					if (mi.getChildMenus().isEmpty()) {
						mi.setParentMenu(parentMenu);
						mi.setMenuLevel(menuLevel + 1);
					} else if (menuLevel == 2) {
						rootJson.put(CommonConstants.COL_DEALFLAG, false);
						rootJson.put(CommonConstants.COL_MSG,
								"修改菜单失败，菜单级别已经超出最大级别");
						return rootJson;
					} else {
						mi.setParentMenu(parentMenu);
						mi.setMenuLevel(menuLevel + 1);
					}
				} else if (mi.getMenuLevel() == 3) {
					mi.setParentMenu(parentMenu);
					mi.setMenuLevel(menuLevel + 1);
				}
			}
			// 从session中获取当前用户信息，设置菜单修改人和修改时间
			final SessionData user = (SessionData) session
					.getAttribute(CommonConstants.SESSION_DATA);
			mi.setUpdateUser(user.getUserId());
			mi.setUpdateTime(new Date());
			// 设置修改的属性
			mi.setMenuName(menuInfo.getMenuName());
			mi.setUrl(menuInfo.getUrl());
			mi.setIconUrl(menuInfo.getIconUrl());
			Integer sortOrder = menuInfo.getSortOrder();
			if (sortOrder == null || "".equals(sortOrder.toString()))
				mi.setSortOrder(CommonConstants.MENU_SORT_ORDER);
			else
				mi.setSortOrder(menuInfo.getSortOrder());
			mi.setDescription(menuInfo.getDescription());
			this.menuInfoService.update(mi);
			rootJson.put(CommonConstants.COL_DEALFLAG, true);
			rootJson.put(CommonConstants.COL_MSG, "修改菜单成功");
		} catch (Exception e) {
			e.printStackTrace();
			rootJson.put(CommonConstants.COL_DEALFLAG, false);
			rootJson.put(CommonConstants.COL_MSG, "修改菜单失败");
		}
		return rootJson;
	}

	/**
	 * 删除菜单
	 * 
	 * @param menuInfo
	 *            要删除的菜单对象
	 * @return 返回删除菜单成功或失败
	 */
	@RequestMapping("deleteMenuInfo")
	@ResponseBody
	public Map<String, Object> deleteMenuInfo(final MenuInfo menuInfo,
			final HttpSession session) {
		// json数据容器
		Map<String, Object> rootJson = new HashMap<String, Object>();
		try {
			// 判断要删除的菜单是否有子菜单，如果有子菜单则不能删除
			MenuInfo mi = this.menuInfoService.getById(menuInfo.getId());
			List<MenuInfo> childMenus = mi.getChildMenus();
			if (childMenus != null && !childMenus.isEmpty()) {
				rootJson.put(CommonConstants.COL_DEALFLAG, false);
				rootJson.put(CommonConstants.COL_MSG, "删除菜单失败，该菜单还包含有子菜单");
				return rootJson;
			}
			// 判断当前是否有角色关联到该菜单，如果有的话则不能删除
			List<RoleInfo> roles = this.roleInfoService
					.findRoleInfoByMenuInfo(menuInfo);
			if (roles != null && roles.size() > 0) {
				// 有角色关联到该菜单，不能删除
				rootJson.put(CommonConstants.COL_DEALFLAG, false);
				rootJson.put(CommonConstants.COL_MSG, "删除菜单失败，还有角色关联到该菜单");
				return rootJson;
			}
			// 判断要删除的菜单是否是最高级菜单，如果是则不能删除，否则可以删除该菜单
			MenuInfo parentMenu = mi.getParentMenu();
			// 如果父菜单为最高级菜单，则不能删除
			if (parentMenu == null) {
				rootJson.put(CommonConstants.COL_DEALFLAG, false);
				rootJson.put(CommonConstants.COL_MSG, "删除菜单失败，不能删除最高级菜单");
				return rootJson;
			}
			// // 从session中获取用户信息，设置菜单的修改人ID和修改时间
			// SessionData user = (SessionData) session
			// .getAttribute(CommonConstants.SESSION_DATA);
			// parentMenu.setUpdateUser(user.getUserId());
			// parentMenu.setUpdateTime(new Date());
			// this.menuInfoService.update(parentMenu);
			// 设置要删除菜单的父菜单为null，即解除该菜单与父菜单的父子关系，然后更新要删除的菜单，最后再删除
			mi.setParentMenu(null);
			this.menuInfoService.update(mi);
			this.menuInfoService.deleteById(menuInfo.getId());
			rootJson.put(CommonConstants.COL_DEALFLAG, true);
			rootJson.put(CommonConstants.COL_MSG, "删除菜单成功");
		} catch (Exception e) {
			e.printStackTrace();
			rootJson.put(CommonConstants.COL_DEALFLAG, false);
			rootJson.put(CommonConstants.COL_MSG, "删除菜单失败");
		}
		return rootJson;
	}

	/**
	 * 判断菜单名是否重复
	 * 
	 * @param menuName
	 *            要验证的菜单名
	 * @param menuId
	 *            要修改菜单的ID
	 * @return 返回验证后的提示信息
	 */
	@RequestMapping("isSameMenuName")
	@ResponseBody
	public Map<String, Object> isSameMenuName(String menuName, Long menuId) {
		// json数据容器
		Map<String, Object> rootJson = new HashMap<String, Object>();
		MenuInfo mi = this.menuInfoService.getMenuInfoByName(menuName);
		// 如果菜单名重复则返回false，否则返回true
		if (mi != null && !mi.getId().equals(menuId))
			rootJson.put(CommonConstants.COL_DEALFLAG, false);
		else
			rootJson.put(CommonConstants.COL_DEALFLAG, true);
		return rootJson;
	}

	/******************* 下面的方法处理资源的相关请求 **************************/

	/**
	 * 分页查询指定菜单关联的资源信息
	 * 
	 * @param menuId
	 *            菜单ID
	 * @param dgm
	 *            数据表格模型对象
	 * @return 返回查询到的资源信息及总记录数
	 */
	@RequestMapping("findResourceInfoList")
	@ResponseBody
	public Map<String, Object> findResourceInfoList(final DataGridModel dgm,
			final Long menuId) {
		// 设置分页参数
		final Page pager = new Page();
		// 获取当前页，对应datagrid中的pageNumber参数
		final int currentPage = (dgm.getPage() == 0) ? 1 : dgm.getPage();
		// 获取页容量，对应datagrid中的pageSize参数
		final int pageSize = (dgm.getRows() == 0) ? CommonConstants.COL_PAGESIZE
				: dgm.getRows();
		// 每页的开始记录 第一页为1 第二页为number +1
		final int index = (currentPage - 1) * pageSize;
		// 设置分页查询参数
		pager.setCurrentPage(currentPage);
		pager.setPageSize(pageSize);
		pager.setIndex(index);
		// 分页查询资源数据
		PageList<ResourceInfo> pageList = this.resourceInfoService
				.findResourceInfoByPage(pager, menuId);
		// json数据容器
		final Map<String, Object> rootJson = new HashMap<String, Object>();
		rootJson.put("rows", pageList.getList());
		rootJson.put("total", pageList.getPage().getCount());
		return rootJson;
	}

	/**
	 * 新增资源
	 * 
	 * @param resourceInfo
	 *            新增的资源对象
	 * @param menuId
	 *            当前菜单的ID
	 * @param session
	 *            会话
	 * @return 返回添加资源成功或失败
	 */
	@RequestMapping("addResourceInfo")
	@ResponseBody
	public Map<String, Object> addResourceInfo(final ResourceInfo resourceInfo,
			final Long menuId, final HttpSession session) {
		// json容器
		Map<String, Object> rootJson = new HashMap<String, Object>();
		try {
			// 首先检查资源名是否重复，如果重复则不能添加该资源，否则可以添加
			ResourceInfo ri = this.resourceInfoService
					.getResourceInfoByName(resourceInfo.getResourceName());
			if (ri != null) {
				// 资源名重复，不能添加
				rootJson.put(CommonConstants.COL_DEALFLAG, false);
				rootJson.put(CommonConstants.COL_MSG, "添加资源失败，资源名已经存在");
				return rootJson;
			}
			// 设置资源的其他属性值，添加资源
			// 从session中获取用户信息，设置资源的添加人ID和添加时间
			SessionData user = (SessionData) session
					.getAttribute(CommonConstants.SESSION_DATA);
			resourceInfo.setCreateUser(user.getUserId());
			resourceInfo.setCreateTime(new Date());
			// 设置删除标识符
			resourceInfo.setDelFlag(CommonConstants.DEL_FLAG_NO);
			this.resourceInfoService.insert(resourceInfo);
			// 维护当前菜单关联的资源，即为当前菜单添加新增的资源
			// 先根据菜单ID查询出菜单对象，然后为其配置新增的资源
			MenuInfo menuInfo = this.menuInfoService.getById(menuId);
			List<ResourceInfo> resources = menuInfo.getResources();
			resources.add(resourceInfo);
			menuInfo.setResources(resources);
			this.menuInfoService.update(menuInfo);
			// 为菜单新增资源后，url与角色之间的对应关系发生变化，此时需要把resourceMap设置为null，
			// 这样再次发送请求时就会重新加载URL与角色之间的对应关系
			FableSecurityMetadataSource.setResourceMap(null);
			rootJson.put(CommonConstants.COL_DEALFLAG, true);
			rootJson.put(CommonConstants.COL_MSG, "添加资源成功");
		} catch (Exception e) {
			e.printStackTrace();
			rootJson.put(CommonConstants.COL_DEALFLAG, false);
			rootJson.put(CommonConstants.COL_MSG, "添加资源失败");
		}
		return rootJson;
	}

	/**
	 * 修改资源信息
	 * 
	 * @param resourceInfo
	 *            资源对象
	 * @param session
	 *            会话
	 * @return 返回修改成功或失败
	 */
	@RequestMapping("updateResourceInfo")
	@ResponseBody
	public Map<String, Object> updateResourceInfo(
			final ResourceInfo resourceInfo, final HttpSession session) {
		// json容器
		Map<String, Object> rootJson = new HashMap<String, Object>();
		try {
			// 先查询出要修改的资源，然后设置要修改的属性值
			final ResourceInfo ri = this.resourceInfoService
					.getById(resourceInfo.getId());
			String oldUrl = ri.getUrl();// 获取修改之前资源的url
			ri.setResourceName(resourceInfo.getResourceName());
			ri.setUrl(resourceInfo.getUrl());
			ri.setType(resourceInfo.getType());
			ri.setDescription(resourceInfo.getDescription());
			// 从session中获取用户信息，设置修改人和修改时间
			SessionData user = (SessionData) session
					.getAttribute(CommonConstants.SESSION_DATA);
			ri.setUpdateUser(user.getUserId());
			ri.setUpdateTime(new Date());
			this.resourceInfoService.update(ri);
			// 如果修改了资源信息的url，则url与角色之间的对应关系发生变化，此时需要把resourceMap设置为null，
			// 这样再次发送请求时就会重新加载URL与角色之间的对应关系
			if (!oldUrl.equals(resourceInfo.getUrl()))
				FableSecurityMetadataSource.setResourceMap(null);
			rootJson.put(CommonConstants.COL_DEALFLAG, true);
			rootJson.put(CommonConstants.COL_MSG, "修改资源成功");
		} catch (Exception e) {
			e.printStackTrace();
			rootJson.put(CommonConstants.COL_DEALFLAG, false);
			rootJson.put(CommonConstants.COL_MSG, "修改资源失败");
		}
		return rootJson;
	}

	/**
	 * 删除资源
	 * 
	 * @param id
	 *            资源ID
	 * @param menuId
	 *            菜单ID
	 * @return 返回删除成功或失败
	 */
	@RequestMapping("deleteResourceInfo")
	@ResponseBody
	public Map<String, Object> deleteResourceInfo(final Long id,
			final Long menuId, HttpSession session) {
		// json容器
		Map<String, Object> rootJson = new HashMap<String, Object>();
		try {
			// 首先根据菜单ID（menuId）查询出资源关联的菜单对象，
			// 然后把要删除的资源从菜单中移除，并修改菜单
			// 最后再删除资源
			MenuInfo menuInfo = this.menuInfoService.getById(menuId);
			ResourceInfo resourceInfo = this.resourceInfoService.getById(id);
			List<ResourceInfo> resources = menuInfo.getResources();
			resources.remove(resourceInfo);
			// 从session中获取用户信息，设置菜单的修改人ID和修改时间
			SessionData user = (SessionData) session
					.getAttribute(CommonConstants.SESSION_DATA);
			menuInfo.setUpdateUser(user.getUserId());
			menuInfo.setUpdateTime(new Date());
			this.menuInfoService.update(menuInfo);
			this.resourceInfoService.deleteById(id);
			// 如果删除了资源，则url与角色之间的对应关系发生变化，此时需要把resourceMap设置为null，
			// 这样再次发送请求时就会重新加载URL与角色之间的对应关系
			FableSecurityMetadataSource.setResourceMap(null);
			rootJson.put(CommonConstants.COL_DEALFLAG, true);
			rootJson.put(CommonConstants.COL_MSG, "删除资源成功");
		} catch (Exception e) {
			e.printStackTrace();
			rootJson.put(CommonConstants.COL_DEALFLAG, false);
			rootJson.put(CommonConstants.COL_MSG, "删除资源失败");
		}
		return rootJson;
	}

	/**
	 * 判断资源名是否重复
	 * 
	 * @param name
	 *            资源名
	 * @param resourceId
	 *            要修改的资源的ID
	 * @return
	 */
	@RequestMapping("isSameResName")
	@ResponseBody
	public Map<String, Object> isSameResName(String name, Long resourceId) {
		// json数据容器
		Map<String, Object> rootJson = new HashMap<String, Object>();
		ResourceInfo ri = this.resourceInfoService.getResourceInfoByName(name);
		// 如果资源名重复则返回false，否则返回true
		if (ri != null && !ri.getId().equals(resourceId))
			rootJson.put(CommonConstants.COL_DEALFLAG, false);
		else
			rootJson.put(CommonConstants.COL_DEALFLAG, true);
		return rootJson;
	}

	/**
	 * 判断资源URL是否重复
	 * 
	 * @param url
	 *            资源URL
	 * @param resourceId
	 *            资源ID
	 * @return
	 */
	@RequestMapping("isSameURL")
	@ResponseBody
	public Map<String, Object> isSameURL(String url, Long resourceId) {
		// json数据容器
		Map<String, Object> rootJson = new HashMap<String, Object>();
		// 首先根据url查询资源，
		// 然后根据查询结果做如下判断：
		// （1）如果查询结果为null或结果不为null但资源对象的id与传入的resourceId相同，则说明url不重复，
		// （2）否则url重复
		ResourceInfo ri = this.resourceInfoService.getResourceInfoByURL(url);
		// 如果资源URL重复则返回false，否则返回true
		if (ri != null && !(ri.getId().equals(resourceId)))
			rootJson.put(CommonConstants.COL_DEALFLAG, false);
		else
			rootJson.put(CommonConstants.COL_DEALFLAG, true);
		return rootJson;
	}

}

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
import com.fable.outer.security.FableSecurityMetadataSource;
import com.fable.outer.service.system.resource.intf.MenuInfoService;
import com.fable.outer.service.system.resource.intf.ResourceInfoService;

/**
 * 
 * @author liuz
 * 
 */
@Controller
@RequestMapping("/resourceInfo")
public class ResourceInfoController {

	@Autowired
	ResourceInfoService resourceInfoService;
	@Autowired
	MenuInfoService menuInfoService;

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

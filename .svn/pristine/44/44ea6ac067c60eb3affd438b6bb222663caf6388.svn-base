package com.fable.outer.service.system.resource.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fable.dsp.common.constants.CommonConstants;
import com.fable.outer.dao.system.resource.intf.MenuInfoDao;
import com.fable.outer.dao.system.resource.intf.RoleInfoDao;
import com.fable.outer.dmo.system.resource.MenuInfo;
import com.fable.outer.dmo.system.resource.ResourceInfo;
import com.fable.outer.dmo.system.resource.RoleInfo;
import com.fable.outer.dto.MenuInfoDto;
import com.fable.outer.service.system.resource.intf.MenuInfoService;

/**
 * 
 * @author liuz
 * 
 */
@Service("menuInfoService")
public class MenuInfoServiceImpl implements MenuInfoService {

	@Autowired
	MenuInfoDao menuInfoDao;
	@Autowired
	RoleInfoDao roleInfoDao;

	@Override
	public List<ResourceInfo> findResourceInfoList(Long menuId) {
		MenuInfo menuInfo = this.menuInfoDao.getById(menuId);
		return menuInfo.getResources();
	}

	@Override
	public void merge(MenuInfo mi) {
		this.menuInfoDao.merge(mi);
	}

	@Override
	public MenuInfo getMenuInfoByName(String menuName) {
		return this.menuInfoDao.getMenuInfoByName(menuName);
	}

	@Override
	public List<MenuInfo> findMenuInfoList() {
		return this.menuInfoDao.findMenuInfoList();
	}

	@Override
	public void insert(MenuInfo entity) {
		this.menuInfoDao.insert(entity);
	}

	@Override
	public void deleteById(Long id) {
		this.menuInfoDao.deleteById(id);
	}

	@Override
	public void delete(MenuInfo entity) {
		this.menuInfoDao.delete(entity);
	}

	@Override
	public void update(MenuInfo entity) {
		this.menuInfoDao.update(entity);
	}

	@Override
	public MenuInfo getById(Long id) {
		return this.menuInfoDao.getById(id);
	}

	@Override
	public List<MenuInfo> findMenuInfoByResourceInfo(ResourceInfo resourceInfo) {
		return this.menuInfoDao.findMenuInfoByResourceInfo(resourceInfo);
	}

	@Override
	public List<MenuInfoDto> findMenuInfoListByRoleId(Long roleId) {
		RoleInfo roleInfo = this.roleInfoDao.getById(roleId);
		// 获取角色拥有的菜单列表
		List<MenuInfo> roleMenus = roleInfo.getMenus();
		// 获取一级菜单列表
		List<MenuInfo> menus = this.menuInfoDao.findMenuInfoList();
		List<MenuInfoDto> menuInfoDtos = new ArrayList<MenuInfoDto>();
		menuInfoDtos = getMenuInfoDtos(roleMenus, menus, menuInfoDtos);
		return menuInfoDtos;
	}

	/**
	 * 获取登陆用户拥有是菜单信息（DTO）列表，其中包含菜单之间的关系
	 * 
	 * @param roleMenus
	 *            登陆用户拥有的菜单列表
	 * @param menus
	 *            一级菜单列表
	 * @param menuInfoDtos
	 *            菜单DTO列表
	 * @return 菜单DTO列表
	 */
	private List<MenuInfoDto> getMenuInfoDtos(List<MenuInfo> roleMenus,
			List<MenuInfo> menus, List<MenuInfoDto> menuInfoDtos) {
		if (menus == null || menus.isEmpty())
			return null;
		for (int i = 0; i < menus.size(); i++) {
			MenuInfo menu = menus.get(i);
			List<MenuInfo> childMenus = menu.getChildMenus();
			if (childMenus != null && !childMenus.isEmpty()) {
				// menu不是叶子菜单，继续遍历
				// 为了保存菜单之间的关联关系，需要把menu转换为DTO，但不能直接使用BeanUtils.copyProperties方法，
				// 否则，menuInfoDto的子菜单不是DTO类型的
				MenuInfoDto menuInfoDto = new MenuInfoDto();
				menuInfoDto.setId(menu.getId());
				menuInfoDto.setMenuName(menu.getMenuName());
				menuInfoDto.setUrl(menu.getUrl());
				menuInfoDto.setIconUrl(menu.getIconUrl());
				menuInfoDto.setChildMenus(new ArrayList<MenuInfoDto>());
				List<MenuInfoDto> miDtos = getMenuInfoDtos(roleMenus,
						childMenus, menuInfoDto.getChildMenus());
				// 如果角色菜单列表中含有某级菜单的叶子菜单，就将该级菜单添加到DTO中
				if (miDtos != null && !miDtos.isEmpty())
					menuInfoDtos.add(menuInfoDto);
			} else {
				// 当menu为叶子菜单（即没有子菜单）且是最低级菜单时，判断角色菜单列表中是否含有menu，如果没有就将其置为null；
				// 如果menu在角色菜单列表中，就将其转换为dto；
				// 如果menu是叶子菜单，但不是最低级菜单，则不做任何处理，
				// 此时返回的dto列表中没有元素
				// 使用contains方法需要在MenuInfo中重写equals方法
				if (menu.getMenuLevel() == CommonConstants.MENU_MAX_LEVEL) {
					if (!roleMenus.contains(menu))
						menus.set(i, null);
					else {
						MenuInfoDto miDto = new MenuInfoDto();
						BeanUtils.copyProperties(menu, miDto, new String[] {
								"sortOrder", "delFlag", "description",
								"parentMenu", "resources", "roles" });
						menuInfoDtos.add(miDto);
					}
				}
			}
		}
		return menuInfoDtos;
	}

	/**
	 * 获取角色拥有的菜单列表，其中包含菜单之间的关系
	 * 
	 * @param roleMenus
	 *            角色拥有的菜单列表
	 * @param menus
	 *            一级菜单列表
	 * @return
	 */
	@SuppressWarnings("unused")
	private List<MenuInfo> getMenuInfos(List<MenuInfo> roleMenus,
			List<MenuInfo> menus) {
		for (MenuInfo menu : menus) {
			List<MenuInfo> childMenus = menu.getChildMenus();
			if (childMenus != null && !childMenus.isEmpty()) {
				// menu不是叶子菜单，继续遍历
				getMenuInfos(roleMenus, childMenus);
			} else {
				// menu为叶子菜单，判断角色菜单中是否含有menu，如果没有就将其移除
				// 使用contains方法需要在MenuInfo中重写equals方法
				if (!roleMenus.contains(menu))
					menus.remove(menu);
			}
		}
		return menus;
	}

}

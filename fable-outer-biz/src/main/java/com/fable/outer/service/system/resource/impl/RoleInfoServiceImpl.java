package com.fable.outer.service.system.resource.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fable.dsp.core.page.Page;
import com.fable.dsp.core.page.PageList;
import com.fable.outer.dao.system.resource.intf.MenuInfoDao;
import com.fable.outer.dao.system.resource.intf.RoleInfoDao;
import com.fable.outer.dmo.system.resource.MenuInfo;
import com.fable.outer.dmo.system.resource.RoleInfo;
import com.fable.outer.service.system.resource.intf.RoleInfoService;

@Service("roleInfoService")
public class RoleInfoServiceImpl implements RoleInfoService {

	@Autowired
	RoleInfoDao roleInfoDao;
	@Autowired
	MenuInfoDao menuInfoDao;

	@Override
	public RoleInfo configMenus(RoleInfo roleInfo, String menuIds) {
		// 首先根据角色ID查询出要配置菜单的角色对象
		// 然后，根据menuIds解析出的菜单ID查询出对应的菜单对象
		// 最后，为角色配置菜单
		roleInfo = roleInfoDao.getById(roleInfo.getId());
		List<MenuInfo> menus=new ArrayList<MenuInfo>();
		if (menuIds != null && !"".equals(menuIds)) {
			String[] ids = menuIds.split(",");
			for (String id : ids) {
				MenuInfo menuInfo=menuInfoDao.getById(Long.valueOf(id));
				menus.add(menuInfo);
			}
		}
		roleInfo.setMenus(menus);
		return roleInfo;
	}
	
	@Override
	public List<RoleInfo> findRoleInfoByMenuInfo(MenuInfo menuInfo) {
		return this.roleInfoDao.findRoleInfoByMenuInfo(menuInfo);
	}

	@Override
	public PageList<RoleInfo> findRoleInfoByPage(Page pager, RoleInfo roleInfo) {
		return roleInfoDao.findRoleInfoByPage(pager, roleInfo);
	}

	@Override
	public RoleInfo getRoleInfoByName(String roleName) {
		return roleInfoDao.getRoleInfoByName(roleName);
	}

	@Override
	public List<RoleInfo> findRoleInfoList() {
		return this.roleInfoDao.findRoleInfoList();
	}

	@Override
	public void merge(RoleInfo roleInfo) {
		this.roleInfoDao.mergeRoleInfo(roleInfo);
	}

	/**
	 * 
	 * @param id
	 *            角色主键
	 * @return 返回角色对象
	 */
	public RoleInfo getRoleById(final Long id) {
		return this.roleInfoDao.getById(id);
	}

	@Override
	public void insert(final RoleInfo entity) {
		this.roleInfoDao.insert(entity);
	}

	@Override
	public void deleteById(final Long id) {
		this.roleInfoDao.deleteById(id);
	}

	@Override
	public void delete(final RoleInfo entity) {
		this.roleInfoDao.delete(entity);
	}

	@Override
	public void update(final RoleInfo entity) {
		this.roleInfoDao.update(entity);
	}

	@Override
	public RoleInfo getById(final Long id) {
		final RoleInfo ro = this.roleInfoDao.getById(id);
		if (ro != null)
			return ro;
		return new RoleInfo();
	}

}

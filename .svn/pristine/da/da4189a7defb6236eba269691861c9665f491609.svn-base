package com.fable.outer.dao.system.resource.intf;

import java.util.List;

import com.fable.dsp.core.dao.GenericDao;
import com.fable.outer.dmo.system.resource.MenuInfo;
import com.fable.outer.dmo.system.resource.ResourceInfo;

public interface MenuInfoDao extends GenericDao<MenuInfo> {
	/**
	 * 查询一级菜单信息
	 * 
	 * @return 返回一级菜单信息列表
	 */
	List<MenuInfo> findMenuInfoList();
	/**
	 * 根据菜单名查询菜单信息
	 * @param menuName 菜单名
	 * @return 返回查到的菜单对象，如果没有查到则返回null
	 */
	MenuInfo getMenuInfoByName(String menuName);
	/**
	 * 修改菜单
	 * @param mi 要修改的菜单对象
	 */
	void merge(MenuInfo mi);
	/**
	 * 根据资源信息查询其关联的菜单信息
	 * @param resourceInfo 资源对象
	 * @return 返回查询到的菜单列表
	 */
	List<MenuInfo> findMenuInfoByResourceInfo(ResourceInfo resourceInfo);

}

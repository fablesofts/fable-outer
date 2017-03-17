package com.fable.outer.service.system.resource.intf;

import java.util.List;

import com.fable.dsp.core.page.Page;
import com.fable.dsp.core.page.PageList;
import com.fable.dsp.core.service.GenericService;
import com.fable.outer.dmo.system.resource.MenuInfo;
import com.fable.outer.dmo.system.resource.RoleInfo;

/**
 * 
 * @author 汪朝
 * 
 */
public interface RoleInfoService extends GenericService<RoleInfo> {
	/**
	 * 查找所有权限用于显示
	 * 
	 * @return 权限对象集合
	 */
	List<RoleInfo> findRoleInfoList();

	/**
	 * 分页查询角色信息
	 * 
	 * @param pager
	 *            分页参数对象
	 * @param roleInfo
	 *            角色对象
	 * @return 返回指定页的角色信息
	 */
	PageList<RoleInfo> findRoleInfoByPage(Page pager, RoleInfo roleInfo);
	/**
	 * 根据角色名查询角色信息
	 * @param roleName 角色名
	 * @return 返回查询的角色对象，如果不存在则返回null
	 */
	RoleInfo getRoleInfoByName(String roleName);
	/**
	 * 更新角色信息
	 * @param roleInfo 要更新的角色信息对象
	 */
	void merge(RoleInfo roleInfo);
	/**
	 * 为指定的角色配置菜单
	 * @param roleId 要配置菜单的角色ID
	 * @param menuIds 菜单ID组成的字符串
	 * @return 返回配置好菜单的角色对象
	 */
	RoleInfo configMenus(RoleInfo roleInfo, String menuIds);
	/**
	 * 根据菜单信息查询关联的角色信息
	 * @param menuInfo 菜单信息对象
	 * @return 返回关联的角色信息列表
	 */
	List<RoleInfo> findRoleInfoByMenuInfo(MenuInfo menuInfo);

}

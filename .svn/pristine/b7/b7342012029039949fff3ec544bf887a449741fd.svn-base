package com.fable.outer.dao.system.resource.intf;

import java.util.List;

import com.fable.dsp.core.dao.GenericDao;
import com.fable.dsp.core.page.Page;
import com.fable.dsp.core.page.PageList;
import com.fable.outer.dmo.system.resource.MenuInfo;
import com.fable.outer.dmo.system.resource.RoleInfo;

/**
 * 
 * @author 汪朝 20140319
 * 
 */
public interface RoleInfoDao extends GenericDao<RoleInfo> {

	/**
	 * 通过roleName查找角色对象
	 * 
	 * @param roleName
	 * @return
	 */
	public RoleInfo getRoleInfoByName(String roleName);

	/**
	 * 通过条件查找多条角色
	 * 
	 * @return
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
	public PageList<RoleInfo> findRoleInfoByPage(Page pager, RoleInfo roleInfo);
	/**
	 * 更新角色信息
	 * @param roleInfo 需要更新的角色对象
	 */
	public void mergeRoleInfo(RoleInfo roleInfo);
	/**
	 * 根据菜单信息查询关联的角色信息
	 * @param menuInfo 菜单信息对象
	 * @return 返回关联的角色信息列表
	 */
	public List<RoleInfo> findRoleInfoByMenuInfo(MenuInfo menuInfo);
	
}

package com.fable.outer.dao.system.resource.intf;

import java.util.List;

import com.fable.dsp.core.dao.GenericDao;
import com.fable.dsp.core.page.Page;
import com.fable.dsp.core.page.PageList;
import com.fable.outer.dmo.system.resource.ResourceInfo;

/**
 * 
 * @author 汪朝 20140319
 * 
 */
public interface ResourceInfoDao extends GenericDao<ResourceInfo> {
	/**
	 * 查找所有的系统资源
	 * 
	 * @return 返回所有的资源对象
	 */
	public List<ResourceInfo> listAllResource();
	/**
	 * 分页查询菜单对应的资源信息
	 * @param pager 分页参数对象
	 * @param menuId 菜单ID
	 * @return 返回查询到的资源信息列表
	 */
	public PageList<ResourceInfo> findResourceInfoByPage(Page pager, Long menuId);
	/**
	 * 根据资源名查询资源信息
	 * @param name 资源名
	 * @return 返回查询到的资源对象，如果没有查到则返回null
	 */
	public ResourceInfo getResourceInfoByName(String name);
	/**
	 * 修改资源
	 * @param ri 要修改的资源对象
	 */
	public void merge(ResourceInfo ri);
	/**
	 * 根据资源URL查询资源信息
	 * @param url 资源URL
	 * @return 返回查询到的资源对象，如果没有则返回null
	 */
	public ResourceInfo getResourceInfoByURL(String url);
}

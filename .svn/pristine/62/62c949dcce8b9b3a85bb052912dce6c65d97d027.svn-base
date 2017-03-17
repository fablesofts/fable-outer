package com.fable.outer.service.system.resource.intf;

import java.util.Map;

import com.fable.dsp.core.page.Page;
import com.fable.dsp.core.page.PageList;
import com.fable.dsp.core.service.GenericService;
import com.fable.outer.dmo.system.resource.ResourceInfo;

/**
 * 
 * @author liuz
 * 
 */
public interface ResourceInfoService extends GenericService<ResourceInfo> {
	/**
	 * 分页查询菜单对应的资源信息
	 * @param pager 分页参数对象
	 * @param menuId 菜单ID
	 * @return 返回查询到的资源信息列表
	 */
	PageList<ResourceInfo> findResourceInfoByPage(Page pager, Long menuId);
	/**
	 * 根据资源名查询资源信息
	 * @param name 资源名
	 * @return 返回查询到的资源对象，如果没有查到则返回null
	 */
	ResourceInfo getResourceInfoByName(String name);
	/**
	 * 修改资源
	 * @param ri 要修改的资源对象
	 */
	void merge(ResourceInfo ri);
	/**
	 * 根据资源URL查询资源信息
	 * @param url 资源URL
	 * @return 返回查询到的资源对象，如果没有则返回null
	 */
	ResourceInfo getResourceInfoByURL(String url);
	/**
	 * 获取资源与权限的关系
	 * @return
	 */
	Map<String,Object> getResourceMap();
}

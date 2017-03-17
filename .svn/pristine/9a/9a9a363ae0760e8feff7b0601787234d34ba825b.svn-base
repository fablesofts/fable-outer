package com.fable.outer.dao.system.resource.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;

import com.fable.dsp.core.dao.hibernate.GenericDaoHibernate;
import com.fable.dsp.core.page.Page;
import com.fable.dsp.core.page.PageList;
import com.fable.outer.dao.system.resource.intf.ResourceInfoDao;
import com.fable.outer.dmo.system.resource.ResourceInfo;

/**
 * 
 * @author 汪朝
 * 
 */
@Repository("resourceInfoDao")
public class ResourceInfoDaoHibernate extends GenericDaoHibernate<ResourceInfo>
		implements ResourceInfoDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<ResourceInfo> listAllResource() {
		// 通过排序,让URL通配放到下面去
		final Session session = this.getHibernateTemplate().getSessionFactory()
				.getCurrentSession();
		List<ResourceInfo> resources = session.createCriteria(ResourceInfo.class)
				.addOrder(Order.desc("url")).list();
		return resources;
	}

	@Override
	public PageList<ResourceInfo> findResourceInfoByPage(Page pager, Long menuId) {
		// SELECT COUNT(*) FROM sys_resource_info AS ri JOIN sys_menu_res AS mr
		// ON ri.ID=mr.RESOURCE_ID WHERE MENU_ID=3
		String sql = "SELECT COUNT(*) FROM sys_resource_info AS ri JOIN sys_menu_res AS mr ON ri.ID=mr.RESOURCE_ID WHERE mr.MENU_ID=?";
		final Session session = this.getHibernateTemplate().getSessionFactory()
				.openSession();
		Long count = Long.valueOf(session.createSQLQuery(sql)
				.setParameter(0, menuId).uniqueResult().toString());
		PageList<ResourceInfo> pageList = new PageList<ResourceInfo>();
		if (count > 0) {
			// 指定的菜单有关联的资源，分页查询资源
			// sql =
			// "SELECT * FROM sys_resource_info AS ri JOIN sys_menu_res AS mr ON ri.ID=mr.RESOURCE_ID WHERE mr.MENU_ID=?";
			String hql = "select mi.resources from MenuInfo mi where mi.id=?";
			@SuppressWarnings("unchecked")
			List<ResourceInfo> list = session.createQuery(hql)
					.setParameter(0, menuId).setFirstResult(pager.getIndex())
					.setMaxResults(pager.getPageSize()).list();
			pageList.setList(list);
			pager.setCount(count);
		} else {
			// 指定的菜单没有关联的资源，不查询
			pageList.setList(new ArrayList<ResourceInfo>());
			pager.setCount(0);
		}
		pageList.setPage(pager);
		session.close();
		return pageList;
	}

	@Override
	public ResourceInfo getResourceInfoByName(String name) {
		final String hql = "from ResourceInfo where resourceName=?";
		final Session session = this.getHibernateTemplate().getSessionFactory()
				.openSession();
		ResourceInfo resourceInfo = (ResourceInfo) session.createQuery(hql)
				.setParameter(0, name).uniqueResult();
		session.close();
		return resourceInfo;
	}

	@Override
	public void merge(ResourceInfo ri) {
		this.getHibernateTemplate().merge(ri);
	}

	@Override
	public ResourceInfo getResourceInfoByURL(String url) {
		final String hql = "from ResourceInfo where url=?";
		final Session session = this.getHibernateTemplate().getSessionFactory()
				.openSession();
		ResourceInfo resourceInfo = (ResourceInfo) session.createQuery(hql)
				.setParameter(0, url).uniqueResult();
		session.close();
		return resourceInfo;
	}
}

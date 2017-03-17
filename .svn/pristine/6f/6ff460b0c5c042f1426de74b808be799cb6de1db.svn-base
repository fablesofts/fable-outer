package com.fable.outer.dao.system.resource.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.fable.dsp.core.dao.hibernate.GenericDaoHibernate;
import com.fable.outer.dao.system.resource.intf.MenuInfoDao;
import com.fable.outer.dmo.system.resource.MenuInfo;
import com.fable.outer.dmo.system.resource.ResourceInfo;

/**
 * 
 * @author 汪朝
 * 
 */
@Repository("menuInfoDao")
public class MenuInfoDaoHibernate extends GenericDaoHibernate<MenuInfo>
		implements MenuInfoDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<MenuInfo> findMenuInfoList() {
		// 查询一级菜单的SQL语句
		// SELECT * FROM SYS_MENU_INFO WHERE PID IS NULL;
		final String hql = "from MenuInfo where parentMenu is null";
		return this.getHibernateTemplate().getSessionFactory()
				.getCurrentSession().createQuery(hql).list();
	}

	@Override
	public MenuInfo getMenuInfoByName(String menuName) {
		final String hql = "from MenuInfo where menuName=?";
		return (MenuInfo) this.getHibernateTemplate().getSessionFactory()
				.getCurrentSession().createQuery(hql).setParameter(0, menuName)
				.uniqueResult();
	}

	@Override
	public void merge(MenuInfo mi) {
		this.getHibernateTemplate().merge(mi);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MenuInfo> findMenuInfoByResourceInfo(ResourceInfo resourceInfo) {
		// SELECT * FROM sys_menu_info AS mi JOIN sys_menu_res AS mr ON
		// mi.id=mr.MENU_ID WHERE mr.RESOURCE_ID=1
		final String sql = "SELECT * FROM sys_menu_info AS mi JOIN sys_menu_res AS mr ON mi.id=mr.MENU_ID WHERE mr.RESOURCE_ID=?";
		return this.getHibernateTemplate().getSessionFactory()
				.getCurrentSession().createSQLQuery(sql)
				.setParameter(0, resourceInfo.getId()).list();
	}

}

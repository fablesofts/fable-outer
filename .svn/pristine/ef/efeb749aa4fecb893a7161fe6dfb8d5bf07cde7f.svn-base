package com.fable.outer.dao.system.resource.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.fable.dsp.core.dao.hibernate.GenericDaoHibernate;
import com.fable.dsp.core.page.Page;
import com.fable.dsp.core.page.PageList;
import com.fable.outer.dao.system.resource.intf.RoleInfoDao;
import com.fable.outer.dmo.system.resource.MenuInfo;
import com.fable.outer.dmo.system.resource.RoleInfo;

/**
 * 
 * @author 汪朝 20140319
 * 
 */
@Repository("roleInfoDao")
public class RoleInfoDaoHibernate extends GenericDaoHibernate<RoleInfo>
		implements RoleInfoDao {

	@Override
	public PageList<RoleInfo> findRoleInfoByPage(Page pager, RoleInfo roleInfo) {
		PageList<RoleInfo> pageList = new PageList<RoleInfo>();
		// 首先查询数据库中角色记录的数量，并设置分页参数；
		// 然后根据记录的数量进行如下操作；
		// 如果数据库中有记录就分页查询，否则不查询。
		Long count = Long.valueOf(this.getHibernateTemplate()
				.findByExample(roleInfo).size());
		pager.setCount(count);
		pageList.setPage(pager);
		if (count > 0) {
			@SuppressWarnings("unchecked")
			List<RoleInfo> list = this.getHibernateTemplate().findByExample(
					roleInfo == null ? new RoleInfo() : roleInfo,
					pager.getIndex(), pager.getPageSize());
			pageList.setList(list);
		} else {
			pageList.setList(new ArrayList<RoleInfo>());
		}
		return pageList;
	}

	@Override
	public List<RoleInfo> findRoleInfoByMenuInfo(MenuInfo menuInfo) {
		// SELECT * FROM SYS_ROLE_INFO AS ri JOIN SYS_ROLE_MENU AS rm ON
		// ri.id=rm.ROLE_ID WHERE rm.MENU_ID=3
		final String sql = "SELECT * FROM SYS_ROLE_INFO AS ri JOIN SYS_ROLE_MENU AS rm ON ri.id=rm.ROLE_ID WHERE rm.MENU_ID=?";
		final Session session = this.getHibernateTemplate().getSessionFactory()
				.openSession();
		@SuppressWarnings("unchecked")
		List<RoleInfo> roles = session.createSQLQuery(sql)
				.setParameter(0, menuInfo.getId()).list();
		session.close();
		return roles;
	}

	/**
	 * 通过rolename获得roleInfo对象
	 */
	@Override
	public RoleInfo getRoleInfoByName(String roleName) {
		final String hql = "from RoleInfo as role where role.roleName = ?";
		final Session session = this.getHibernateTemplate().getSessionFactory()
				.openSession();
		RoleInfo roleInfo = (RoleInfo) session.createQuery(hql)
				.setParameter(0, roleName).uniqueResult();
		session.close();
		return roleInfo;
	}

	/**
	 * 为了管理用户权限
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<RoleInfo> findRoleInfoList() {
		return getHibernateTemplate().findByExample(new RoleInfo());
	}

	@Override
	public void mergeRoleInfo(RoleInfo roleInfo) {
		this.getHibernateTemplate().merge(roleInfo);
	}

}

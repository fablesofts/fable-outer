package com.fable.outer.dao.system.resource.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.fable.dsp.core.dao.hibernate.GenericDaoHibernate;
import com.fable.dsp.core.page.Page;
import com.fable.dsp.core.page.PageList;
import com.fable.outer.dao.system.resource.intf.UserInfoDao;
import com.fable.outer.dmo.system.resource.RoleInfo;
import com.fable.outer.dmo.system.resource.UserInfo;

/**
 * 
 * @author 汪朝
 * 
 */
@Repository("userInfoDao")
public class UserDaoHibernate extends GenericDaoHibernate<UserInfo> implements
		UserInfoDao {
	@Override
	public UserInfo getUserInfoByUsername(String username) {
		// "SELECT * FROM USER_INFO WHERE USER_NAME = " + username;
		final String hql = "from UserInfo where username = ?";
		@SuppressWarnings("unchecked")
		List<UserInfo> list = this.getHibernateTemplate().find(hql, username);
		return list.isEmpty() ? null : list.get(0);

	}

	@Override
	public List<UserInfo> findUserInfoListByConditions(UserInfo userinfo) {
		@SuppressWarnings("unchecked")
		List<UserInfo> list = getHibernateTemplate().findByExample(userinfo);
		return list;
	}

	@Override
	public PageList<UserInfo> findPageUserInfo(Page pager, UserInfo userinfo) {
		PageList<UserInfo> pageUserInfo = new PageList<UserInfo>();
		Long count = Long.valueOf(this.findUserInfoListByConditions(userinfo)
				.size());
		if (count > 0) {
			@SuppressWarnings("unchecked")
			List<UserInfo> list = getHibernateTemplate().findByExample(
					userinfo == null ? new UserInfo() : userinfo,
					pager.getIndex(), pager.getPageSize());
			pager.setCount(count);
			pageUserInfo.setList(list);
		} else {
			pageUserInfo.setList(new ArrayList<UserInfo>());
			pager.setCount(0);
		}
		pageUserInfo.setPage(pager);
		return pageUserInfo;
	}

	@Override
	public void mergeUserInfo(UserInfo userinfo) {
		this.getHibernateTemplate().merge(userinfo);
	}

	@Override
	public List<UserInfo> findUserInfoByRoleInfo(RoleInfo roleInfo) {
		// SELECT * FROM SYS_USER_INFO AS ui JOIN SYS_USER_ROLE AS ur ON
		// ui.id=ur.USER_ID WHERE ur.ROLE_ID=1;
		final String sql = "SELECT * FROM SYS_USER_INFO AS ui JOIN SYS_USER_ROLE AS ur ON ui.id=ur.USER_ID WHERE ur.ROLE_ID=?";
		final Session session = this.getHibernateTemplate().getSessionFactory()
				.openSession();
		@SuppressWarnings("unchecked")
		List<UserInfo> users = session.createSQLQuery(sql)
				.setParameter(0, roleInfo.getId()).list();
		session.close();
		return users;
	}
	
}

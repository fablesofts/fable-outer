package com.fable.outer.dao.system.resource.intf;

import java.util.List;

import com.fable.dsp.core.dao.GenericDao;
import com.fable.dsp.core.page.Page;
import com.fable.dsp.core.page.PageList;
import com.fable.outer.dmo.system.resource.RoleInfo;
import com.fable.outer.dmo.system.resource.UserInfo;

/**
 * 用户信息
 * 
 * @author 汪朝 20140319
 * 
 */
public interface UserInfoDao extends GenericDao<UserInfo> {

	/**
	 * 通过用户名查找用户
	 * @param username
	 * @return
	 */
	public UserInfo getUserInfoByUsername(String username);

	/**
	 * 通过条件查找多条
	 * 
	 * @param InterestInfo
	 * @return
	 */
	List<UserInfo> findUserInfoListByConditions(UserInfo userinfo);

	/**
	 * 通过条件查询分页数据
	 * 
	 * @param pager
	 * @param InterestInfo
	 *            为空查询全部数据
	 * @return
	 */
	PageList<UserInfo> findPageUserInfo(Page pager, UserInfo userinfo);

	/**
	 * marge修改用户信息对象
	 * 
	 * @param userinfo
	 *            用户信息对象
	 * 
	 */
	void mergeUserInfo(UserInfo userinfo);
	/**
	 * 根据角色信息查询用户信息
	 * @param roleInfo 角色信息对象
	 * @return 返回满足角色要求的用户信息列表
	 */
	public List<UserInfo> findUserInfoByRoleInfo(RoleInfo roleInfo);
}

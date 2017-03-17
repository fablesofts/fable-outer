package com.fable.outer.service.system.resource.intf;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.fable.dsp.core.page.Page;
import com.fable.dsp.core.page.PageList;
import com.fable.outer.dmo.system.resource.RoleInfo;
import com.fable.outer.dmo.system.resource.UserInfo;

/**
 * 
 * @author 汪朝 
 * 
 */
public interface UserInfoService extends UserDetailsService {
	/**
	 * 通过条件查找多条
	 * 
	 * @param UserInfo 用户信息实体
	 * @return
	 */
	List<UserInfo> findUserInfoListByConditions(UserInfo userinfo);

	/**
	 * 通过条件查找1条
	 * 
	 * @param UserInfo 用户信息实体
	 * @return
	 */
	UserInfo findUserInfoByConditions(UserInfo userinfo);
	
	
	/**
     * 通过条件查找1条
     * 
     * @param UserInfo 用户信息实体
     * @return
     */
    UserInfo findUserInfoById(Long id);
	


	/**
	 * 通过条件查询分页数据
	 * 
	 * @param pager 分页实体
	 * @param UserInfo 用户信息实体
	 *            为空查询全部数据
	 * @return
	 */
	PageList<UserInfo> findPageUserInfo(Page pager, UserInfo userinfo);
	
	 /**
     * 插入数据实体
     * @param UserInfo 用户信息实体
     */
    void insert(UserInfo userinfo);

    /**
     * 根据ID删除数据实体
     * @param id 主键
     */
    void deleteById(Long id);

    /**
     * 删除数据实体
     * @param UserInfo 用户信息实体
     */
    void delete(UserInfo userinfo);

    /**
     * 更新数据实体
     * @param UserInfo 用户信息实体
     */
    void update(UserInfo userinfo);

    /**
     * 根据ID获取数据实体
     * @param id 主键
     * @return UserInfo 数据实体
     */
    UserInfo getById(Long id);
    /**
     * 修改UserInfo
     * @param userinfo
     */
    void merge(UserInfo userinfo);
    
    UserInfo findUserInfoByName(String name);
    /**
     * 根据角色信息查询用户信息
     * @param roleInfo 角色信息对象
     * @return 返回查询到的用户信息列表
     */
	List<UserInfo> findUserInfoByRoleInfo(RoleInfo roleInfo);
}

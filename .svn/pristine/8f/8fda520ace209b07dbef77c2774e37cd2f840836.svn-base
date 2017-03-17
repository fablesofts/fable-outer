package com.fable.outer.service.system.resource.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fable.dsp.common.util.UtilMD5;
import com.fable.dsp.core.page.Page;
import com.fable.dsp.core.page.PageList;
import com.fable.outer.dao.system.resource.intf.RoleInfoDao;
import com.fable.outer.dao.system.resource.intf.UserInfoDao;
import com.fable.outer.dmo.system.resource.RoleInfo;
import com.fable.outer.dmo.system.resource.UserInfo;
import com.fable.outer.service.system.resource.intf.UserInfoService;

/**
 * 
 * @author 汪朝 20140319
 * 
 */
@Service("userInfoService")
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    UserInfoDao userInfoDao;
    @Autowired
    RoleInfoDao roleInfoDao;
    
    /**
     * @param username 用户名
     * @exception UsernameNotFoundException 没找到用户异常.
     * @return 返回用户详情
     */
    @Override
    public UserDetails loadUserByUsername(final String username)
        throws UsernameNotFoundException {
        System.out.println("username is " + username);
        // 这里应该可以不用再查了
        final UserInfo user =
            this.userInfoDao.getUserInfoByUsername(username);


        // Collection<GrantedAuthority> grantedAuths =
        // obtionGrantedAuthorities(user);

        // boolean enables = true;
        // boolean accountNonExpired = true;
        // boolean credentialsNonExpired = true;
        // boolean accountNonLocked = true;
        // // 封装成spring security的user
        // User userdetail = new User(users.getAccount(),
        // users.getPassword(), enables, accountNonExpired,
        // credentialsNonExpired, accountNonLocked,
        // grantedAuths);
        return user;
    }

    // 按条件查询多个用户
    @Override
    public List<UserInfo> findUserInfoListByConditions(
        final UserInfo userinfo) {
        return this.userInfoDao.findUserInfoListByConditions(userinfo);
    }

    // 按条件查询单个用户
    @Override
    public UserInfo findUserInfoByConditions(final UserInfo userinfo) {
        if (this.userInfoDao.findUserInfoListByConditions(userinfo).size() > 0)
            return this.userInfoDao.findUserInfoListByConditions(userinfo)
                .get(0);
        return null;
    }
    
    @Override
	public List<UserInfo> findUserInfoByRoleInfo(final RoleInfo roleInfo) {
		return this.userInfoDao.findUserInfoByRoleInfo(roleInfo);
	}

    // 按条件查询分页
    @Override
    public PageList<UserInfo> findPageUserInfo(final Page pager,
        final UserInfo userinfo) {
        return this.userInfoDao.findPageUserInfo(pager, userinfo);
    }

    @Override
    public void insert(final UserInfo userinfo) {
        userinfo.setPassword(UtilMD5.String2Md5(userinfo.getPassword()));
        this.userInfoDao.insert(userinfo);
    }

    @Override
    public void deleteById(final Long id) {
        this.userInfoDao.deleteById(id);
    }

    @Override
    public void delete(final UserInfo userinfo) {
        this.userInfoDao.delete(userinfo);
    }

    @Override
    public void update(final UserInfo userinfo) {
        this.userInfoDao.update(userinfo);
    }

    @Override
    public UserInfo getById(final Long id) {
        return this.userInfoDao.getById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void merge(final UserInfo userinfo) {
        this.userInfoDao.mergeUserInfo(userinfo);
    }

    @Override
    public UserInfo findUserInfoByName(final String name) {
        // TODO Auto-generated method stub
        return this.userInfoDao.getUserInfoByUsername(name);
    }

    @Override
    public UserInfo findUserInfoById(final Long id) {
        return this.userInfoDao.getById(id);
    }


    // //取得用户的权限
    // private Set<GrantedAuthority> obtionGrantedAuthorities(UserInfo user)
    // {
    // Set<GrantedAuthority> authSet = new HashSet<GrantedAuthority>();
    // List<Resources> resources = new ArrayList<Resources>();
    // Set<Roles> roles = user.getRoles();
    //
    // for(Roles role : roles) {
    // Set<Resources> tempRes = role.getResources();
    // for(Resources res : tempRes) {
    // resources.add(res);
    // }
    // }
    // for(Resources res : resources) {
    // authSet.add(new GrantedAuthorityImpl(res.getName()));
    // }
    // return authSet;
    // }
}

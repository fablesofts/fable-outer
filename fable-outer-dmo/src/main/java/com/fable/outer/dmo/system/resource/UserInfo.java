package com.fable.outer.dmo.system.resource;

import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fable.dsp.core.entity.AuditEntity;

/**
 * 用户信息
 * 
 * @author 汪朝 20140319
 */
@Entity
@Table(name = "SYS_USER_INFO")
public class UserInfo extends AuditEntity implements UserDetails {

	private static final long serialVersionUID = 8207666487567133549L;

	@TableGenerator(name = "sysUserGen", table = "SYS_ID_GEN", pkColumnName = "GEN_KEY", valueColumnName = "GEN_VALUE", pkColumnValue = "SYS_USER_ID", allocationSize = 1, initialValue = 20)
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "sysUserGen")
	private Long id;

	/** 用户名 */
	@Column(name = "USERNAME", nullable = false, unique = true, length = 32)
	private String username;

	/** 密码 */
	@Column(name = "PASSWORD", length = 32, nullable = false)
	private String password;

	/** 真实姓名 */
	@Column(name = "REAL_NAME", length = 32)
	protected String realname;

	/** 删除标志,1表示删除,0表示不删除 */
	@Column(name = "DEL_FLAG", length = 1)
	protected char delFlag = '0';

	/** 描述 */
	@Column(name = "DESCRIPTION", length = 512)
	protected String description;

	/** 角色 */
	@ManyToMany(cascade = CascadeType.MERGE)
	@LazyCollection(LazyCollectionOption.FALSE)
	@Fetch(FetchMode.SUBSELECT)
	@JoinTable(name = "SYS_USER_ROLE", joinColumns = { @JoinColumn(name = "USER_ID") }, inverseJoinColumns = { @JoinColumn(name = "ROLE_ID") })
	private List<RoleInfo> roles;

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public char getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(char delFlag) {
		this.delFlag = delFlag;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return roles;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<RoleInfo> getRoles() {
		return roles;
	}

	public void setRoles(List<RoleInfo> roles) {
		this.roles = roles;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
